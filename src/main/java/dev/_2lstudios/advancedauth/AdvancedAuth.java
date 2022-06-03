package dev._2lstudios.advancedauth;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import com.dotphin.milkshake.Milkshake;
import com.dotphin.milkshake.Provider;
import com.dotphin.milkshake.Repository;

import dev._2lstudios.advancedauth.api.AuthAPI;
import dev._2lstudios.advancedauth.api.events.AuthEvent;
import dev._2lstudios.advancedauth.cache.CacheEngine;
import dev._2lstudios.advancedauth.commands.AdvancedAuthCommand;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.commands.player.AddEmailCommand;
import dev._2lstudios.advancedauth.commands.player.AutoLoginCommand;
import dev._2lstudios.advancedauth.commands.player.ChangePasswordCommand;
import dev._2lstudios.advancedauth.commands.player.LoginCommand;
import dev._2lstudios.advancedauth.commands.player.LogoutCommand;
import dev._2lstudios.advancedauth.commands.player.RegisterCommand;
import dev._2lstudios.advancedauth.commands.player.UnregisterCommand;
import dev._2lstudios.advancedauth.config.ConfigManager;
import dev._2lstudios.advancedauth.config.Configuration;
import dev._2lstudios.advancedauth.errors.NoSuchCacheEngineException;
import dev._2lstudios.advancedauth.errors.NoSuchCipherException;
import dev._2lstudios.advancedauth.i18n.LanguageManager;
import dev._2lstudios.advancedauth.listeners.AsyncPlayerPreLoginListener;
import dev._2lstudios.advancedauth.listeners.PlayerJoinListener;
import dev._2lstudios.advancedauth.listeners.PlayerLoginListener;
import dev._2lstudios.advancedauth.listeners.PlayerQuitListener;
import dev._2lstudios.advancedauth.listeners.blockers.BlockerListenerHandler;
import dev._2lstudios.advancedauth.migration.MigrationManager;
import dev._2lstudios.advancedauth.players.AuthPlayerData;
import dev._2lstudios.advancedauth.players.AuthPlayerManager;
import dev._2lstudios.advancedauth.security.Cipher;
import dev._2lstudios.advancedauth.security.ConsoleFilter;
import dev._2lstudios.advancedauth.security.CountryCheck;
import dev._2lstudios.advancedauth.security.Faillock;
import dev._2lstudios.advancedauth.services.AuthService;
import dev._2lstudios.advancedauth.services.GeoIPService;
import dev._2lstudios.advancedauth.tasks.PlayerAuthNotifyTask;
import dev._2lstudios.advancedauth.tasks.PlayerDataFetchTask;
import dev._2lstudios.advancedauth.tasks.PlayerTimeoutTask;

public class AdvancedAuth extends JavaPlugin {
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private MigrationManager migrationManager;
    private AuthPlayerManager playerManager;

    private CacheEngine cache;
    private Repository<AuthPlayerData> playerDataRepository;

    private AuthService authService;
    
    private Cipher cipher;
    private CountryCheck countryCheck;
    private Faillock faillock;

    private List<String> silentCommands;
    private boolean shutdown = false;

    private void addCommand(CommandListener command) {
        command.register(this, false);

        String name = command.getCommandInfo().name();
        boolean silent = command.getCommandInfo().silent();

        if (silent) {
            this.silentCommands.add(name);
            Command cmd = this.getCommand(command.getCommandInfo().name());
            if (cmd != null) {
                for (String alias : cmd.getAliases()) {
                    this.silentCommands.add(alias);
                }
            }
        }
    }

    private void addListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
    
