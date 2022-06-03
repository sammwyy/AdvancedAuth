package dev._2lstudios.advancedauth.players;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.dotphin.milkshake.Repository;
import com.dotphin.milkshake.find.FindFilter;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.commands.CommandExecutor;
import dev._2lstudios.advancedauth.utils.ArrayUtils;
import dev._2lstudios.advancedauth.utils.PlaceholderUtils;
import dev._2lstudios.advancedauth.utils.PlayerUtils;
import dev._2lstudios.advancedauth.utils.ServerUtils;

public class AuthPlayer extends CommandExecutor {
    private Player bukkitPlayer;

    private AuthPlayerData data = null;
    private AuthState state = AuthState.UNFETCHED;
    private boolean hidden = false;
    private int timer = 0;

    public AuthPlayer(AdvancedAuth plugin, Player bukkitPlayer) {
        super(plugin, bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
    }

    public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    public String getLang(boolean returnDefault) {
        String lang = null;

        if (ServerUtils.hasPlayerGetLocaleAPI()) {
            lang = this.getBukkitPlayer().getLocale();
        } else {
            lang = PlayerUtils.getPlayerLocaleInLegacyWay(this.bukkitPlayer);
        }

        if (returnDefault && lang == null) {
            if (this.data.lang != null) {
                lang = this.data.lang;
            } else {
                lang = this.getPlugin().getLanguageManager().getDefaultLocale();
            }
        }

        return lang;
    }

    @Override
    public String getLang() {
        return this.getLang(true);
    }

    public String getName() {
        return this.getBukkitPlayer().getName();
    }


    public String getLowerName() {
        return this.getName().toLowerCase();
    }

    public UUID getUUID() {
        return this.bukkitPlayer.getUniqueId();
    }

    public String getUUIDAsStr() {
        return this.getUUID().toString();
    }

    public boolean isOnline() {
        return this.bukkitPlayer != null && this.bukkitPlayer.isOnline();
    }

    public AuthState getState() {
        return this.state;
    }

    public AuthPlayerData getData() {
        return this.data;
    }
    public void addTimer() {
        this.timer++;
    }

    public boolean comparePassword(String candidate) {
        if (!this.isRegistered()) {
            return false;
        }

        return this.getPlugin().getCipher().compare(this.data.password, candidate);
    }

    public void createSession() {
        if (this.data != null && this.data.getID() != null) {
            this.getPlugin().getCache().set("auth_session_" + this.data.getID(), this.getAddress());
        }
    }

    public boolean hasActiveSession() {
        if (this.data == null || this.data.getID() == null) {
            return false;
        }

        String sessionAddr = this.getPlugin().getCache().get("auth_session_" + this.data.getID());
        if (sessionAddr == null) {
            return false;
        } else {
            return sessionAddr.equals(this.getAddress());
        }
    }

    public void deleteSession() {
        if (this.data.getID() != null) {
            this.getPlugin().getCache().delete("auth_session_" + this.data.getID());
        }
    }

    public void fetchUserData() {
        Repository<AuthPlayerData> repo = this.getPlugin().getPlayerDataRepository();

        // Find user data
        FindFilter filter = new FindFilter("username", this.getLowerName()).or().isEquals("uuid",  this.getUUIDAsStr());
        this.data = repo.findOne(filter);

        // Check for guest playing.
        boolean isGuestEnabled = this.getPlugin().getConfig().getBoolean("authentication.is-register-optional");

        if (this.data == null && isGuestEnabled) {
            this.state = AuthState.GUEST;
            this.updateEffects();
        } else {
            this.state = AuthState.UNLOGGED;
        }

        // Check for username spoof.
        if (this.data != null) {
            // Check if is the same name.
            if (this.getBukkitPlayer().getName().equalsIgnoreCase(this.data.displayName)) {
                // But with different case.
                if (!this.getBukkitPlayer().getName().equals(this.data.displayName)) {
                    // And kick if isn't the same as previous saved.
                    this.getPlugin().getServer().getScheduler().runTask(this.getPlugin(), () -> {
                        this.getBukkitPlayer().kickPlayer(
                            ChatColor.translateAlternateColorCodes('&', 
                            this.getI18nMessage("common.username-case-mismatch")
                                .replace("{current_name}", this.getBukkitPlayer().getName())
                                .replace("{registered_name}", this.data.displayName))
                        );
                    });

                    return;
                }
            }
        }

        // Check for session
        if (this.hasActiveSession()) {
            this.login(LoginReason.SESSION_RESUME);
        }
    }

    public String getAddress() {
        return this.getBukkitPlayer().getAddress().getAddress().toString();
    }

    public List<AuthPlayerData> getAlts() {
        return this.getPlugin().getAuthService().getAltsForAddress(this.getAddress());
    }

    public boolean isLogged() {
        return this.state == AuthState.GUEST || this.state == AuthState.LOGGED;
    }

    public int getTimer() {
        return this.timer;
    }

    public boolean isFetched() {
        return this.state != AuthState.UNFETCHED;
    }

    public boolean isRegistered() {
        return this.data != null;
    }

    public boolean isGuest() {
        return this.state == AuthState.GUEST;
    }

    public void login(LoginReason reason) {
        if (this.isRegistered()) {
            this.timer = 0;
            this.state = AuthState.LOGGED;
            this.updateVisualTime(0);

            if (reason != LoginReason.REGISTERED) {
                this.data.displayName = this.getName();
                this.data.username = this.getName().toLowerCase();
                this.data.uuid = this.getBukkitPlayer().getUniqueId().toString();
                this.data.lastLoginIP = this.getAddress();
                this.data.lastLoginDate = System.currentTimeMillis();
                this.data.lang = this.getLang();
                this.data.save();
            }

            if (reason == LoginReason.PASSWORD && this.data.enabledSession) {
                this.createSession();
            }

            switch (reason) {
            case PASSWORD:
                this.sendI18nMessage("login.successfully");
                break;
            case SESSION_RESUME:
                this.sendI18nMessage("login.session-resumed");
                break;
            case FORCED:
                this.sendI18nMessage("login.forced");
                break;
            case REGISTERED:
                break;
            }

            if (this.getPlugin().getConfig().getBoolean("authentication.send-server-on-login.enabled")) {
                List<String> servers = this.getPlugin().getConfig().getStringList("authentication.send-server-on-login.servers");
                String server =  ArrayUtils.randomItem(servers);
                
                try {
                    this.sendToServer(server);
                } catch (Exception e) {
                    this.sendMessage(this.getI18nMessage("common.error-sending-server").replace("{server}", server));
                }
            }

            this.updateEffects();
        }
    }

    public void updateHideOrShowPlayer() {
        String blockMode = this.getPlugin().getConfig().getString("settings.actions.hide-players", "default");

        if (blockMode.equalsIgnoreCase("always")) {
            this.hide();
        } else if (blockMode.equalsIgnoreCase("never")) {
            this.show();
        } else {
            if (this.isLogged()) {
                this.show();
            } else {
                this.hide();
            }
        }
    }

    public void updateAllowOrDenyMovement () {
        String blockMode = this.getPlugin().getConfig().getString("settings.actions.deny-move", "default");
        
        if (blockMode.equalsIgnoreCase("always")) {
            this.getBukkitPlayer().setWalkSpeed(0.0f);
        } else if (blockMode.equalsIgnoreCase("never")) {
            this.getBukkitPlayer().setWalkSpeed(0.2f);
        } else {
            if (this.isLogged()) {
                this.getBukkitPlayer().setWalkSpeed(0.2f);
            } else {
                this.getBukkitPlayer().setWalkSpeed(0.0f);
            }
        }
    }

    public void updateEffects() {
        this.getPlugin().getServer().getScheduler().runTask(this.getPlugin(), () -> {
            this.updateHideOrShowPlayer();
            this.updateAllowOrDenyMovement();
        });
    }

    public void logout() {
        if (this.isLogged()) {
            this.state = AuthState.UNLOGGED;
            this.timer = 0;

            boolean resumeSession = this.getPlugin().getConfig().getBoolean("authentication.resume-session", false);
            if (resumeSession) {
                this.deleteSession();
            }
        }
    }

    public boolean register(String password) {
        if (this.isRegistered()) {
            return false;
        }

        this.data = new AuthPlayerData();
        this.data.displayName = this.getName();
        this.data.username = this.getName().toLowerCase();
        this.data.uuid = this.getBukkitPlayer().getUniqueId().toString();
        this.data.password = this.getPlugin().getCipher().hash(password);
        this.data.lastLoginIP = this.getAddress();
        this.data.lastLoginDate = System.currentTimeMillis();
        this.data.registrationIP = this.getAddress();
        this.data.registrationDate = System.currentTimeMillis();
        this.data.enabledSession = this.getPlugin().getConfig().getBoolean("authentication.resume-session-by-default");
        this.data.lang = this.getLang();

        this.data.save();
        this.login(LoginReason.REGISTERED);
        return true;
    }

    @Override
    public void sendMessage(String message) {
        String prefix = this.getPlugin().getConfig().getString("settings.prefix");
        super.sendMessage(PlaceholderUtils.format(prefix + message, this));
    }

    public void sendToServer(String server) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        out.writeUTF("Connect");
        out.writeUTF(server);
        this.getBukkitPlayer().sendPluginMessage(this.getPlugin(), "BungeeCord", b.toByteArray());
        b.close();
        out.close();
    }

