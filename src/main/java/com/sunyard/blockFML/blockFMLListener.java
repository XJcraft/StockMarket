package com.sunyard.blockFML;

import com.sunyard.trade.stockMarket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by Weiyuan on 2016/1/19.
 */
public class blockFMLListener implements Listener {
    Plugin plugin;

    public blockFMLListener(stockMarket stockMarket) {
        plugin = stockMarket;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockFML(PlayerLoginEvent event) {
        if (event.getHostname().contains("FML")) {
            plugin.getLogger().info(String.format("%s tried to connect with modded client", event.getPlayer().getName()));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(plugin.getConfig().getString("message.blockFML"));
        }
    }
}
