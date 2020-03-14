package org.xjcraft.trade;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.annotation.RCommand;
import org.xjcraft.api.CommonCommandExecutor;
import org.xjcraft.trade.gui.Bag;
import org.xjcraft.trade.gui.Counter;
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
        Shop shop = new Shop(plugin, player, "GOV", new ItemStack(Material.LEATHER, 1), null);
        player.openInventory(shop.getInventory());
    }

    @RCommand(value = "label", sender = RCommand.Sender.PLAYER, desc = "获取手持物品的标签")
    public void label(CommandSender player) {
        ItemStack itemInHand = ((Player) player).getItemInHand();
        player.sendMessage(itemInHand.getType().name() + "-" + manager.getSubType(itemInHand));
    }

    @RCommand(value = "get", sender = RCommand.Sender.PLAYER, desc = "获取指定类型物品")
    public void get(CommandSender player, String type) {
        get(player, type, null);
    }

    @RCommand(value = "get", sender = RCommand.Sender.PLAYER, desc = "获取指定类型物品")
    public void get(CommandSender player, String type, String subtype) {
        if (player instanceof Player) {
            ItemStack itemStack = manager.getItemStack(type, subtype);
            ((Player) player).getInventory().addItem(itemStack);
        }
    }

    @RCommand(value = "on", sender = RCommand.Sender.PLAYER, desc = "打开仓库面板")
    public void on(CommandSender player) {
        if (player instanceof Player) {
            ((Player) player).openInventory(new Bag(plugin, (Player) player).getInventory());
        }
    }

    @RCommand(value = "mine", sender = RCommand.Sender.PLAYER, desc = "打开柜台面板")
    public void mine(CommandSender player) {
        if (player instanceof Player) {
            ((Player) player).openInventory(new Counter(plugin, (Player) player).getInventory());
        }
    }

}
