package org.xjcraft.trade;

import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.BannerMeta;
import org.xjcraft.trade.gui.StockMarketGui;

public class StockMarketListener implements Listener {
    private final StockMarket plugin;
    private final StockMarketManager manager;

    public StockMarketListener(StockMarket plugin, StockMarketManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof StockMarketGui) {
            event.setCancelled(true);
            ((StockMarketGui) event.getInventory().getHolder()).onClick((Player) event.getWhoClicked(), event.getSlot());
        }

        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() instanceof BannerMeta) {
            BannerMeta meta = (BannerMeta) event.getCurrentItem().getItemMeta();
            System.out.println(meta.getBaseColor());
            for (Pattern pattern : meta.getPatterns()) {
                System.out.println(pattern.getColor() + ":" + pattern.getPattern());
            }

        }
    }
}
