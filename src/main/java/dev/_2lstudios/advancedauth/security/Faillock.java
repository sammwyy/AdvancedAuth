package dev._2lstudios.advancedauth.security;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class Faillock {
    private final AdvancedAuth plugin;
    
    public Faillock(final AdvancedAuth plugin) {
        this.plugin = plugin;
    }

    public int getTries(final String address) {
        if (!this.plugin.getMainConfig().getBoolean("security.fail-lock.enabled")) {
            return 0;
        }

        String rawTries = this.plugin.getCache().get("faillock_try_" + address);
        int tries = 0;

        if (rawTries != null && !rawTries.equals("")) {
            tries = Integer.parseInt(rawTries);
        }

        return tries;
    }

    public void handleFail(final String address) {
        if (!this.plugin.getMainConfig().getBoolean("security.fail-lock.enabled")) {
            return;
        }

        int tries = this.getTries(address);
        tries++;
        this.plugin.getCache().set("faillock_try_" + address, "" + tries);
    }

    public boolean isAddressLocked(final String address) {
        if (!this.plugin.getMainConfig().getBoolean("security.fail-lock.enabled")) {
            return false;
        }

        int tries = this.getTries(address);
        int maxTries = this.plugin.getMainConfig().getInt("security.fail-lock.tries");

        if (tries >= maxTries) {
            return true;
        } else {
            return false;
        }
    }
}
