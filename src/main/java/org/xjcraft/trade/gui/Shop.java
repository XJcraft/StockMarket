package org.xjcraft.trade.gui;

import lombok.AllArgsConstructor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.trade.StockMarket;
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.config.IconConfig;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.utils.ItemUtil;
import org.xjcraft.trade.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop implements InventoryHolder, StockMarketGui {
    private final String currency;
    private final ItemStack item;
    private ShopMode mode;
    private Sign sign;
    private String itemLabel;
    private String itemHash;
    Inventory inventory;
    //数据部分
    Integer price = null;
    Integer number = null;
    List<Map<String, Object>> currentBuys;
    List<Map<String, Object>> currentSells;
    private StockMarket plugin;
    int itemsInBag;

    public Shop(StockMarket plugin, Player player, String currency, ItemStack item, Sign sign, ShopMode mode) {
        this.plugin = plugin;
        this.currency = currency;
        this.item = item.clone();
        this.mode = mode;
        this.item.setAmount(1);
        this.sign = sign;

        setInventory(mode);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }

    public void setInventory(ShopMode mode) {
        switch (mode) {

            case SIMPLE:
                inventory = Bukkit.createInventory(this, 54, Config.config.getTitle_simple());
                break;
            case BUY:
                inventory = Bukkit.createInventory(this, 54, Config.config.getTitle_buy());
                break;
            case SELL:
                inventory = Bukkit.createInventory(this, 54, Config.config.getTitle_sell());
                break;
        }


    }

    public void update(Player player) {
        if (itemLabel == null) itemLabel = item.getType().name();
        if (itemHash == null) itemHash = plugin.getManager().getSubType(item);
        this.currentSells = plugin.getManager().getSells(currency, itemLabel, itemHash);
        this.currentBuys = plugin.getManager().getBuys(currency, itemLabel, itemHash);
        plugin.getServer().getScheduler().runTask(plugin, () -> refresh(player));
    }

    public void refresh(Player player) {

        itemsInBag = ItemUtil.getItemNumber(player, item);
        if (price == null) {
            if ((currentSells != null && currentSells.size() > 0)) {
                price = (int) currentSells.get(0).get("price");
            } else {
                price = 1;
            }
        }
        if (number == null) {
            if (itemsInBag > 0 && !mode.buy) {
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
        HashMap<String, String> placeHolder = new HashMap<>();
        placeHolder.put("price", price + "");//设置的价格
        placeHolder.put("currency", currency);//使用的货币
        placeHolder.put("amount", number + "");//设置的数量
        placeHolder.put("remain", itemsInBag + "");//背包中剩余物品数量
        placeHolder.put("sellSize", currentSells.size() + "");//出售笔数
        placeHolder.put("sellPrice", (currentSells.size() > 0 ? (int) currentSells.get(0).get("price") : 0) + "");//最低出售价中第一笔的价格
        placeHolder.put("sellNumber", (currentSells.size() > 0 ? (int) currentSells.get(0).get("trade_number") : 0) + "");//最低出售价中第一笔的数量
        placeHolder.put("buySize", currentBuys.size() + "");//收购笔数
        placeHolder.put("buyPrice", (currentBuys.size() > 0 ? (int) currentBuys.get(0).get("price") : 0) + "");//最高收购价中第一笔价格
        placeHolder.put("buyNumber", (currentBuys.size() > 0 ? (int) currentBuys.get(0).get("trade_number") : 0) + "");//最高收购价中第一笔数量
        placeHolder.put("name", plugin.getManager().getTranslate(item));//物品名称
        for (int i = 0; i < inventory.getSize(); i++) {
            switch (i) {

                //价格面板
                case Slot.PRICE_1_PlUS:
                case Slot.PRICE_10_PlUS:
                case Slot.PRICE_100_PlUS:
                case Slot.PRICE_1000_PlUS:
                case Slot.PRICE_10000_PlUS:
                    inventory.setItem(i, mode.priceModify ? ItemUtil.getUpArrow() : IconConfig.config.getAir());
                    break;
                case Slot.PRICE_1_MINUS:
                case Slot.PRICE_10_MINUS:
                case Slot.PRICE_100_MINUS:
                case Slot.PRICE_1000_MINUS:
                case Slot.PRICE_10000_MINUS:
                    inventory.setItem(i, mode.priceModify ? ItemUtil.getDownArrow() : IconConfig.config.getAir());
                    break;
                case Slot.PRICE_INPUT:
                    inventory.setItem(i, mode.priceModify ? ItemUtil.getAmountInfo("价格"):IconConfig.config.getAir());
                    break;
                case Slot.NUM_INPUT:
                    inventory.setItem(i, ItemUtil.getAmountInfo("数量"));
                    break;
                //数量面板
                case Slot.NUM_1_PLUS:
                case Slot.NUM_10_PLUS:
                case Slot.NUM_100_PLUS:
                case Slot.NUM_1000_PLUS:
                    inventory.setItem(i, ItemUtil.getUpArrow());
                    break;
                case Slot.NUM_1_MINUS:
                case Slot.NUM_10_MINUS:
                case Slot.NUM_100_MINUS:
                case Slot.NUM_1000_MINUS:
                    inventory.setItem(i, ItemUtil.getDownArrow());
                    break;
                case Slot.ITEM_INSTANCE:
                    inventory.setItem(i, item);
                    break;
                case Slot.PRICE_INFO:
                    switch (this.mode) {
                        case SIMPLE:
                            inventory.setItem(i, ItemUtil.getSellInfoButton());
                            break;
                        case BUY:
                            inventory.setItem(i, ItemUtil.getBuyInfoButton());
                            break;
                        case SELL:
                            inventory.setItem(i, ItemUtil.getSellInfoButton());
                            break;
                    }
                case Slot.REMAIN:
                    break;
                case Slot.SWITCH_BAG:
                    inventory.setItem(i, ItemUtil.getSwitchBagButton());
                    break;
                case Slot.SWITCH_COUNTER:
                    inventory.setItem(i, ItemUtil.getSwitchCounterButton());
                    break;
                case Slot.MINE:
                    if (mode.buy) {
                        inventory.setItem(i, IconConfig.config.getAir());
                    } else {
                        inventory.setItem(i,ItemUtil.getMine());
                    }
                    break;
                case Slot.CONFIRM_BUY:
                    if (mode.buy) {
                        if (mode.priceModify) {
                            inventory.setItem(i,ItemUtil.getBuyConfirm());
                        } else {
                            inventory.setItem(i,ItemUtil.getBuySimple());
                        }
                    } else {
                        inventory.setItem(i, ItemUtil.getSellConfirm());
                    }
                    break;
                case Slot.CLOSE:
                    if (mode.priceModify)
                        inventory.setItem(i, ItemUtil.getClose());
                    break;
                case Slot.SWITCH_BUY:
                    inventory.setItem(i, (mode == ShopMode.SIMPLE) ? ItemUtil.getBuymod() : IconConfig.config.getAir());
                    break;
                case Slot.SWITCH_SELL:
                    inventory.setItem(i, (mode == ShopMode.SIMPLE) ? ItemUtil.getSellmod() : IconConfig.config.getAir());
                    break;
            }
        }

        sign.setLine(2, this.currentSells.size() == 0 ? "" : StringUtil.applyPlaceHolder(Config.config.getLine2(), new HashMap<String, String>() {{
            put("sell", Shop.this.currentSells.get(0).get("price") + "");
        }}));
        sign.setLine(3, this.currentBuys.size() == 0 ? "" : StringUtil.applyPlaceHolder(Config.config.getLine3(), new HashMap<String, String>() {{
            put("buy", Shop.this.currentBuys.get(0).get("price") + "");
        }}));
        sign.update();
        
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(Player player, int slot) {
        switch (slot) {
            case Slot.CLOSE:
                plugin.getManager().openIndex(player, sign, currency, item);
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
                if (mode.priceModify)
                    this.price += 1;
                break;
            case Slot.PRICE_10_PlUS:
                if (mode.priceModify)
                    this.price += 10;
                break;
            case Slot.PRICE_100_PlUS:
                if (mode.priceModify)
                    this.price += 100;
                break;
            case Slot.PRICE_1000_PlUS:
                if (mode.priceModify)
                    this.price += 1000;
                break;
            case Slot.PRICE_10000_PlUS:
                if (mode.priceModify)
                    this.price += 10000;
                break;
            case Slot.PRICE_1_MINUS:
                if (mode.priceModify)
                    this.price -= 1;
                break;
            case Slot.PRICE_10_MINUS:
                if (mode.priceModify)
                    this.price -= 10;
                break;
            case Slot.PRICE_100_MINUS:
                if (mode.priceModify)
                    this.price -= 100;
                break;
            case Slot.PRICE_1000_MINUS:
                if (mode.priceModify)
                    this.price -= 1000;
                break;
            case Slot.PRICE_10000_MINUS:
                if (mode.priceModify)
                    this.price -= 10000;
                break;
            case Slot.SWITCH_BUY:
                if (mode == ShopMode.SIMPLE) {
                    this.mode = ShopMode.BUY;
                    this.setInventory(this.mode);
                    player.openInventory(getInventory());
                }
                break;
            case Slot.SWITCH_SELL:
                if (mode == ShopMode.SIMPLE) {
                    this.mode = ShopMode.SELL;
                    number = null;
                    this.setInventory(this.mode);
                    player.openInventory(getInventory());
                }
                break;
            case Slot.MINE:
                if (mode == ShopMode.SELL) {
                    this.number = Math.max(itemsInBag, 1);

                }

                break;
            case Slot.CONFIRM_BUY:
                player.closeInventory();
                if (mode.buy) {
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                        plugin.getManager().buy(player, currency, item, price, number, success -> plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                            new Bag(plugin, player).collectAll(player);
                            player.openInventory(getInventory());
                            if (mode == ShopMode.SIMPLE && currentSells.size() == 0) {
                                plugin.getManager().openIndex(player, sign, currency, item);
                            }
                        }, 1));
                    });
                } else {
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                        plugin.getManager().sell(player, currency, item, price, number, success -> plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                            player.openInventory(getInventory());
                        }, 1));
                    });
                }
                return;
            case Slot.SWITCH_BAG:
                player.openInventory(new Bag(plugin, player).getInventory());
                return;
            case Slot.SWITCH_COUNTER:
                player.openInventory(new Counter(plugin, player).getInventory());
                return;
            case Slot.BUY_INFO:

                break;
            case Slot.SELL_INFO:

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
                if (mode.priceModify)
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
            case Slot.PRICE_INFO:
                if (mode.buy) {
                    for (Map<String, Object> currentBuy : this.currentSells) {
                        player.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSellDetailInfo(), new HashMap<String, String>() {{
                            put("player", (String) currentBuy.get("player"));
                            put("currency",(String) currentBuy.get("currency"));
                            put("price",String.valueOf(currentBuy.get("price")) + "");
                            put("number",String.valueOf(currentBuy.get("trade_number")) + "");
                            put("type",(String) currentBuy.get("item_name"));
                        }}));
                    }
                } else {
                    for (Map<String, Object> currentBuy : this.currentBuys) {
                        player.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getBuyDetailInfo(), new HashMap<String, String>() {{
                            put("player", (String) currentBuy.get("player"));
                            put("currency",(String) currentBuy.get("currency"));
                            put("price",String.valueOf(currentBuy.get("price")) + "");
                            put("number",String.valueOf(currentBuy.get("trade_number")) + "");
                            put("type",(String) currentBuy.get("item_name"));
                        }}));
                    }
                }
                break;
            default:
                return;

        }
        this.refresh(player);
    }


    interface Slot {

        int SELL_INFO = 7;
        int BUY_INFO = 17;
        //价格面板
        int PRICE_1_PlUS = 5;
        int PRICE_10_PlUS = 4;
        int PRICE_100_PlUS = 3;
        int PRICE_1000_PlUS = 2;
        int PRICE_10000_PlUS = 1;
        int PRICE_1_DISPLAY = 14;
        int PRICE_10_DISPLAY = 13;
        int PRICE_100_DISPLAY = 12;
        int PRICE_1000_DISPLAY = 11;
        int PRICE_10000_DISPLAY = 10;
        int PRICE_1_MINUS = 23;
        int PRICE_10_MINUS = 22;
        int PRICE_100_MINUS = 21;
        int PRICE_1000_MINUS = 20;
        int PRICE_10000_MINUS = 19;
        int PRICE_INPUT = 15;
        int PRICE_INFO = 9;

        //数量面板
        int NUM_1_PLUS = 31;
        int NUM_10_PLUS = 30;
        int NUM_100_PLUS = 29;
        int NUM_1000_PLUS = 28;
        int NUM_1_DISPLAY = 40;
        int NUM_10_DISPLAY = 39;
        int NUM_100_DISPLAY = 38;
        int NUM_1000_DISPLAY = 37;
        int NUM_1_MINUS = 49;
        int NUM_10_MINUS = 48;
        int NUM_100_MINUS = 47;
        int NUM_1000_MINUS = 46;
        int NUM_INPUT = 41;
        int ITEM_INSTANCE = 36;

        //操作按钮
        int REMAIN = 50;
        int SWITCH_BUY = 25;
        int SWITCH_SELL = 26;
        int SWITCH_BAG = 34;
        int SWITCH_COUNTER = 35;
        int MINE = 52;
        int CONFIRM_BUY = 53;
        int CLOSE = 8;

    }

    @AllArgsConstructor
    public enum ShopMode {
        SIMPLE(false, true),
        BUY(true, true),
        SELL(true, false);
        public boolean priceModify;
        public boolean buy;

    }
}
