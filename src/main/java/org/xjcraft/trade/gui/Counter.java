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
import org.xjcraft.trade.entity.StockTrade;
import org.xjcraft.trade.utils.ItemUtil;
import org.xjcraft.utils.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Counter implements InventoryHolder, StockMarketGui {
    private final StockMarket plugin;
    private final Inventory inventory;
    private List<StockTrade> trades;

    public Counter(StockMarket plugin, Player player) {
        this.plugin = plugin;
        inventory = Bukkit.createInventory(this, 54, Config.config.getShop_offerName());
        inventory.setItem(53, ItemUtil.getSwitchBagButton());
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }

    private void update(Player player) {
        trades = plugin.getManager().getTrades(player);
        plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
    }

    private void refresh(Player player) {
        for (int i = 0; i < 53; i++) {
            if (i < trades.size()) {
                StockTrade trade = this.trades.get(i);
                ItemStack itemStack = plugin.getManager().getItemStack(trade.getItem(), trade.getHash());
                ItemMeta itemMeta = itemStack.getItemMeta();
                String s = StringUtil.applyPlaceHolder(MessageConfig.config.getTrade(), new HashMap<String, String>() {{
                    put("operation", trade.getSell() ? "卖出" : "收购");
                    put("type", plugin.getManager().getTranslate(itemStack));
                    put("subtype", trade.getHash());
                    put("currency", trade.getCurrency());
                    put("time", trade.getCreateTime().toString());
                    put("id", trade.getId() + "");
                    put("number", trade.getTradeNumber() + "");
                    put("price", trade.getPrice() + "");
                }});
                itemMeta.setLore(Arrays.asList(s.split("\n")));
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i, itemStack);
            } else {
                inventory.setItem(i, null);
            }
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
            player.openInventory(new Bag(plugin, player).getInventory());
        } else if (slot < trades.size()) {
            collect(player, slot);
        }
    }

    private void collect(Player player, int slot) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (this) {
                try {
                    cancelTrade(player, slot);
                } catch (NumberFormatException e) {

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    update(player);
                }
            }
        });
    }

    private void cancelTrade(Player player, int slot) {
        StockTrade trade = trades.get(slot);
        plugin.getManager().delete(trade);
        plugin.getManager().cancelTrade(player, trade);
    }


}
