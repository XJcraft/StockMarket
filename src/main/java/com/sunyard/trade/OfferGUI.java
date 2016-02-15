package com.sunyard.trade;

import com.sunyard.database.Trade;
import com.sunyard.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiyuan on 2016/2/1.
 */
public class OfferGUI {
    public static void OfferGUI(Plugin plugin, Player player) {
        List<Trade> list = plugin.getDatabase().find(Trade.class).where().ieq("player", player.getName()).orderBy().asc("id").findList();
        Inventory menu = Bukkit.createInventory(null, 54, plugin.getConfig().getString("shop.offerName"));
        ItemStack[] itemStacks = menu.getContents();
        int slot = 0;
        boolean isFull = false;
        for (Trade trade : list) {
            Material material = Material.getMaterial(trade.getMaterial());
            itemStacks[slot] = new ItemStack(material, 1);
            ItemMeta itemMeta = itemStacks[slot].getItemMeta();
            itemMeta.setDisplayName(String.format("Flow number:" + trade.getId()));
            List<String> stringList = new ArrayList<String>();
            if (trade.isSell()) {
                stringList.add(String.format("Sell %s:", material.name()));
                stringList.add(String.format("%d %s", trade.getTradeNumber(), trade.getMaterial()));
            } else {
                stringList.add(String.format("Buy: %s", material.name()));
                stringList.add(String.format("%d %s", trade.getTradeNumber(), ItemUtil.getCurrency()));
            }
            stringList.add(String.format("Price: %d:%d", trade.getItemPrice(), trade.getMoneyPrice()));
//            stringList.add(String.format("Number: %d", trade.getTradeNumber()));
            stringList.add(String.format("Click to CANCEL!"));

            itemMeta.setLore(stringList);
            itemStacks[slot].setItemMeta(itemMeta);
            slot++;
            if (slot >= 54) {
                break;
            }
        }

        menu.setContents(itemStacks);
        player.openInventory(menu);


    }
}
