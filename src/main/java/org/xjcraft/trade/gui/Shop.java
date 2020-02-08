package org.xjcraft.trade.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.trade.StockMarket;
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.entity.Trade;
import org.xjcraft.trade.utils.ItemUtil;

import java.util.List;

public class Shop implements InventoryHolder, StockMarketGui {
    private final String currency;
    private final ItemStack item;
    private String itemLabel;
    Inventory inventory;
    //数据部分
    int price = 1;
    int number = 1;
    boolean stackMode = false;
    List<Trade> currentBuys;
    List<Trade> currentSells;
    private StockMarket plugin;


    public Shop(StockMarket plugin, Player player, String currency, ItemStack item) {
        this.plugin = plugin;
        this.currency = currency;
        this.item = item;
        inventory = Bukkit.createInventory(this, 54, Config.config.getShop_name());
        int[] upArrowSlots = {Slot.PRICE_1_PlUS, Slot.PRICE_10_PlUS, Slot.PRICE_100_PlUS, Slot.PRICE_1000_PlUS, Slot.PRICE_10000_PlUS,
                Slot.NUM_1_PLUS, Slot.NUM_10_PLUS, Slot.NUM_100_PLUS, Slot.NUM_1000_PLUS,};
        for (int slot : upArrowSlots) {
            inventory.setItem(slot, ItemUtil.getUpArrow());
        }
        int[] downArrwoSlots = {Slot.PRICE_1_MINUS, Slot.PRICE_10_MINUS, Slot.PRICE_100_MINUS, Slot.PRICE_1000_MINUS, Slot.PRICE_10000_MINUS,
                Slot.NUM_1_MINUS, Slot.NUM_10_MINUS, Slot.NUM_100_MINUS, Slot.NUM_1000_MINUS,};
        for (int slot : downArrwoSlots) {
            inventory.setItem(slot, ItemUtil.getDownArrow());
        }
        inventory.setItem(Slot.MARKET_INFO, ItemUtil.getMarketInfo());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }

    public void update(Player player) {
        if (itemLabel == null) itemLabel = plugin.getManager().getLabel(item);
        this.currentSells = plugin.getManager().getSells(currency, itemLabel);
        this.currentBuys = plugin.getManager().getBuys(currency, itemLabel);

        plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
    }

