package org.xjcraft.trade.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.StockMarket;
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.entity.StockStorage;
import org.xjcraft.trade.utils.ItemUtil;
import org.xjcraft.utils.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Bag implements InventoryHolder, StockMarketGui {
    private final StockMarket plugin;
    private final Inventory inventory;
    private List<StockStorage> storage;

    public Bag(StockMarket plugin, Player player) {
        this.plugin = plugin;
        inventory = Bukkit.createInventory(this, 54, Config.config.getShop_bagName());
        inventory.setItem(53, ItemUtil.getSwitchCounterButton());
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }

    private void update(Player player) {
        storage = plugin.getManager().getStorage(player);
        plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
    }

    private void refresh(Player player) {
        for (int i = 0; i < 52; i++) {
            if (i < storage.size()) {
                StockStorage storage = this.storage.get(i);
                ItemStack itemStack = plugin.getManager().getItemStack(storage.getItem(), storage.getHash());
                ItemMeta itemMeta = itemStack.getItemMeta();
                String s = StringUtil.applyPlaceHolder(MessageConfig.config.getStorage(), new HashMap<String, String>() {{
                    put("seller", storage.getSource());
                    put("type", plugin.getManager().getTranslate(itemStack));
                    put("subtype", storage.getHash());
                    put("time", storage.getCreateTime().toString());
                    put("id", storage.getId() + "");
                    put("number", storage.getNumber() + "");
                }});
                itemMeta.setLore(Arrays.asList(s.split("\n")));
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i, itemStack);
            } else {
                inventory.setItem(i, null);
            }
        }
        if (storage.size() > 0) {
            inventory.setItem(52, ItemUtil.getCollectAll());
        }
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(Player player, int slot) {
        if (slot == 53) {
            player.closeInventory();
            player.openInventory(new Counter(plugin, player).getInventory());
        } else if (slot == 52 && storage.size() > 0) {
            collectAll(player);
        } else if (slot < storage.size()) {
            collect(player, slot);
        }
    }

    private void collect(Player player, int slot) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (this) {
                try {
                    collectStorage(player, slot);
                } catch (NumberFormatException e) {

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    update(player);
                }
            }
        });
    }

    private void collectAll(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (this) {
                for (int i = 0; i < storage.size(); i++) {
                    try {
                        collectStorage(player, i);
                    } catch (NumberFormatException e) {
                        break;
                    }
                }
                update(player);
            }
        });
    }

    private void collectStorage(Player player, int slot) throws NumberFormatException {

        StockStorage stockStorage = storage.get(slot);
        ItemStack itemStack = plugin.getManager().getItemStack(stockStorage.getItem(), stockStorage.getHash());
        Integer number = stockStorage.getNumber();
        itemStack.setAmount(number);
        int stacks = (int) Math.ceil(number.doubleValue() / itemStack.getMaxStackSize());
        int i = ItemUtil.countEmptySlot(player);
        if (i < stacks) {
            player.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSlotNotEnough(), new HashMap<String, String>() {{
                put("slot", stacks + "");
            }}));
            throw new NumberFormatException();
        }

        plugin.getManager().delete(stockStorage);
        player.getInventory().addItem(itemStack);
    }


}
