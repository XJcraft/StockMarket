package org.xjcraft.trade;

import com.avaje.ebean.Ebean;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.xjcraft.database.CustomItem;
import org.xjcraft.database.Trade;
import org.xjcraft.util.ItemUtil;
import org.xjcraft.util.SerializeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiyuan on 2016/2/1.
 */
public class OfferGUI {
    public static void OfferGUI(Plugin plugin, Player player) {
        List<Trade> list = Ebean.getServer("database").find(Trade.class).where().ieq("player", player.getName()).orderBy().asc("id").findList();
        Inventory menu = Bukkit.createInventory(null, 54, plugin.getConfig().getString("shop.offerName"));
        ItemStack[] itemStacks = menu.getContents();
        int slot = 0;
        boolean isFull = false;
        for (Trade trade : list) {
            String[] names = trade.getMaterial().split(":");
            Material material;
//            plugin.getLogger().info("special");
            if (names[0].equalsIgnoreCase("S")) {
                CustomItem customItem = Ebean.getServer("database").find(CustomItem.class).where().ieq("name", names[1]).findUnique();
                material = SerializeUtil.deSerialization(customItem.getFlatItem()).getType();
//                plugin.getLogger().info(material.name());
            } else {
//                plugin.getLogger().info(trade.getMaterial());
                material = Material.getMaterial(names[0]);
            }

//            plugin.getLogger().info(material.name());
            itemStacks[slot] = new ItemStack(material, 1, trade.getDurability());
            ItemMeta itemMeta = itemStacks[slot].getItemMeta();
            itemMeta.setDisplayName(String.format(plugin.getConfig().getString("message.lore.flow") + ":" + trade.getId()));
            List<String> stringList = new ArrayList<String>();
            if (trade.isSell()) {
                stringList.add(String.format("%s %s:", plugin.getConfig().getString("message.lore.sell"), material.name()));
                stringList.add(String.format("%d %s", trade.getTradeNumber(), trade.getMaterial()));
            } else {
                stringList.add(String.format("%s %s", plugin.getConfig().getString("message.lore.sell"), material.name()));
                stringList.add(String.format("%d %s", trade.getTradeNumber(), ItemUtil.getCurrency()));
            }
            stringList.add(String.format("%s %d:%d", plugin.getConfig().getString("message.lore.price"), trade.getItemPrice(), trade.getMoneyPrice()));
//            stringList.add(String.format("Number: %d", trade.getTradeNumber()));
            stringList.add(String.format(plugin.getConfig().getString("message.lore.cancel")));

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
