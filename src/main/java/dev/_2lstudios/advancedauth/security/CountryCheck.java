package dev._2lstudios.advancedauth.security;

import java.net.InetSocketAddress;
import java.util.List;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.config.Configuration;
import dev._2lstudios.advancedauth.services.GeoIPService;

public class CountryCheck {
    private boolean enabled;
    private String mode;
    private List<String> list;

    public CountryCheck (final AdvancedAuth plugin) {
        Configuration config = plugin.getConfig();

        this.enabled = config.getBoolean("security.country-check.enabled", false);
        this.mode = config.getString("security.country-check.mode");
        this.list = config.getStringList("security.country-check.list");
    }

    public boolean canJoinAddress (final String address) {
        if (!this.enabled) {
            return true;
        }

        final String country = GeoIPService.getCountryCode(address);
        final boolean isInList = this.list.contains(country);

        if (this.mode.equalsIgnoreCase("blacklist") && isInList) {
            return false;
        } else if (this.mode.equalsIgnoreCase("whitelist") && !isInList) {
            return false;
        } else {
            return true;
        }
    }

    public boolean canJoinAddress(final InetSocketAddress address) {
        return this.canJoinAddress(address.toString());
    }
}