    public void refresh(Player player) {
        if (price > 99999) price = price % 99999;
        if (price < 1) price = price + 99999;
        if (number > 9999) number = number % 9999;
        if (number < 1) number = number + 9999;
        int[] priceIndexes = {Slot.PRICE_1_DISPLAY, Slot.PRICE_10_DISPLAY, Slot.PRICE_100_DISPLAY, Slot.PRICE_1000_DISPLAY, Slot.PRICE_10000_DISPLAY,};
        for (int i = 0; i < 5; i++) {
            ItemStack numberStack = ItemUtil.getNumberStack(price / (int) Math.pow(10, (double) i) % 10);
            inventory.setItem(priceIndexes[i], numberStack);
        }
        int[] numberIndexes = {Slot.NUM_1_DISPLAY, Slot.NUM_10_DISPLAY, Slot.NUM_100_DISPLAY, Slot.NUM_1000_DISPLAY};
        for (int i = 0; i < 4; i++) {
            ItemStack numberStack = ItemUtil.getNumberStack(number / (int) Math.pow(10, (double) i) % 10);
            inventory.setItem(numberIndexes[i], numberStack);
        }
        inventory.setItem(Slot.STACK_MODE, ItemUtil.getStackButton(item, stackMode,
                String.format(MessageConfig.config.getItemOwned(), ItemUtil.getItemNumber(player, item), item.getType().name()),
                MessageConfig.config.getStackButton()));
        inventory.setItem(Slot.CONFIRM_SELL, ItemUtil.getStackButton(new ItemStack(Material.RED_WOOL), false, String.format(MessageConfig.config.getSellButton(), price, number, item.getType().name())));
        inventory.setItem(Slot.CONFIRM_BUY, ItemUtil.getStackButton(new ItemStack(Material.LIME_WOOL), false, String.format(MessageConfig.config.getSellButton(), price, number, item.getType().name())));
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(Player player, int slot) {
        switch (slot) {
            case Slot.STACK_MODE:
                this.stackMode = !this.stackMode;
                break;

            case Slot.NUM_1_PLUS:
                this.number += 1;
                break;
            case Slot.NUM_10_PLUS:
                this.number += 10;
                break;
            case Slot.NUM_100_PLUS:
                this.number += 100;
                break;
            case Slot.NUM_1000_PLUS:
                this.number += 1000;
                break;
            case Slot.NUM_1_MINUS:
                this.number -= 1;
                break;
            case Slot.NUM_10_MINUS:
                this.number -= 10;
                break;
            case Slot.NUM_100_MINUS:
                this.number -= 100;
                break;
            case Slot.NUM_1000_MINUS:
                this.number -= 1000;
                break;
            case Slot.PRICE_1_PlUS:
                this.price += 1;
                break;
            case Slot.PRICE_10_PlUS:
                this.price += 10;
                break;
            case Slot.PRICE_100_PlUS:
                this.price += 100;
                break;
            case Slot.PRICE_1000_PlUS:
                this.price += 1000;
                break;
            case Slot.PRICE_10000_PlUS:
                this.price += 10000;
                break;
            case Slot.PRICE_1_MINUS:
                this.price -= 1;
                break;
            case Slot.PRICE_10_MINUS:
                this.price -= 10;
                break;
            case Slot.PRICE_100_MINUS:
                this.price -= 100;
                break;
            case Slot.PRICE_1000_MINUS:
                this.price -= 1000;
                break;
            case Slot.PRICE_10000_MINUS:
                this.price -= 10000;
                break;
            default:
                return;

        }
        this.refresh(player);
    }


    public static class Slot {

        public static final int MARKET_INFO = 14;
        public static final int CURRENCY_DIGITAL_0 = 15;
        public static final int CURRENCY_DIGITAL_1 = 16;
        public static final int CURRENCY_DIGITAL_2 = 17;
        //价格面板
        public static final int PRICE_1_PlUS = 4;
        public static final int PRICE_10_PlUS = 3;
        public static final int PRICE_100_PlUS = 2;
        public static final int PRICE_1000_PlUS = 1;
        public static final int PRICE_10000_PlUS = 0;
        public static final int PRICE_1_DISPLAY = 13;
        public static final int PRICE_10_DISPLAY = 12;
        public static final int PRICE_100_DISPLAY = 11;
        public static final int PRICE_1000_DISPLAY = 10;
        public static final int PRICE_10000_DISPLAY = 9;
        public static final int PRICE_1_MINUS = 22;
        public static final int PRICE_10_MINUS = 21;
        public static final int PRICE_100_MINUS = 20;
        public static final int PRICE_1000_MINUS = 19;
        public static final int PRICE_10000_MINUS = 18;

        //数量面板
        public static final int NUM_1_PLUS = 30;
        public static final int NUM_10_PLUS = 29;
        public static final int NUM_100_PLUS = 28;
        public static final int NUM_1000_PLUS = 27;
        public static final int NUM_1_DISPLAY = 39;
        public static final int NUM_10_DISPLAY = 38;
        public static final int NUM_100_DISPLAY = 37;
        public static final int NUM_1000_DISPLAY = 36;
        public static final int NUM_1_MINUS = 48;
        public static final int NUM_10_MINUS = 47;
        public static final int NUM_100_MINUS = 46;
        public static final int NUM_1000_MINUS = 45;

        //操作按钮
        public static final int REMAIN = 31;
        public static final int STACK_MODE = 40;
        public static final int CONFIRM_SELL = 42;
        public static final int CONFIRM_BUY = 44;

    }
}
