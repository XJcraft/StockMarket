package org.xjcraft.trade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.xjcraft.database.CustomItem;
import org.xjcraft.database.Storage;
import uk.co.tggl.pluckerpluck.multiinv.inventory.MIItemStack;

import java.util.ArrayList;
import java.util.Calendar;
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
        ItemStack template;
        for (Storage storage : list) {
            String[] names = storage.getItemName().split(":");
            Material material;
            short durability = storage.getDurability();
            if (names[0].equalsIgnoreCase("S")) {
                MIItemStack miItemStack = new MIItemStack(plugin.getDatabase().find(CustomItem.class).where().ieq("name", names[1]).findUnique().getFlatItem());
                material = miItemStack.getItemStack().getType();
                template = miItemStack.getItemStack();
            } else {
                material = Material.getMaterial(storage.getItemName());
                template = new ItemStack(material, 1, durability);
            }


            int number = storage.getItemNumber();
            int i = 1;
            int all = number / material.getMaxStackSize();
            if (number % material.getMaxStackSize() != 0) {
                all++;
            }
            while (number > 0) {
                if (number > material.getMaxStackSize()) {
                    template.setAmount(material.getMaxStackSize());
                    itemStacks[slot] = template;
                    number = number - material.getMaxStackSize();
                } else {
                    template.setAmount(number);
                    itemStacks[slot] = template;
                    number = number - number;
                }
                ItemMeta itemMeta = itemStacks[slot].getItemMeta();
                itemMeta.setDisplayName(String.format(plugin.getConfig().getString("message.lore.flow") + ":" + storage.getId()));
                List<String> stringList = new ArrayList<String>();
                stringList.add(String.format(plugin.getConfig().getString("message.lore.paid"), storage.getPaidFrom()));
                stringList.add(String.format(plugin.getConfig().getString("message.lore.from"), storage.getShopType()));
                if (storage.getBargainDate() != null) {
                    stringList.add(String.format(plugin.getConfig().getString("message.lore.sold"), (Calendar) storage.getBargainDate()));
                } else {
                    stringList.add(String.format(plugin.getConfig().getString("message.lore.neverSold")));
                }
                stringList.add(String.format(plugin.getConfig().getString("message.lore.bought"), (Calendar) storage.getOrderDate()));
                stringList.add(String.format(plugin.getConfig().getString("message.lore.package"), i, all));

                if (storage.getBargainDate() == null) {
                    stringList.add(String.format(plugin.getConfig().getString("message.lore.refund")));
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
            itemMeta.setDisplayName(plugin.getConfig().getString("message.lore.collect"));
            itemStacks[53].setItemMeta(itemMeta);
        }
        menu.setContents(itemStacks);
        player.openInventory(menu);


    }
}
