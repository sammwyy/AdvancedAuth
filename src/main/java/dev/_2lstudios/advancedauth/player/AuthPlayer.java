package dev._2lstudios.advancedauth.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.hooks.ProxyHook;
import dev._2lstudios.advancedauth.utils.PlaceholderUtils;
import dev._2lstudios.jelly.JellyPlugin;
import dev._2lstudios.jelly.player.PluginPlayer;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.repository.Repository;
import com.dotphin.milkshakeorm.utils.MapFactory;

public class AuthPlayer extends PluginPlayer {

    private final AdvancedAuth plugin;

    private AuthPlayerData data = null;
    private boolean fetched = false;
    private boolean logged = false;
    private int timer = 0;

    public AuthPlayer(final JellyPlugin plugin, final Player player) {
        super(plugin, player);
        this.plugin = (AdvancedAuth) plugin;
    }

    public String getName() {
        return this.getBukkitPlayer().getName();
    }

    public void addTimer() {
        this.timer++;
    }

    public boolean comparePassword(final String candidate) {
        if (!this.isRegistered()) {
            return false;
        }

        return this.plugin.getCipher().compare(this.data.password, candidate);
    }

    public void createSession() {
        if (this.data != null && this.data._id != null) {
            this.plugin.getCache().set("auth_session_" + this.data._id, this.getAddress());
        }
    }

    public boolean hasActiveSession() {
        if (this.data == null || this.data._id == null) {
            return false;
        }

        final String sessionAddr = this.plugin.getCache().get("auth_session_" + this.data._id);
        if (sessionAddr == null) {
            return false;
        } else {
            return sessionAddr.equals(this.getAddress());
        }
    }

    public void deleteSession() {
        if (this.data._id != null) {
            this.plugin.getCache().delete("auth_session_" + this.data._id);
        }
    }

    public void fetchUserData() {
        final Repository<AuthPlayerData> repo = MilkshakeORM.getRepository(AuthPlayerData.class);

        // Find by username
        final String username = this.getBukkitPlayer().getName().toLowerCase();
        final MapFactory usernameFilter = MapFactory.create("username", username);
        final AuthPlayerData byUsernameData = (AuthPlayerData) repo.findOne(usernameFilter);

        if (byUsernameData != null) {
            this.data = byUsernameData;
        } else {
            // Find by UUID
            final String uuid = this.getBukkitPlayer().getUniqueId().toString();
            final MapFactory uuidFilter = MapFactory.create("uuid", uuid);
            final AuthPlayerData byUUIDData = (AuthPlayerData) repo.findOne(uuidFilter);
            this.data = byUUIDData;
        }

        this.fetched = true;

        // Check for username spoof.
        if (this.data != null) {
            // Check if is the same name.
            if (this.getBukkitPlayer().getName().equalsIgnoreCase(this.data.displayName)) {
                // But with different case.
                if (!this.getBukkitPlayer().getName().equals(this.data.displayName)) {
                    // And kick if isn't the same as previous saved.
                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        this.getBukkitPlayer().kickPlayer(
                            ChatColor.translateAlternateColorCodes('&', 
                            this.getI18nString("common.username-case-mismatch")
                                .replace("{current_name}", this.getBukkitPlayer().getName())
                                .replace("{registered_name}", this.data.displayName))
                        );
                    });
                }
            }
        }

        // Check for session
        boolean resumeSession = this.plugin.getMainConfig().getBoolean("authentication.resume-session", false);
        if (resumeSession && this.hasActiveSession()) {
            this.login(LoginReason.SESSION_RESUME);
        }
    }

    public String getAddress() {
        return this.getBukkitPlayer().getAddress().getAddress().toString();
    }

    public AuthPlayerData[] getAlts() {
        final Repository<AuthPlayerData> repo = MilkshakeORM.getRepository(AuthPlayerData.class);
        final MapFactory filter = MapFactory.create("lastLoginIP", this.getAddress());
        return repo.findMany(filter);
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

    public void login(final LoginReason reason) {
        if (this.isRegistered()) {
            this.logged = true;
            this.data.displayName = this.getName();
            this.data.username = this.getName().toLowerCase();
            this.data.uuid = this.getBukkitPlayer().getUniqueId().toString();
            this.data.lastLoginIP = this.getAddress();
            this.data.save();

            boolean resumeSession = this.plugin.getMainConfig().getBoolean("authentication.resume-session", false);

            if (reason == LoginReason.PASSWORD && resumeSession) {
                this.createSession();
            }

            switch (reason) {
            case PASSWORD:
                this.sendI18nMessage("login.successfully");
                break;
            case SESSION_RESUME:
                this.sendI18nMessage("login.session-resumed");
                break;
            }

            final ProxyHook proxy = this.plugin.getProxyHook();
            if (proxy != null) {
                final String server = this.plugin.getMainConfig().getString("authentication.send-server-on-login.server");
                try {
                    proxy.sendServer(this.getBukkitPlayer(), server);
                } catch (Exception e) {
                    this.sendMessage(this.getI18nString("common.error-sending-server").replace("{server}", server));
                }
            }
        }
    }

    public void logout() {
        if (this.isLogged()) {
            this.logged = false;
            this.timer = 0;

            boolean resumeSession = this.plugin.getMainConfig().getBoolean("authentication.resume-session", false);
            if (resumeSession) {
                this.deleteSession();
            }
        }
    }

    public boolean register(final String password) {
        if (this.isRegistered()) {
            return false;
        }

        this.data = new AuthPlayerData();
        this.data.displayName = this.getName();
        this.data.username = this.getName().toLowerCase();
        this.data.uuid = this.getBukkitPlayer().getUniqueId().toString();
        this.data.password = this.plugin.getCipher().hash(password);
        this.data.lastLoginIP = this.getAddress();
        this.data.registrationIP = this.getAddress();

        this.data.save();
        this.timer = 0;
        this.logged = true;
        return true;
    }

    @Override
    public void sendMessage(final String message) {
        final String prefix = this.plugin.getMainConfig().getString("settings.prefix", "&8[&6&lA&e&lA&8] &r");
        super.sendMessage(PlaceholderUtils.format(prefix + message, this));
    }

    public void setEmail(final String email) {
        this.data.email = email;
        this.data.save();
    }

    public void setPassword(final String password) {
        this.data.password = this.plugin.getCipher().hash(password);
        this.data.save();
    }

    public void unregister() {
        this.logout();
        this.data.delete();
        this.data = null;
    }
}