    public void setEmail(String email) {
        this.data.email = email;
        this.data.save();
    }

    public void setPassword(String password) {
        this.data.password = this.getPlugin().getCipher().hash(password);
        this.data.save();
    }

    public void unregister() {
        this.logout();
        this.data.delete();
        this.data = null;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void show() {
        this.hidden = false;
        for (Player op : this.getPlugin().getServer().getOnlinePlayers()){
            this.showOther(op);
        }
    }

    @SuppressWarnings("deprecation")
    public void showOther(Player player) {
        player.showPlayer(this.getBukkitPlayer());
    }

    public void hide() {
        this.hidden = true;

        for (Player op : this.getPlugin().getServer().getOnlinePlayers()){
            this.hideOther(op);
        }
    }

    @SuppressWarnings("deprecation")
    public void hideOther(Player player) {
        player.hidePlayer(this.getBukkitPlayer());
    }

    public void kick(String message) {
        this.getPlugin().getServer().getScheduler().runTask(this.getPlugin(), () -> {
            this.getBukkitPlayer().kickPlayer(this.formatMessage(message));
        });
    }

    public void kickI18n(String key) {
        this.kick(this.getI18nMessage(key));
    }

    public void updateVisualTime(long timeLeft) {
        switch (this.getPlugin().getConfig().getString("authentication.visual-time-indicator")) {
            case "LEVEL":
                this.bukkitPlayer.setLevel((int) timeLeft);
                break;
        }
    }

    @SuppressWarnings("deprecation")
    public void sendTitle(String title, String subtitle) {
        this.bukkitPlayer.resetTitle();
        this.bukkitPlayer.sendTitle(this.formatMessage(title), this.formatMessage(subtitle));
    }

    public void sendI18nTitle(String title, String subtitle) {
        this.sendTitle(this.getI18nMessage(title), this.getI18nMessage(subtitle));
    }
}