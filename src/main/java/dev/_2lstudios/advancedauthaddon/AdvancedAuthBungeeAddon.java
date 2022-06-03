package dev._2lstudios.advancedauthaddon;

import java.util.ArrayList;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class AdvancedAuthBungeeAddon extends Plugin implements Listener {
    private List<String> logged;

    @Override
    public void onEnable() {
        this.logged = new ArrayList<>();
        this.getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (!e.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }
        
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        String subChannel = in.readUTF();

        if (subChannel.equalsIgnoreCase( "AdvancedAuth")) {
            if (e.getSender() instanceof Server && e.getReceiver() instanceof ProxiedPlayer) {
                ProxiedPlayer receiver = (ProxiedPlayer) e.getReceiver();
                String name = receiver.getName();

                if (!this.logged.contains(name)) {
                    this.logged.add(name);
                }
            }
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent e) {
        if (e.getRequest().getReason() != Reason.JOIN_PROXY) {
            String name = e.getPlayer().getName();
            if (!this.logged.contains(name)) {
                e.setCancelled(true);
                this.getLogger().severe("Possible exploit detected, " + name + " tried to switch server without being logged.");
            }
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        String name = e.getPlayer().getName();
        this.logged.remove(name);
    }
}
