package dev._2lstudios.advancedauth;

import dev._2lstudios.advancedauth.commands.LoginCommand;
import dev._2lstudios.advancedauth.commands.RegisterCommand;
import dev._2lstudios.advancedauth.player.AuthPlayerData;
import dev._2lstudios.advancedauth.player.AuthPlayerManager;
import dev._2lstudios.advancedauth.tasks.PlayerDataFetchTask;
import dev._2lstudios.advancedauth.tasks.PlayerTimeoutTask;
import dev._2lstudios.advancedauth.tasks.PlayerAuthNotifyTask;
import dev._2lstudios.advancedauth.utils.URI;

import dev._2lstudios.jelly.JellyPlugin;
import dev._2lstudios.jelly.config.Configuration;

import dev._2lstudios.mineorm.DatabaseType;
import dev._2lstudios.mineorm.MineORM;
import dev._2lstudios.mineorm.providers.IProvider;

public class AdvancedAuth extends JellyPlugin {

    private Configuration databaseConfig;

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
        final IProvider provider = MineORM.connect(DatabaseType.valueOf(driver.toUpperCase()), uri.toString());

        // Register repository
        MineORM.addRepository(AuthPlayerData.class, provider, collection);
    }

    private void onInitialize() throws Exception {
        // Register player manager
        this.setPluginPlayerManager(new AuthPlayerManager(this));

        // Register tasks
        this.getServer().getScheduler().runTaskTimer(this, new PlayerAuthNotifyTask(this), 20L, 20L);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new PlayerDataFetchTask(this), 20L, 20L);
        this.getServer().getScheduler().runTaskTimer(this, new PlayerTimeoutTask(this), 20L, 20L);

        // Register commands
        this.addCommand(new LoginCommand());
        this.addCommand(new RegisterCommand());
    }

    @Override
    public void onEnable() {
        // Load configuration
        this.databaseConfig = this.getConfig("database.yml");

        // Setup database
        this.setupDatabase();

        // Safe plugin initialize
        try {
            this.onInitialize();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
