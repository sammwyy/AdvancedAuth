package dev._2lstudios.advancedauth.security;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class Faillock {
    private AdvancedAuth plugin;
    
    public Faillock(AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    public int getTries(String address) {
        if (!this.plugin.getConfig().getBoolean("security.fail-lock.enabled")) {
            return 0;
        }

        String rawTries = this.plugin.getCache().get("faillock_try_" + address);
        int tries = 0;

        if (rawTries != null && !rawTries.equals("")) {
            tries = Integer.parseInt(rawTries);
        }

        return tries;
    }

    public void handleFail(String address) {
        if (!this.plugin.getConfig().getBoolean("security.fail-lock.enabled")) {
            return;
        }

        int tries = this.getTries(address);
        tries++;
        this.plugin.getCache().set("faillock_try_" + address, "" + tries);
    }

    public boolean isAddressLocked(String address) {
        if (!this.plugin.getConfig().getBoolean("security.fail-lock.enabled")) {
            return false;
        }

        int tries = this.getTries(address);
        int maxTries = this.plugin.getConfig().getInt("security.fail-lock.tries");

        if (tries >= maxTries) {
            return true;
        } else {
            return false;
        }
    }
}
