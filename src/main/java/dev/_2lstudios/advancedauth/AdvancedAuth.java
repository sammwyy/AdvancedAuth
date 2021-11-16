package dev._2lstudios.advancedauth;

import java.io.IOException;

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
import dev._2lstudios.advancedauth.listeners.PlayerJoinListener;
import dev._2lstudios.advancedauth.listeners.blockers.BlockerListenerHandler;
import dev._2lstudios.advancedauth.player.AuthPlayerData;
import dev._2lstudios.advancedauth.player.AuthPlayerManager;
import dev._2lstudios.advancedauth.security.Cipher;
import dev._2lstudios.advancedauth.tasks.PlayerDataFetchTask;
import dev._2lstudios.advancedauth.tasks.PlayerTimeoutTask;
import dev._2lstudios.advancedauth.tasks.PlayerAuthNotifyTask;
import dev._2lstudios.advancedauth.utils.URI;

import dev._2lstudios.jelly.JellyPlugin;
import dev._2lstudios.jelly.config.Configuration;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.providers.Provider;

public class AdvancedAuth extends JellyPlugin {

    private Configuration databaseConfig;
    private Configuration mainConfig;

    private CacheEngine cache;
    private Cipher cipher;

    private void setupDatabase() {
        // Connect to database
        final String driver = this.databaseConfig.getString("storage.driver", "mongodb");
        final String username = this.databaseConfig.getString("storage.username", "");
        final String password = this.databaseConfig.getString("storage.password", "");
        final String host = this.databaseConfig.getString("storage.host", "localhost");
        final int port = this.databaseConfig.getInt("storage.port", 27017);
        final String database = this.databaseConfig.getString("storage.database", "minecraft");
        final String collection = this.databaseConfig.getString("storage.collection", "players");

        final URI uri = new URI().setProtocol(driver).setUsername(username).setPassword(password).setHost(host)
                .setPort(port).setPath(database);
        final Provider provider = MilkshakeORM.connect(uri.toString());

        // Register repository
        MilkshakeORM.addRepository(AuthPlayerData.class, provider, collection);
    }

    private void setupCacheEngine() {
        final String driver = this.databaseConfig.getString("cache.driver", "memory");
        final String host = this.databaseConfig.getString("cache.host", "localhost");
        final int port = this.databaseConfig.getInt("cache.port", 6379);
        final String password = this.databaseConfig.getString("cache.password", "");
        final int expiration = this.databaseConfig.getInt("cache.expiration", 1440);

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

        // Load configuration
        this.databaseConfig = this.getConfig("database.yml");
        this.mainConfig = this.getConfig("config.yml");

        // Extract any language file from jar
        try {
            this.getLanguageManager().loadLanguages();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Setup database
        this.setupDatabase();

        // Setup cache engine
        this.setupCacheEngine();

        // Initialize cipher
        this.setupCipher();

        // Register player manager
        this.setPluginPlayerManager(new AuthPlayerManager(this));

        // Register tasks
        final long interval = this.getConfig().getInt("authentication.message-interval", 2) * 20L;

        this.getServer().getScheduler().runTaskTimer(this, new PlayerAuthNotifyTask(this), interval, interval);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new PlayerDataFetchTask(this), 20L, 20L);
        this.getServer().getScheduler().runTaskTimer(this, new PlayerTimeoutTask(this), 20L, 20L);

        // Register events
        BlockerListenerHandler.register(this);
        this.addEventListener(new PlayerJoinListener(this));

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
        final String cacheDriver = this.databaseConfig.getString("cache.driver");
        final String storageDriver = this.databaseConfig.getString("storage.driver");
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

    /* Static instance */
    private static AdvancedAuth instance;

    public static AdvancedAuth getInstance() {
        return instance;
    }
}
