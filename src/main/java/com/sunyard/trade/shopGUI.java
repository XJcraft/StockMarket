package com.sunyard.trade;

import com.sunyard.util.itemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Created by Weiyuan on 2016/1/12.
 */
public class shopGUI {
    public static void shopGUI(Plugin plugin, Player player, Material shopType, int moneyPrice, int itemPrice, int sellNumber, int buyNumber, boolean itemSize, boolean moneySize) {
        itemPrice = itemPrice % 10000;
        moneyPrice = moneyPrice % 10000;
        if (itemPrice == 0) {
            itemPrice = 1;
        }
        if (moneyPrice == 0) {
            moneyPrice = 1;
        }

        sellNumber = sellNumber % 1000;
        buyNumber = buyNumber % 1000;
        if (sellNumber == 0) {
            sellNumber = 1;
        }
        if (buyNumber == 0) {
            buyNumber = 1;
        }
        Inventory menu = Bukkit.createInventory(null, 54, "[Stock Market]");
        ItemStack[] itemStacks = menu.getContents();

        for (int i = 0; i < 4; i++) {
            itemStacks[12 - i] = itemUtil.getNumberStack(itemPrice / (int) Math.pow(10, (double) i) % 10);
            itemStacks[17 - i] = itemUtil.getNumberStack(moneyPrice / (int) Math.pow(10, (double) i) % 10);
            if (i != 3) {
                itemStacks[38 - i] = itemUtil.getNumberStack(sellNumber / (int) Math.pow(10, (double) i) % 10);
                itemStacks[44 - i] = itemUtil.getNumberStack(buyNumber / (int) Math.pow(10, (double) i) % 10);
            }
        }

        int[] upArrow = {0, 1, 2, 3, 5, 6, 7, 8, 27, 28, 29, 33, 34, 35};
        for (int i : upArrow
                ) {
            itemStacks[i] = itemUtil.getUpArrow();
        }

        int[] downArrow = {18, 19, 20, 21, 23, 24, 25, 26, 45, 46, 47, 51, 52, 53};
        for (int i : downArrow) {
            itemStacks[i] = itemUtil.getDownArrow();
        }

        if (itemSize) {
            itemStacks[39] = new ItemStack(shopType, shopType.getMaxStackSize());
        } else {
            itemStacks[39] = new ItemStack(shopType, 1);
        }

        if (moneySize) {
            itemStacks[41] = new ItemStack(itemUtil.getCurrency(), itemUtil.getCurrency().getMaxStackSize());
        } else {
            itemStacks[41] = new ItemStack(itemUtil.getCurrency(), 1);
        }

        itemStacks[13] = itemUtil.sell(shopType, String.format(plugin.getConfig().getString("message.priceButton"), itemPrice, shopType.name(), moneyPrice));
        itemStacks[40] = itemUtil.getDetail(shopType.name(), moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);

        if (itemSize) {
            sellNumber = sellNumber * shopType.getMaxStackSize();
        }
        if (moneySize) {
            buyNumber = buyNumber * itemUtil.getCurrency().getMaxStackSize();
        }

        //买卖键
        itemStacks[48] = itemUtil.sell(shopType, String.format(plugin.getConfig().getString("message.sellButton"), sellNumber, shopType.name(), sellNumber * moneyPrice / itemPrice));
        itemStacks[50] = itemUtil.buy(String.format(plugin.getConfig().getString("message.buyButton"), shopType.name(), moneyPrice * buyNumber, itemPrice, moneyPrice));

        //库存显示
        itemStacks[30] = itemUtil.sell(shopType, String.format(plugin.getConfig().getString("message.itemOwned"), itemUtil.getItemNumber(player, shopType), shopType.name()));
        itemStacks[32] = itemUtil.buy(String.format(plugin.getConfig().getString("message.moneyOwned"), itemUtil.getItemNumber(player, itemUtil.getCurrency())));

        menu.setContents(itemStacks);
        player.openInventory(menu);
    }
}
