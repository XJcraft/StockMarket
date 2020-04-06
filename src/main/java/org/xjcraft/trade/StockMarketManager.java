package org.xjcraft.trade;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.zjyl1994.minecraftplugin.multicurrency.services.BankService;
import com.zjyl1994.minecraftplugin.multicurrency.utils.OperateResult;
import com.zjyl1994.minecraftplugin.multicurrency.utils.TxTypeEnum;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.bean.StockCustomItem;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.entity.StockHistory;
import org.xjcraft.trade.entity.StockStorage;
import org.xjcraft.trade.entity.StockTrade;
import org.xjcraft.trade.gui.Callback;
import org.xjcraft.trade.gui.Menu;
import org.xjcraft.trade.gui.Shop;
import org.xjcraft.trade.utils.ItemUtil;
import org.xjcraft.utils.JSON;
import org.xjcraft.utils.StringUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class StockMarketManager {
    private StockMarket plugin;
    private Dao dao;
    private final String SHOP_ACCOUNT = "$SHOP";
    private Map<String, String> map;

    public StockMarketManager(StockMarket stockMarket, Dao dao) {
        plugin = stockMarket;
        this.dao = dao;
        InputStream resource = plugin.getResource("zh_cn.json");
        map = new Gson().fromJson(new InputStreamReader(resource), Map.class);
    }


    Cache<ItemStack, String> build = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    /**
     * 取中文翻译
     *
     * @param itemStack
     * @return
     */
    public String getTranslate(ItemStack itemStack) {
        if (itemStack.getItemMeta() != null && !StringUtil.isEmpty(itemStack.getItemMeta().getDisplayName())) {
            return itemStack.getItemMeta().getDisplayName();
        }
        return map.getOrDefault("block." + itemStack.getType().getKey().getNamespace() + "." + itemStack.getType().getKey().getKey(), map.getOrDefault("item." + itemStack.getType().getKey().getNamespace() + "." + itemStack.getType().getKey().getKey(), itemStack.getType().getKey().getKey()));
    }

    /**
     * 由物品和hashcode取物品meta
     *
     * @param itemStack
     * @param hashcode
     * @return
     */
    public ItemMeta getMeta(ItemStack itemStack, String hashcode) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Class<? extends ItemMeta> aClass = itemMeta.getClass();
        String className = aClass.getName().replaceAll("org\\.bukkit\\.craftbukkit\\..*?\\.inventory\\.", "");
        Map<String, ItemMeta> specials = dao.getSpecials(className);
        return specials.get(hashcode);
    }

    /**
     * 由类型和hashcode获取实物
     *
     * @param type
     * @param hashcode
     * @return
     */
    public ItemStack getItemStack(String type, String hashcode) {
        Material material = Material.valueOf(type);
        ItemStack itemStack = new ItemStack(material);
        if (!StringUtil.isEmpty(hashcode)) {
            ItemMeta meta = plugin.getManager().getMeta(itemStack, hashcode);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    /**
     * 搜索或生成新的hashcode
     *
     * @param item
     * @return
     */
    public String getSubType(ItemStack item) {
//        System.out.println(item.getItemMeta().hashCode());
        try {
            return build.get(item, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (!item.hasItemMeta()) {
                        return "";
                    } else {
                        try {
                            ItemMeta itemMeta = item.getItemMeta();
                            Class<? extends ItemMeta> aClass = itemMeta.getClass();
                            String className = aClass.getName().replaceAll("org\\.bukkit\\.craftbukkit\\..*?\\.inventory\\.", "");
//                Method method = aClass.getSuperclass().getClasses()[0].getDeclaredMethod("deserialize", (Class<Map<String, Object>>) (Class) Map.class);
                            Integer hashcode = ItemUtil.hashcode(itemMeta);
                            Map<String, ItemMeta> specials = dao.getSpecials(className);
//                            for (Map.Entry<String, ItemMeta> entry : specials.entrySet()) {
//                                boolean equals = entry.getValue().equals(itemMeta);
//                                if (equals) {
//                                    return entry.getKey() + "";
//                                }
//                            }
                            ItemMeta meta = specials.get(hashcode.toString());
                            if (meta != null) return hashcode.toString();
                            StockCustomItem stockCustomItem = new StockCustomItem();
                            stockCustomItem.setId(hashcode);
                            stockCustomItem.setMeta(className);
                            stockCustomItem.setFlatItem(itemMeta);
                            dao.save(stockCustomItem);

                            return stockCustomItem.getId() + "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return "";
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<StockTrade> getSells(String currency, String type, String subType) {
        return dao.getSells(currency, type, subType);
    }

    public List<StockTrade> getBuys(String currency, String itemLabel, String itemHash) {
        return dao.getBuys(currency, itemLabel, itemHash);
    }

    /**
     * 购买操作
     *
     * @param buyer
     * @param currency
     * @param itemStack
     * @param price
     * @param amount
     * @param callback
     */
    public void buy(Player buyer, String currency, ItemStack itemStack, Integer price, Integer amount, Callback callback) {
        String type = itemStack.getType().name();
        String subType = getSubType(itemStack);
        synchronized (this) {
            Integer total = price * amount;
            OperateResult operateResult = BankService.queryCurrencyBalance(buyer.getName(), currency);
            BigDecimal data = (BigDecimal) operateResult.getData();
            if (data.intValue() < total) {
                buyer.sendMessage(MessageConfig.config.getMoreMoney());
                callback.onDone(false);
                return;
            }

            List<StockTrade> sells = this.getSells(currency, type, subType);
            for (StockTrade sell : sells) {
                if (sell.getPrice() <= price) {
                    //direct buy
                    if (sell.getTradeNumber() <= amount) {
                        OperateResult result = BankService.transferTo(buyer.getName(), sell.getPlayer(), currency, new BigDecimal((sell.getTradeNumber() * sell.getPrice())), TxTypeEnum.SHOP_TRADE_OUT, String.format("buy %s %s %s with %s %s each", sell.getTradeNumber(), type, subType, sell.getPrice(), currency));
                        if (!result.getSuccess()) {
                            buyer.sendMessage("error:" + operateResult.getReason());
                            callback.onDone(false);
                            return;
                        }
                        amount -= sell.getTradeNumber();
                        StockHistory stockHistory = new StockHistory(buyer.getName(), sell.getPlayer(), sell.getItem(), sell.getHash(), sell.getPrice(), sell.getCurrency(), sell.getTradeNumber());
                        dao.save(stockHistory);
                        dao.delete(sell);
                        gain(type, subType, sell.getPlayer(), sell.getTradeNumber(), buyer.getName());
                        buyer.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getBuyHint(), stockHistory.fetchPlaceHolder()));
                    } else {
                        OperateResult result = BankService.transferTo(buyer.getName(), sell.getPlayer(), currency, new BigDecimal((amount * sell.getPrice())), TxTypeEnum.SHOP_TRADE_OUT, String.format("buy %s %s %s with %s %s each", amount, type, subType, sell.getPrice(), currency));
                        if (!result.getSuccess()) {
                            buyer.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        sell.setTradeNumber(sell.getTradeNumber() - amount);
                        StockHistory stockHistory = new StockHistory(buyer.getName(), sell.getPlayer(), sell.getItem(), sell.getHash(), sell.getPrice(), sell.getCurrency(), amount);
                        dao.save(stockHistory);
                        dao.save(sell);
                        gain(type, subType, sell.getPlayer(), amount, buyer.getName());
                        amount = 0;
                        buyer.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getBuyHint(), stockHistory.fetchPlaceHolder()));
                        break;
                    }
                }
            }
            if (amount > 0) {
                //put buy order
                OperateResult result = BankService.transferTo(buyer.getName(), SHOP_ACCOUNT, currency, new BigDecimal(amount * price), TxTypeEnum.SHOP_TRADE_OUT, String.format("order %s %s %s with %s %s  each", amount, type, subType, price, currency));
                if (!result.getSuccess()) {
                    buyer.sendMessage("error:" + result.getReason());
                    callback.onDone(false);
                    return;
                }
                StockTrade trade = new StockTrade();

                trade.setItem(type);
                trade.setHash(subType);
                trade.setCurrency(currency);
                trade.setPrice(price);
                trade.setSell(false);
                trade.setPlayer(buyer.getName());
                trade.setTradeNumber(amount);
                dao.save(trade);
                int finalAmount = amount;
                buyer.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getBuyRemain(), new HashMap<String, String>() {{
                    put("number", finalAmount + "");
                    put("currency", currency + "");
                    put("price", price + "");
                }}));
            }
            callback.onDone(true);
            return;
        }
    }

    /**
     * 出售操作
     *
     * @param seller
     * @param currency
     * @param itemStack
     * @param price
     * @param amount
     * @param callback
     */
    public void sell(Player seller, String currency, ItemStack itemStack, int price, int amount, Callback callback) {
        String type = itemStack.getType().name();
        String subType = getSubType(itemStack);
        synchronized (this) {
            int itemNumber = ItemUtil.getItemNumber(seller, itemStack);
            if (itemNumber < amount) {
                seller.sendMessage(MessageConfig.config.getMoreItem());
                callback.onDone(false);
                return;
            }
            List<StockTrade> buys = this.getBuys(currency, type, subType);
            for (StockTrade buy : buys) {
                if (buy.getPrice() >= price) {
                    //direct sell with higher price
                    if (buy.getTradeNumber() <= amount) {
                        OperateResult result = BankService.transferTo(SHOP_ACCOUNT, seller.getName(), currency, new BigDecimal((buy.getTradeNumber() * buy.getPrice())), TxTypeEnum.SHOP_TRADE_OUT, String.format("sell %s %s %s with %s %s each", buy.getTradeNumber(), type, subType, buy.getPrice(), currency));
                        if (!result.getSuccess()) {
                            seller.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        amount -= buy.getTradeNumber();
                        StockHistory stockHistory = new StockHistory(buy.getPlayer(), seller.getName(), buy.getItem(), buy.getHash(), buy.getPrice(), buy.getCurrency(), buy.getTradeNumber());
                        dao.save(stockHistory);
                        dao.delete(buy);
                        pay(seller, itemStack, buy.getPlayer(), buy.getTradeNumber());
                        seller.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSellHint(), stockHistory.fetchPlaceHolder()));
                    } else {
                        OperateResult result = BankService.transferTo(SHOP_ACCOUNT, seller.getName(), currency, new BigDecimal((amount * buy.getPrice())), TxTypeEnum.SHOP_TRADE_OUT, String.format("sell %s %s %s with %s %s each", amount, type, subType, buy.getPrice(), currency));
                        if (!result.getSuccess()) {
                            seller.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        buy.setTradeNumber(buy.getTradeNumber() - amount);
                        StockHistory stockHistory = new StockHistory(buy.getPlayer(), seller.getName(), buy.getItem(), buy.getHash(), buy.getPrice(), buy.getCurrency(), amount);
                        dao.save(stockHistory);
                        dao.save(buy);
                        pay(seller, itemStack, buy.getPlayer(), amount);
                        amount = 0;
                        seller.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSellHint(), stockHistory.fetchPlaceHolder()));
                        break;
                    }
                }
            }
            if (amount > 0) {
                //put buy order
                try {
                    ItemUtil.removeItem(seller, itemStack, amount);
                    StockTrade trade = new StockTrade();

                    trade.setItem(type);
                    trade.setHash(subType);
                    trade.setCurrency(currency);
                    trade.setPrice(price);
                    trade.setSell(true);
                    trade.setPlayer(seller.getName());
                    trade.setTradeNumber(amount);
                    dao.save(trade);
                    int finalAmount = amount;
                    seller.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSellRemain(), new HashMap<String, String>() {{
                        put("number", finalAmount + "");
                        put("currency", currency + "");
                        put("price", price + "");
                    }}));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            callback.onDone(true);
            return;
        }
    }

    /**
     * 出售入他人库
     *
     * @param seller
     * @param item
     * @param buyer
     * @param tradeNumber
     */
    private void pay(Player seller, ItemStack item, String buyer, Integer tradeNumber) {
        try {
            ItemUtil.removeItem(seller, item, tradeNumber);
            StockStorage storage = new StockStorage();
            storage.setName(buyer);
            storage.setNumber(tradeNumber);
            storage.setSource(seller.getName());
            storage.setItem(item.getType().name());
            storage.setHash(getSubType(item));

            dao.save(storage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 购买入库
     *
     * @param type
     * @param subtype
     * @param source
     * @param number
     * @param player
     */
    public void gain(String type, String subtype, String source, Integer number, String player) {
        StockStorage storage = new StockStorage();
        storage.setItem(type);
        storage.setHash(subtype);
        storage.setName(player);
        storage.setNumber(number);
        storage.setSource(source);
        dao.save(storage);
    }

    public List<StockStorage> getStorage(Player player) {
        return dao.getStorage(player);
    }

    public void delete(Object o) {
        dao.delete(o);
    }

    public List<StockTrade> getTrades(Player player) {
        return dao.getTrades(player);
    }

    public void cancelTrade(Player player, StockTrade trade) {
        if (trade.getSell()) {
            gain(trade.getItem(), trade.getHash(), "refund", trade.getTradeNumber(), player.getName());
        } else {
            int total = trade.getPrice() * trade.getTradeNumber();
            OperateResult result = BankService.transferTo(SHOP_ACCOUNT, player.getName(), trade.getCurrency(), new BigDecimal(total), TxTypeEnum.SHOP_TRADE_OUT, String.format("refound %s %s with %s %s", total, trade.getItem() + trade.getHash(), trade.getPrice(), trade.getCurrency()));
            if (!result.getSuccess()) {
                player.sendMessage("error:" + result.getReason());
                plugin.getLogger().warning("玩家退款不成功！" + JSON.toJSONString(trade));
                return;
            }
        }
    }

    public void openIndex(Player player, Sign sign, String currency, ItemStack itemStack) {
        if (StringUtil.isEmpty(sign.getLine(2))) {
            Menu shop = new Menu(plugin, player, currency, itemStack, sign);
            player.openInventory(shop.getInventory());
        } else {
            Shop shop = new Shop(plugin, player, currency, itemStack, sign, Shop.ShopMode.SIMPLE);
            player.openInventory(shop.getInventory());
        }
    }

    public StockTrade getTradeById(StockTrade trade) {
        return dao.getTrade(trade.getId());
    }
}
