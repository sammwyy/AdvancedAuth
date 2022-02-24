package dev._2lstudios.advancedauth;

import java.io.IOException;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.providers.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import org.bukkit.configuration.InvalidConfigurationException;

import dev._2lstudios.advancedauth.cache.CacheEngine;
import dev._2lstudios.advancedauth.commands.AdvancedAuthCommand;
import dev._2lstudios.advancedauth.commands.player.AddEmailCommand;
import dev._2lstudios.advancedauth.commands.player.ChangePasswordCommand;
import dev._2lstudios.advancedauth.commands.player.LoginCommand;
import dev._2lstudios.advancedauth.commands.player.LogoutCommand;
import dev._2lstudios.advancedauth.commands.player.RegisterCommand;
import dev._2lstudios.advancedauth.commands.player.UnregisterCommand;
import dev._2lstudios.advancedauth.errors.NoSuchCacheEngineException;
import dev._2lstudios.advancedauth.errors.NoSuchCipherException;
import dev._2lstudios.advancedauth.hooks.ProxyHook;
import dev._2lstudios.advancedauth.hooks.impl.ProxyBungeecordHook;
import dev._2lstudios.advancedauth.listeners.PlayerJoinListener;
import dev._2lstudios.advancedauth.listeners.PlayerLoginListener;
import dev._2lstudios.advancedauth.listeners.blockers.BlockerListenerHandler;
import dev._2lstudios.advancedauth.player.AuthPlayerData;
import dev._2lstudios.advancedauth.player.AuthPlayerManager;
import dev._2lstudios.advancedauth.security.Cipher;
import dev._2lstudios.advancedauth.security.ConsoleFilter;
import dev._2lstudios.advancedauth.tasks.PlayerDataFetchTask;
import dev._2lstudios.advancedauth.tasks.PlayerTimeoutTask;
import dev._2lstudios.advancedauth.tasks.PlayerAuthNotifyTask;

import dev._2lstudios.jelly.JellyPlugin;
import dev._2lstudios.jelly.config.Configuration;

public class AdvancedAuth extends JellyPlugin {
    private Configuration mainConfig;

    private CacheEngine cache;
    private Cipher cipher;

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
        this.useLanguageAPI();

        // Load configuration
        this.mainConfig = this.getConfig("config.yml");

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

        // Register player manager
        this.setPluginPlayerManager(new AuthPlayerManager(this));

        // Register tasks
        final long interval = this.getConfig().getInt("authentication.message-interval", 2) * 20L;

        this.getServer().getScheduler().runTaskTimer(this, new PlayerAuthNotifyTask(this), interval, interval);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new PlayerDataFetchTask(this), 20L, 20L);
        this.getServer().getScheduler().runTaskTimer(this, new PlayerTimeoutTask(this), 20L, 20L);

        // Register event blockers
        BlockerListenerHandler.register(this);

        // Register listeners
        this.addEventListener(new PlayerJoinListener(this));
        this.addEventListener(new PlayerLoginListener(this));

        // Register commands
        this.addCommand(new AddEmailCommand());
        this.addCommand(new ChangePasswordCommand());
        this.addCommand(new LoginCommand());
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
    public CacheEngine getCache() {
        return this.cache;
    }

    public Cipher getCipher() {
        return this.cipher;
    }

    public Configuration getMainConfig() {
        return this.mainConfig;
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
