package org.xjcraft.trade;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.annotation.RCommand;
import org.xjcraft.api.CommonCommandExecutor;
import org.xjcraft.trade.gui.Shop;

public class StockMarketCommands implements CommonCommandExecutor {
    private StockMarketManager manager;

    public StockMarketCommands(StockMarketManager manager) {
        this.manager = manager;
    }

    @RCommand("test")
    public void test(Player player) {
        Shop shop = new Shop(player, "", new ItemStack(Material.LEATHER, 1));
        player.openInventory(shop.getInventory());
    }
}
