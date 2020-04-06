package org.xjcraft.trade.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.StockMarket;
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.config.IconConfig;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.entity.StockTrade;
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
        inventory = Bukkit.createInventory(this, 54, Config.config.getTitle_offer());
        inventory.setItem(Slot.BAG, IconConfig.config.getBag());
//        inventory.setItem(Slot.CLOSE, IconConfig.config.getClose());
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }

    private void update(Player player) {
        trades = plugin.getManager().getTrades(player);
        plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
    }

    private void refresh(Player player) {
        for (int i = 0; i < Slot.BAG; i++) {
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
        switch (slot) {
            case Slot.BAG:
                player.openInventory(new Bag(plugin, player).getInventory());
                break;
//            case Slot.CLOSE:
//                player.closeInventory();
//                break;
            default:
                if (slot < trades.size() && slot >= 0) {
                    collect(player, slot);
                }
                break;
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
        StockTrade trade = plugin.getManager().getTradeById(trades.get(slot));
        if (trade == null) return;
        plugin.getManager().delete(trade);
        plugin.getManager().cancelTrade(player, trade);
    }

    interface Slot {
        int BAG = 53;
        int CLOSE = 51;

    }

}
