package dev._2lstudios.advancedauth.services;

import java.util.List;

import com.sammwy.milkshake.Repository;
import com.sammwy.milkshake.find.FindFilter;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.players.AuthPlayerData;

public class AuthService {
    private AdvancedAuth plugin;
    private Repository<AuthPlayerData> repository;

    public AuthService(AdvancedAuth plugin) {
        this.plugin = plugin;
        this.repository = plugin.getPlayerDataRepository();
    }

    public AuthPlayerData getByUsername(String username) {
        return repository.findOne(new FindFilter("username", username.toLowerCase()));
    }

    public List<AuthPlayerData> getAltsForAddress(String address) {
        return repository.findMany(new FindFilter("lastLoginIP", address));
    }

    public List<AuthPlayerData> getAltsForPlayer(String username) {
        AuthPlayerData data = this.getByUsername(username);
        if (data == null) {
            return null;
        } else {
            return this.getAltsForAddress(data.lastLoginIP);
        }
    }

    public boolean changePassword(String username, String newPassword) {
        AuthPlayerData player = this.getByUsername(username);
        if (player == null) {
            return false;
        }

        String hash = this.plugin.getCipher().hash(newPassword);
        player.password = hash;
        player.save();
        return true;
    }

    public boolean unregister(String username) {
        return repository.deleteOne(new FindFilter("username", username.toLowerCase()));
    }
}
