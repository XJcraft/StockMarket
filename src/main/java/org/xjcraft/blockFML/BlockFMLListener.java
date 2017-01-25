package org.xjcraft.blockFML;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.xjcraft.trade.StockMarket;

/**
 * Created by Weiyuan on 2016/1/19.
 */
public class BlockFMLListener implements Listener {
    private Plugin plugin;

    public BlockFMLListener(StockMarket stockMarket) {
        this.plugin = stockMarket;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockFML(PlayerLoginEvent event) {


		if (event.getHostname ().contains ("FML") && !event.getPlayer ().isOp ()) {
			this.plugin.getLogger().info(String.format("%s tried to connect with modded client", event.getPlayer().getName()));
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.plugin.getConfig().getString("message.blockFML"));
        }
    }
}
