package org.xjcraft.trade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.xjcraft.database.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiyuan on 2016/1/24.
 */
public class BagGUI {
    public static void BagGUI(Plugin plugin, Player player) {
        List<Storage> list = plugin.getDatabase().find(Storage.class).where().ieq("playername", player.getName()).orderBy().asc("id").findList();
        Inventory menu = Bukkit.createInventory(null, 54, plugin.getConfig().getString("shop.bagName"));
        ItemStack[] itemStacks = menu.getContents();
        int slot = 0;
        boolean isFull = false;
        for (Storage storage : list) {
            Material material = Material.getMaterial(storage.getItemName());
            short durability = storage.getDurability();
            int number = storage.getItemNumber();
            int i = 1;
            int all = number / material.getMaxStackSize();
            if (number % material.getMaxStackSize() != 0) {
                all++;
            }
            while (number > 0) {
                if (number > material.getMaxStackSize()) {
                    itemStacks[slot] = new ItemStack(material, material.getMaxStackSize(), durability);
                    number = number - material.getMaxStackSize();
                } else {
                    itemStacks[slot] = new ItemStack(material, number, durability);
                    number = number - number;
                }
                ItemMeta itemMeta = itemStacks[slot].getItemMeta();
                itemMeta.setDisplayName(String.format("Flow number:" + storage.getId()));
                List<String> stringList = new ArrayList<String>();
                stringList.add(String.format("Paid by %s", storage.getPaidFrom()));
                stringList.add(String.format("Market from %s", storage.getShopType()));
                if (storage.getBargainDate() != null) {
                    stringList.add(String.format("Sold in %s", storage.getBargainDate().getTime().toString()));
                } else {
                    stringList.add(String.format("Never sold"));
                }
                stringList.add(String.format("Bought in %s", storage.getOrderDate().getTime().toString()));
                stringList.add(String.format("Package %d/%d", i, all));

                if (storage.getBargainDate() == null) {
                    stringList.add(String.format("This is a refund!"));
                }
                itemMeta.setLore(stringList);
                itemStacks[slot].setItemMeta(itemMeta);
                slot++;
                i++;
                if (slot >= 53) {
                    isFull = true;
                    break;
                }
            }
            if (isFull) {
                break;
            }
        }
        if (list.size() > 0) {
            itemStacks[53] = new ItemStack(Material.ENDER_CHEST, 1);
            ItemMeta itemMeta = itemStacks[53].getItemMeta();
            itemMeta.setDisplayName("Collect All!");
            itemStacks[53].setItemMeta(itemMeta);
        }
        menu.setContents(itemStacks);
        player.openInventory(menu);


    }
}
