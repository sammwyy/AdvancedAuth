package dev._2lstudios.advancedauth.hooks.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.entity.Player;

import dev._2lstudios.advancedauth.AdvancedAuth;
import dev._2lstudios.advancedauth.hooks.ProxyHook;

public class ProxyBungeecordHook implements ProxyHook {

    private final AdvancedAuth plugin;

    public ProxyBungeecordHook (final AdvancedAuth plugin) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    @Override
    public void sendServer(final Player player, String server) throws Exception {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(byteOut);
        
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(this.plugin, "BungeeCord", byteOut.toByteArray());

        byteOut.close();
        out.close();

        System.out.print("XD");
    }
}
