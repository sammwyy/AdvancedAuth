package dev._2lstudios.advancedauth.bukkit;

import java.io.IOException;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.providers.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import org.bukkit.configuration.InvalidConfigurationException;

import dev._2lstudios.advancedauth.bukkit.commands.AdvancedAuthCommand;
import dev._2lstudios.advancedauth.bukkit.commands.player.AddEmailCommand;
import dev._2lstudios.advancedauth.bukkit.commands.player.AutoLoginCommand;
import dev._2lstudios.advancedauth.bukkit.commands.player.ChangePasswordCommand;
import dev._2lstudios.advancedauth.bukkit.commands.player.LoginCommand;
import dev._2lstudios.advancedauth.bukkit.commands.player.LogoutCommand;
import dev._2lstudios.advancedauth.bukkit.commands.player.RegisterCommand;
import dev._2lstudios.advancedauth.bukkit.commands.player.UnregisterCommand;
import dev._2lstudios.advancedauth.bukkit.hooks.ProxyHook;
import dev._2lstudios.advancedauth.bukkit.hooks.impl.ProxyBungeecordHook;
import dev._2lstudios.advancedauth.bukkit.listeners.AsyncPlayerPreLoginListener;
import dev._2lstudios.advancedauth.bukkit.listeners.PlayerJoinListener;
import dev._2lstudios.advancedauth.bukkit.listeners.PlayerLoginListener;
import dev._2lstudios.advancedauth.bukkit.listeners.PlayerQuitListener;
import dev._2lstudios.advancedauth.bukkit.listeners.blockers.BlockerListenerHandler;
import dev._2lstudios.advancedauth.bukkit.migration.MigrationManager;
import dev._2lstudios.advancedauth.bukkit.player.AuthPlayerData;
import dev._2lstudios.advancedauth.bukkit.player.AuthPlayerManager;
import dev._2lstudios.advancedauth.bukkit.security.Cipher;
import dev._2lstudios.advancedauth.bukkit.security.ConsoleFilter;
import dev._2lstudios.advancedauth.bukkit.security.CountryCheck;
import dev._2lstudios.advancedauth.bukkit.security.Faillock;
import dev._2lstudios.advancedauth.bukkit.tasks.PlayerDataFetchTask;
import dev._2lstudios.advancedauth.bukkit.tasks.PlayerTimeoutTask;
import dev._2lstudios.advancedauth.common.cache.CacheEngine;
import dev._2lstudios.advancedauth.common.errors.NoSuchCacheEngineException;
import dev._2lstudios.advancedauth.common.errors.NoSuchCipherException;
import dev._2lstudios.advancedauth.common.services.GeoIPService;
import dev._2lstudios.advancedauth.bukkit.tasks.PlayerAuthNotifyTask;

import dev._2lstudios.jelly.JellyPlugin;
import dev._2lstudios.jelly.config.Configuration;

public class AdvancedAuth extends JellyPlugin {
    private Configuration mainConfig;
    private Configuration migrationConfig;

    private MigrationManager migration;
    private CacheEngine cache;
    private Cipher cipher;

    private CountryCheck countryCheck;
    private Faillock faillock;

    private ProxyHook proxyHook;

    private void setupDatabase() {
        // Connect to database.
        final String dbUri = this.mainConfig.getString("storage.database.uri", "mongodb://localhost/minecraft");
        final String dbCollection = this.mainConfig.getString("storage.database.collection", "Users");
        final Provider provider = MilkshakeORM.connect(dbUri);
        MilkshakeORM.addRepository(AuthPlayerData.class, provider, dbCollection);

        // Connect to cache engine.
        final String driver = this.mainConfig.getString("storage.cache.driver", "memory");
        final String host = this.mainConfig.getString("storage.cache.hist", "localhost");
        final int port = this.mainConfig.getInt("storage.cache.port", 6379);
        final String password = this.mainConfig.getString("storage.cache.password", "");
        final int expiration = this.mainConfig.getInt("storage.cache.expiration", 1440);

        try {
            this.cache = CacheEngine.getEngine(driver, expiration, host, port, password);
        } catch (final NoSuchCacheEngineException e) {
            e.printStackTrace();
            this.getLogger().severe("Stopping server to avoid security breaches");
            this.getServer().shutdown();
        }
    }

    private void setupCipher() {
        try {
            this.cipher = Cipher.getCipher(this.mainConfig.getString("security.cipher", "bcrypt"));
        } catch (final NoSuchCipherException e) {
            e.printStackTrace();
            this.getLogger().severe("Stopping server to avoid security breaches");
            this.getServer().shutdown();
        }
    }