    public boolean callEvent(AuthEvent event) {
        this.getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public void shutdownServer() {
        if (!this.shutdown) {
            this.shutdown = true;
            this.getLogger().severe("Stopping server to avoid security breaches");
            this.getServer().shutdown();
        }
    }
    
    @Override
    public void onEnable () {
        // Initialize API.
        new AuthAPI(this);

        // Initialize important variables.
        this.silentCommands = new ArrayList<>();

        // Instantiate managers.
        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        this.migrationManager = new MigrationManager(this);
        this.playerManager = new AuthPlayerManager(this);

        // Load data.
        this.languageManager.loadLanguagesSafe();
        this.playerManager.addAll();

        // Register channels.
        Messenger messenger = this.getServer().getMessenger();

        boolean sendBungeeServer = this.getConfig().getBoolean("authentication.send-server-on-login.enabled"); 
        boolean bungeeHook = this.getConfig().getBoolean("settings.bungee-hook");

        if (sendBungeeServer || bungeeHook) {
            messenger.registerOutgoingPluginChannel(this, "BungeeCord");
        }

        // Setup logger.
        Logging.setLogger(this.getLogger());
        Logging.setEnabled(this.getConfig().getBoolean("settings.logging"));

        // Setup Console filter
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter(this));

        // Setup database.
        String databaseUri = this.getConfig().getString("storage.database.uri");
        String databaseCollection = this.getConfig().getString("storage.database.collection");

        if (databaseUri == null || databaseCollection == null) {
            this.getLogger().severe("Database URI or Database collection in config.yml isn't defined.");
            this.shutdownServer();
            return;
        }

        Provider provider = Milkshake.connect(databaseUri);
        this.playerDataRepository = Milkshake.addRepository(
            AuthPlayerData.class, 
            provider, 
            databaseCollection
        );

        // Setup cache engine.
        String driver = this.getConfig().getString("storage.cache.driver");
        String host = this.getConfig().getString("storage.cache.driver");
        int port = this.getConfig().getInt("storage.cache.driver");
        String password = this.getConfig().getString("storage.cache.driver");
        int expiration = this.getConfig().getInt("storage.cache.expiration");

        try {
            this.cache = CacheEngine.getEngine(driver, expiration, host, port, password);
        } catch (NoSuchCacheEngineException e) {
            e.printStackTrace();
            this.shutdownServer();
            return;
        }

        // Setup cipher.
        try {
            this.cipher = Cipher.getCipher(this.getConfig().getString("security.cipher", "bcrypt"));
        } catch (NoSuchCipherException e) {
            e.printStackTrace();
            this.shutdownServer();
            return;
        }

        // Initialize Services
        this.authService = new AuthService(this);

        try {
            GeoIPService.start(this.getDataFolder());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize security checks.
        this.countryCheck = new CountryCheck(this);
        this.faillock = new Faillock(this);

        // Register tasks
        long interval = this.getConfig().getInt("authentication.message-interval", 2) * 20L;

        this.getServer().getScheduler().runTaskTimer(this, new PlayerAuthNotifyTask(this), interval, interval);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new PlayerDataFetchTask(this), 20L, 20L);
        this.getServer().getScheduler().runTaskTimer(this, new PlayerTimeoutTask(this), 20L, 20L);

        // Register event blockers
        BlockerListenerHandler.register(this);

        // Register listeners
        this.addListener(new AsyncPlayerPreLoginListener(this));
        this.addListener(new PlayerJoinListener(this));
        this.addListener(new PlayerLoginListener(this));
        this.addListener(new PlayerQuitListener(this));

        // Register commands
        this.addCommand(new AddEmailCommand());
        this.addCommand(new AutoLoginCommand());
        this.addCommand(new ChangePasswordCommand());
        this.addCommand(new LoginCommand());
        this.addCommand(new LogoutCommand());
        this.addCommand(new RegisterCommand());
        this.addCommand(new UnregisterCommand());

        this.addCommand(new AdvancedAuthCommand());

        // Print welcome message if plugin starts correctly
        String cipherAlgorithm = this.getConfig().getString("security.cipher");
        String cacheDriver = this.getConfig().getString("storage.cache.driver");
        String storageDriver =  this.getConfig().getString("storage.database.uri").split("://")[0];
        String pluginVersion = this.getDescription().getVersion();

        this.getServer().getConsoleSender().sendMessage("§8============================================");
        this.getServer().getConsoleSender().sendMessage("              §6§lAdvanced§e§lAuth§r");
        this.getServer().getConsoleSender().sendMessage("§7- §eBungee Hook: " + (bungeeHook ? "§aYes" : "§cNo"));
        this.getServer().getConsoleSender().sendMessage("§7- §eCache Engine: §7" + cacheDriver);
        this.getServer().getConsoleSender().sendMessage("§7- §eCipher: §7" + cipherAlgorithm);
        this.getServer().getConsoleSender().sendMessage("§7- §eStorage: §7" + storageDriver);
        this.getServer().getConsoleSender().sendMessage("§7- §eVersion: §7" + pluginVersion);
        this.getServer().getConsoleSender().sendMessage("§8============================================");
    }

    @Override
    public void onDisable() {
        this.shutdownServer();
    }

    // Configuration getters
    public Configuration getConfig() {
        return this.configManager.getConfig("config.yml");
    }

    public Configuration getMigrationConfig() {
        return this.configManager.getConfig("migration.yml");
    }

    // Managers getters
    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public MigrationManager getMigrationManager() {
        return this.migrationManager;
    }

    public AuthPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    // Service getters
    public AuthService getAuthService() {
        return this.authService;
    }

    // Security modules
    public CountryCheck getCountryCheck() {
        return this.countryCheck;
    }

    public Faillock getFailLock() {
        return this.faillock;
    }

    // Providers getters
    public CacheEngine getCache() {
        return this.cache;
    }
    
    public Repository<AuthPlayerData> getPlayerDataRepository() {
        return this.playerDataRepository;
    }

    public Cipher getCipher() {
        return this.cipher;
    }

    public List<String> getSilentCommands() {
        return this.silentCommands;
    }

    // Others getters
    public boolean hasPlugin(String pluginName) {
        Plugin plugin = this.getServer().getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}