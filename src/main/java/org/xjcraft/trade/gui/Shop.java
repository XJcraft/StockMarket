package org.xjcraft.trade.gui;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.trade.StockMarket;
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.entity.StockTrade;
import org.xjcraft.trade.utils.ItemUtil;
import org.xjcraft.utils.StringUtil;

import java.util.HashMap;
import java.util.List;

public class Shop implements InventoryHolder, StockMarketGui {
    private final String currency;
    private final ItemStack item;
    private String itemLabel;
    private String itemHash;
    Inventory inventory;
    //数据部分
    Integer price = null;
    Integer number = null;
    boolean stackMode = false;
    List<StockTrade> currentBuys;
    List<StockTrade> currentSells;
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
        inventory.setItem(Slot.SWITCH_BAG, ItemUtil.getSwitchBagButton());
        inventory.setItem(Slot.SWITCH_COUNTER, ItemUtil.getSwitchCounterButton());
        inventory.setItem(Slot.SELL_INFO, ItemUtil.getSellInfoButton());
        inventory.setItem(Slot.BUY_INFO, ItemUtil.getBuyInfoButton());
        inventory.setItem(Slot.PRICE_INPUT, ItemUtil.getInputButton());
        inventory.setItem(Slot.NUM_INPUT, ItemUtil.getInputButton());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }

    public void update(Player player) {
        if (itemLabel == null) itemLabel = item.getType().name();
        if (itemHash == null) itemHash = plugin.getManager().getSubType(item);
        this.currentSells = plugin.getManager().getSells(currency, itemLabel, itemHash);
        this.currentBuys = plugin.getManager().getBuys(currency, itemLabel, itemHash);

        plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
    }

    public void refresh(Player player) {
        int itemsInBag = ItemUtil.getItemNumber(player, item);
        if (price == null) {
            if (currentSells != null && currentSells.size() > 0) {
                price = currentSells.get(0).getPrice();
            } else {
                price = 1;
            }
        }
        if (number == null) {
            if (itemsInBag > 0) {
                number = itemsInBag;
            } else {
                number = 1;
            }
        }
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
        inventory.setItem(Slot.MARKET_INFO, ItemUtil.getMarketInfo(new String[]{this.currentSells.size() == 0 ? MessageConfig.config.getNohighest() : StringUtil.applyPlaceHolder(MessageConfig.config.getSellInfo(), new HashMap<String, String>() {{
            put("size", currentSells.size() + "");
            put("price", currentSells.get(0).getPrice() + "");
            put("currency", currentSells.get(0).getCurrency() + "");
        }}), this.currentBuys.size() == 0 ? MessageConfig.config.getNolowest() : StringUtil.applyPlaceHolder(MessageConfig.config.getBuyInfo(), new HashMap<String, String>() {{
            put("size", currentBuys.size() + "");
            put("price", currentBuys.get(0).getPrice() + "");
            put("currency", currentBuys.get(0).getCurrency() + "");
        }})}));

        inventory.setItem(Slot.CONFIRM_SELL, ItemUtil.getStackButton(new ItemStack(Material.RED_WOOL), false, "出售", String.format(MessageConfig.config.getSellButton(), price, number * (stackMode ? item.getMaxStackSize() : 1), plugin.getManager().getTranslate(item))));
        inventory.setItem(Slot.CONFIRM_BUY, ItemUtil.getStackButton(new ItemStack(Material.LIME_WOOL), false, "买入", String.format(MessageConfig.config.getBuyButton(), price, number * (stackMode ? item.getMaxStackSize() : 1), plugin.getManager().getTranslate(item))));
        ItemStack clone = item.clone();
        clone.setAmount(stackMode ? item.getMaxStackSize() : 1);
        inventory.setItem(Slot.STACK_MODE, clone);
        inventory.setItem(Slot.REMAIN, ItemUtil.getStackButton(new ItemStack(Material.CHEST), false,
                "我的背包", String.format(MessageConfig.config.getItemOwned(), itemsInBag, plugin.getManager().getTranslate(item) + ""),
                MessageConfig.config.getStackButton()));
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(Player player, int slot) {
        switch (slot) {
            case Slot.REMAIN:
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
            case Slot.CONFIRM_BUY:
                player.closeInventory();
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.getManager().buy(player, currency, item, price, number * (stackMode ? item.getMaxStackSize() : 1), success -> plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        new Bag(plugin, player).collectAll(player);
                        player.openInventory(getInventory());
                    }, 1));
                });
                return;
            case Slot.CONFIRM_SELL:
                player.closeInventory();
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.getManager().sell(player, currency, item, price, number * (stackMode ? item.getMaxStackSize() : 1), success -> plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.openInventory(getInventory()), 1));
                });
                return;
            case Slot.SWITCH_BAG:
                player.closeInventory();
                player.openInventory(new Bag(plugin, player).getInventory());
                return;
            case Slot.SWITCH_COUNTER:
                player.closeInventory();
                player.openInventory(new Counter(plugin, player).getInventory());
                return;
            case Slot.BUY_INFO:
                for (StockTrade currentBuy : this.currentBuys) {
                    player.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getBuyDetailInfo(), new HashMap<String, String>() {{
                        put("player", currentBuy.getPlayer());
                        put("currency", currentBuy.getCurrency());
                        put("price", currentBuy.getPrice() + "");
                        put("number", currentBuy.getTradeNumber() + "");
                        put("type", currentBuy.getItem());
                        put("subtype", currentBuy.getHash());
                    }}));
                }
                break;
            case Slot.SELL_INFO:
                for (StockTrade currentBuy : this.currentSells) {
                    player.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSellDetailInfo(), new HashMap<String, String>() {{
                        put("player", currentBuy.getPlayer());
                        put("currency", currentBuy.getCurrency());
                        put("price", currentBuy.getPrice() + "");
                        put("number", currentBuy.getTradeNumber() + "");
                        put("type", currentBuy.getItem());
                        put("subtype", currentBuy.getHash());
                    }}));
                }
                break;
            case Slot.NUM_INPUT:
                new AnvilGUI.Builder().plugin(plugin)
                        .title(MessageConfig.config.getInputButton())
                        .text(number + "")
                        .item(new ItemStack(Material.PAPER))
                        .onComplete((p, text) -> {
                            boolean success = false;
                            try {
                                int i = Integer.parseInt(text);
                                number = i;
                                success = true;
                                return AnvilGUI.Response.close();
                            } catch (NumberFormatException e) {
                                return AnvilGUI.Response.text(MessageConfig.config.getInputTitle());
                            } finally {
                                if (success) player.openInventory(Shop.this.getInventory());
                            }

                        }).onClose(p -> {
                    player.openInventory(getInventory());
                    plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
                }).open(player);

                break;
            case Slot.PRICE_INPUT:
                new AnvilGUI.Builder().plugin(plugin)
                        .title(MessageConfig.config.getInputButton())
                        .text(price + "")
                        .item(new ItemStack(Material.PAPER))
                        .onComplete((p, text) -> {
                            boolean success = false;
                            try {
                                int i = Integer.parseInt(text);
                                price = i;
                                success = true;
                                return AnvilGUI.Response.close();
                            } catch (NumberFormatException e) {
                                return AnvilGUI.Response.text(MessageConfig.config.getInputTitle());
                            } finally {
                                if (success) player.openInventory(Shop.this.getInventory());
                            }

                        }).onClose(p -> {
                    player.openInventory(getInventory());
                    plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
                }).open(player);
                break;
            default:
                return;

        }
        this.refresh(player);
    }


    public static class Slot {

        public static final int PRICE_INPUT = 14;
        public static final int MARKET_INFO = 15;
        public static final int SELL_INFO = 6;
        public static final int BUY_INFO = 24;
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

        public static final int NUM_INPUT = 40;
        //操作按钮
        public static final int REMAIN = 49;
        public static final int STACK_MODE = 31;
        public static final int SWITCH_BAG = 43;
        public static final int SWITCH_COUNTER = 44;
        public static final int CONFIRM_SELL = 52;
        public static final int CONFIRM_BUY = 53;


    }
}
