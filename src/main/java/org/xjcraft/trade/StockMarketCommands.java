package org.xjcraft.trade;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.annotation.RCommand;
import org.xjcraft.api.CommonCommandExecutor;
import org.xjcraft.trade.gui.Shop;

public class StockMarketCommands implements CommonCommandExecutor {
    private StockMarket plugin;
    private StockMarketManager manager;

    public StockMarketCommands(StockMarket stockMarket, StockMarketManager manager) {
        plugin = stockMarket;
        this.manager = manager;
    }

    @RCommand("test")
    public void test(Player player) {
        Shop shop = new Shop(plugin, player, "", new ItemStack(Material.LEATHER, 1));
        player.openInventory(shop.getInventory());
    }

    @RCommand("label")
    public void label(Player player) {
        ItemStack itemInHand = player.getItemInHand();
        player.sendMessage(manager.getLabel(itemInHand));
    }
}