    @Override
    public void onEnable() {
        // Set static instance
        instance = this;

        // Use hooks
        this.useCommandAPI();
        this.useConfigAPI();

        // Load configuration
        this.mainConfig = this.getConfig("config.yml");
        this.migrationConfig = this.getConfig("migration.yml");

        // Use language manager
        this.useLanguageAPI(this.mainConfig.getString("settings.default-lang", "en"));

        // Logger
        Logging.setLogger(this.getLogger());
        Logging.setEnabled(this.mainConfig.getBoolean("settings.logging"));
        
        // Logger filter
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter());
        
        // Extract any language file from jar
        try {
            this.getLanguageManager().loadLanguages();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Setup hooks
        if (this.mainConfig.getBoolean("authentication.send-server-on-login.enabled")) {
            final String proxyHookName = this.mainConfig.getString("authentication.send-server-on-login.proxy");
            if (proxyHookName.equalsIgnoreCase("bungeecord") || proxyHookName.equalsIgnoreCase("bungee")) {
                this.proxyHook = new ProxyBungeecordHook(this);
            }
        }

        // Setup database
        this.setupDatabase();

        // Initialize cipher
        this.setupCipher();

        // Initialize migration
        this.migration = new MigrationManager(this);

        // Register player manager
        this.setPluginPlayerManager(new AuthPlayerManager(this));
        
        // Initialize Services
        try {
            GeoIPService.start(this.getDataFolder());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize security checks.
        this.countryCheck = new CountryCheck(this);
        this.faillock = new Faillock(this);

        // Register tasks
        final long interval = this.getConfig().getInt("authentication.message-interval", 2) * 20L;

        this.getServer().getScheduler().runTaskTimer(this, new PlayerAuthNotifyTask(this), interval, interval);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new PlayerDataFetchTask(this), 20L, 20L);
        this.getServer().getScheduler().runTaskTimer(this, new PlayerTimeoutTask(this), 20L, 20L);

        // Register event blockers
        BlockerListenerHandler.register(this);

        // Register listeners
        this.addEventListener(new AsyncPlayerPreLoginListener(this));
        this.addEventListener(new PlayerJoinListener(this));
        this.addEventListener(new PlayerLoginListener(this));
        this.addEventListener(new PlayerQuitListener(this));

        // Register commands
        this.addCommand(new AddEmailCommand());
        this.addCommand(new AutoLoginCommand());
        this.addCommand(new ChangePasswordCommand());
        this.addCommand(new LoginCommand(this));
        this.addCommand(new LogoutCommand());
        this.addCommand(new RegisterCommand(this));
        this.addCommand(new UnregisterCommand());

        this.addCommand(new AdvancedAuthCommand(this));

        // Print welcome message if plugin starts correctly
        final String cipherAlgorithm = this.mainConfig.getString("security.cipher");
        final String cacheDriver = this.mainConfig.getString("storage.cache.driver");
        final String storageDriver =  this.mainConfig.getString("storage.database.uri").split("://")[0];
        final String pluginVersion = this.getDescription().getVersion();

        this.getServer().getConsoleSender().sendMessage("§8============================================");
        this.getServer().getConsoleSender().sendMessage("              §6§lAdvanced§e§lAuth§r");
        this.getServer().getConsoleSender().sendMessage("§7- §eCache Engine: §7" + cacheDriver);
        this.getServer().getConsoleSender().sendMessage("§7- §eCipher: §7" + cipherAlgorithm);
        this.getServer().getConsoleSender().sendMessage("§7- §eStorage: §7" + storageDriver);
        this.getServer().getConsoleSender().sendMessage("§7- §eVersion: §7" + pluginVersion);
        this.getServer().getConsoleSender().sendMessage("§8============================================");
    }

    /* Plugin getters */
    public CountryCheck getCountryCheck() {
        return this.countryCheck;
    }

    public Faillock getFailLock() {
        return this.faillock;
    }

    public MigrationManager getMigration() {
        return this.migration;
    }

    public CacheEngine getCache() {
        return this.cache;
    }

    public Cipher getCipher() {
        return this.cipher;
    }

    public Configuration getMainConfig() {
        return this.mainConfig;
    }

    public Configuration getMigrationConfig() {
        return this.migrationConfig;
    }

    public ProxyHook getProxyHook() {
        return this.proxyHook;
    }

    /* Static instance */
    private static AdvancedAuth instance;

    public static AdvancedAuth getInstance() {
        return instance;
    }
}
