package dev._2lstudios.advancedauth.player;

import org.bukkit.entity.Player;

import dev._2lstudios.jelly.JellyPlugin;
import dev._2lstudios.jelly.player.PluginPlayer;
import dev._2lstudios.mineorm.MineORM;
import dev._2lstudios.mineorm.repository.Repository;
import dev._2lstudios.mineorm.utils.MapFactory;

public class AuthPlayer extends PluginPlayer {
    private AuthPlayerData data = null;

    private boolean fetched = false;
    private boolean logged = false;
    private int timer = 0;

    public AuthPlayer(final JellyPlugin plugin, final Player player) {
        super(plugin, player);
    }

    public void addTimer() {
        this.timer++;
    }

    public boolean comparePassword(final String candidate) {
        if (!this.isRegistered()) {
            return false;
        }

        return candidate.equals(this.data.password);
    }

    public void fetchUserData() {
        final Repository<AuthPlayerData> repo = MineORM.getRepository(AuthPlayerData.class);

        // Find by username
        final String username = this.getBukkitPlayer().getName();
        final MapFactory usernameFilter = MapFactory.create("username", username);
        final AuthPlayerData byUsernameData = (AuthPlayerData) repo.findOne(usernameFilter);

        if (byUsernameData != null) {
            this.data = byUsernameData;
            this.fetched = true;
            return;
        }

        // Find by UUID
        final String uuid = this.getBukkitPlayer().getUniqueId().toString();
        final MapFactory uuidFilter = MapFactory.create("uuid", uuid);
        final AuthPlayerData byUUIDData = (AuthPlayerData) repo.findOne(uuidFilter);

        this.data = byUUIDData;
        this.fetched = true;
    }

    public int getTimer() {
        return this.timer;
    }

    public boolean isFetched() {
        return this.fetched;
    }

    public boolean isLogged() {
        return this.logged;
    }

    public boolean isRegistered() {
        return this.data != null;
    }

    public void login() {
        if (this.isRegistered()) {
            this.logged = true;
            this.data.username = this.getBukkitPlayer().getName();
            this.data.uuid = this.getBukkitPlayer().getUniqueId().toString();
            this.data.save();
        }
    }

    public boolean register(final String password, final String email) {
        if (this.isRegistered()) {
            return false;
        }

        this.data = new AuthPlayerData();
        this.data.username = this.getBukkitPlayer().getName();
        this.data.uuid = this.getBukkitPlayer().getUniqueId().toString();
        this.data.password = password;
        this.data.email = email;

        this.data.save();
        this.logged = true;
        return true;
    }

    public boolean register(final String password) {
        return this.register(password, null);
    }
}