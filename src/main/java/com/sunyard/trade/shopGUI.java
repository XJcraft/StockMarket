package com.sunyard.trade;

import com.sunyard.database.Trade;
import com.sunyard.util.ItemUtil;
import com.sunyard.util.SqlUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 * Created by Weiyuan on 2016/1/12.
 */
public class ShopGUI {
    public static void shopGUI(Plugin plugin, Player player, Material shopType, int moneyPrice, int itemPrice,
                               int sellNumber, int buyNumber, boolean itemSize, boolean moneySize) {
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
            itemStacks[12 - i] = ItemUtil.getNumberStack(itemPrice / (int) Math.pow(10, (double) i) % 10);
            itemStacks[17 - i] = ItemUtil.getNumberStack(moneyPrice / (int) Math.pow(10, (double) i) % 10);
            if (i != 3) {
                itemStacks[38 - i] = ItemUtil.getNumberStack(sellNumber / (int) Math.pow(10, (double) i) % 10);
                itemStacks[44 - i] = ItemUtil.getNumberStack(buyNumber / (int) Math.pow(10, (double) i) % 10);
            }
        }

        int[] upArrow = {0, 1, 2, 3, 5, 6, 7, 8, 27, 28, 29, 33, 34, 35};
        for (int i : upArrow
                ) {
            itemStacks[i] = ItemUtil.getUpArrow();
        }

        int[] downArrow = {18, 19, 20, 21, 23, 24, 25, 26, 45, 46, 47, 51, 52, 53};
        for (int i : downArrow) {
            itemStacks[i] = ItemUtil.getDownArrow();
        }

        if (itemSize) {
            itemStacks[39] = new ItemStack(shopType, shopType.getMaxStackSize());
        } else {
            itemStacks[39] = new ItemStack(shopType, 1);
        }

        if (moneySize) {
            itemStacks[41] = new ItemStack(ItemUtil.getCurrency(), ItemUtil.getCurrency().getMaxStackSize());
        } else {
            itemStacks[41] = new ItemStack(ItemUtil.getCurrency(), 1);
        }

        itemStacks[13] = ItemUtil.sell(shopType, String.format(plugin.getConfig().getString("message.priceButton"), itemPrice, shopType.name(), moneyPrice));
        itemStacks[40] = ItemUtil.getDetail(shopType.name(), moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);

        if (itemSize) {
            sellNumber = sellNumber * shopType.getMaxStackSize();
        }
        if (moneySize) {
            buyNumber = buyNumber * ItemUtil.getCurrency().getMaxStackSize();
        }

        //买卖键
        itemStacks[48] = ItemUtil.sell(shopType,
                String.format(plugin.getConfig().getString("message.sellButton"), sellNumber, shopType.name(), sellNumber * moneyPrice / itemPrice));
        itemStacks[50] = ItemUtil.buy(
                String.format(plugin.getConfig().getString("message.buyButton"), shopType.name(), moneyPrice * buyNumber, itemPrice, moneyPrice));

        //库存显示
        itemStacks[30] = ItemUtil.sell(shopType,
                String.format(plugin.getConfig().getString("message.itemOwned"), ItemUtil.getItemNumber(player, shopType), shopType.name()));
        itemStacks[32] = ItemUtil.buy(
                String.format(plugin.getConfig().getString("message.moneyOwned"), ItemUtil.getItemNumber(player, ItemUtil.getCurrency())));

        //最低卖价
        itemStacks[4] = getLowest(plugin, shopType);

        //最高售价
        itemStacks[22] = getHighest(plugin, shopType);

        menu.setContents(itemStacks);
        player.openInventory(menu);
    }

    private static ItemStack getHighest(Plugin plugin, Material shopType) {
        ItemStack itemStack = ItemUtil.getHighest();
        ItemMeta itemMetaH = itemStack.getItemMeta();
        Trade tradeH = SqlUtil.getFirst(
                plugin.getDatabase().find(Trade.class).where().ieq("material", shopType.name()).ieq("sell", "0").orderBy().desc("price").findList());
        if (tradeH != null) {
            itemMetaH.setDisplayName(String.format("Highest buy price: %d:%d by %s", tradeH.getItemPrice(), tradeH.getMoneyPrice(), tradeH.getPlayer()));
        } else {
            itemMetaH.setDisplayName("Nobody buying");
        }
        itemStack.setItemMeta(itemMetaH);
        return itemStack;
    }

    private static ItemStack getLowest(Plugin plugin, Material shopType) {
        ItemStack itemStack = ItemUtil.getLowest();
        ItemMeta itemMeta = itemStack.getItemMeta();
        Trade trade = SqlUtil.getFirst(
                plugin.getDatabase().find(Trade.class).where().ieq("material", shopType.name()).ieq("sell", "1").orderBy().asc("price").findList());
        if (trade != null) {
            itemMeta.setDisplayName(String.format("Lowest sell price: %d:%d by %s", trade.getItemPrice(), trade.getMoneyPrice(), trade.getPlayer()));
        } else {
            itemMeta.setDisplayName("Nobody selling");
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
