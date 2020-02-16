package org.xjcraft.trade;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.zjyl1994.minecraftplugin.multicurrency.services.BankService;
import com.zjyl1994.minecraftplugin.multicurrency.utils.OperateResult;
import com.zjyl1994.minecraftplugin.multicurrency.utils.TxTypeEnum;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.bean.StockCustomItem;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.entity.StockStorage;
import org.xjcraft.trade.entity.StockTrade;
import org.xjcraft.trade.gui.Callback;
import org.xjcraft.trade.utils.ItemUtil;
import org.xjcraft.utils.StringUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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

    public List<StockTrade> getTrades(String currency, ItemStack item) {
        return null;
    }

    Cache<ItemStack, String> build = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public String getTranslate(ItemStack itemStack) {
        return map.getOrDefault("item." + itemStack.getType().getKey().getNamespace() + "." + itemStack.getType().getKey().getKey(), itemStack.getType().getKey().getKey());
    }

    public String getLabel(ItemStack item) {
        System.out.println(item.getItemMeta().hashCode());
        try {
            return build.get(item, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (!item.hasItemMeta()) {
                        String[] name = {"N", item.getType().name()};
                        return StringUtil.join(name, ";");
                    } else {
                        try {
                            ItemMeta itemMeta = item.getItemMeta();
                            Class<? extends ItemMeta> aClass = itemMeta.getClass();
                            String className = aClass.getName().replaceAll("org\\.bukkit\\.craftbukkit\\..*?\\.inventory\\.", "");
//                Method method = aClass.getSuperclass().getClasses()[0].getDeclaredMethod("deserialize", (Class<Map<String, Object>>) (Class) Map.class);
                            Map<Integer, ItemMeta> specials = dao.getSpecials(className);
                            for (Map.Entry<Integer, ItemMeta> entry : specials.entrySet()) {
                                boolean equals = entry.getValue().equals(itemMeta);
                                if (equals) {
                                    String[] name = {"S", entry.getKey() + ""};
                                    return StringUtil.join(name, ";");
                                }
                            }
                            StockCustomItem stockCustomItem = new StockCustomItem();
                            stockCustomItem.setId(itemMeta.hashCode());
                            stockCustomItem.setMeta(className);
                            stockCustomItem.setFlatItem(itemMeta);
                            dao.save(stockCustomItem);

                            String[] name = {"S", item.getType().name(), stockCustomItem.getId() + ""};
                            return StringUtil.join(name, ";");
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

    public List<StockTrade> getSells(String currency, String item) {
        return dao.getSells(currency, item);

    }

    public List<StockTrade> getBuys(String currency, String item) {
        return dao.getBuys(currency, item);
    }

    public void buy(Player player, String currency, ItemStack itemStack, Integer price, Integer amount, Callback callback) {
        String item = getLabel(itemStack);
        synchronized (this) {
            Integer total = price * amount;
            OperateResult operateResult = BankService.queryCurrencyBalance(player.getName(), currency);
            BigDecimal data = (BigDecimal) operateResult.getData();
            if (data.intValue() < total) {
                player.sendMessage(MessageConfig.config.getMoreMoney());
                callback.onDone(false);
                return;
            }

            List<StockTrade> sells = this.getSells(currency, item);
            for (StockTrade sell : sells) {
                if (sell.getPrice() <= price) {
                    //direct buy
                    if (sell.getTradeNumber() <= amount) {
                        OperateResult result = BankService.transferTo(player.getName(), sell.getPlayer(), currency, new BigDecimal((sell.getTradeNumber() * sell.getPrice())), TxTypeEnum.SHOP_TRADE_IN, String.format("buy %s %s with %s %s", amount, item, price, currency));
                        if (!result.getSuccess()) {
                            player.sendMessage("error:" + operateResult.getReason());
                            callback.onDone(false);
                            return;
                        }
                        amount -= sell.getTradeNumber();
                        dao.delete(sell);
                        gain(item, sell.getPlayer(), sell.getTradeNumber(), player.getName());
                    } else {
                        OperateResult result = BankService.transferTo(player.getName(), sell.getPlayer(), currency, new BigDecimal((amount * sell.getPrice())), TxTypeEnum.SHOP_TRADE_IN, String.format("buy %s %s with %s %s", amount, item, price, currency));
                        if (!result.getSuccess()) {
                            player.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        sell.setTradeNumber(sell.getTradeNumber() - amount);
                        amount = 0;
                        dao.save(sell);
                        gain(item, sell.getPlayer(), amount, player.getName());
                        break;
                    }
                }
            }
            if (amount > 0) {
                //put buy order
                OperateResult result = BankService.transferTo(player.getName(), SHOP_ACCOUNT, currency, new BigDecimal(total), TxTypeEnum.SHOP_TRADE_IN, String.format("buy %s %s with %s %s", total, item, price, currency));
                if (!result.getSuccess()) {
                    player.sendMessage("error:" + result.getReason());
                    callback.onDone(false);
                    return;
                }
                StockTrade trade = new StockTrade();

                trade.setItem(item);
                trade.setCurrency(currency);
                trade.setPrice(price);
                trade.setSell(false);
                trade.setPlayer(player.getName());
                trade.setTradeNumber(amount);
                dao.save(trade);

            }
            callback.onDone(true);
            return;
        }
    }

    public void sell(Player player, String currency, ItemStack itemStack, int price, int amount, Callback callback) {
        String item = getLabel(itemStack);
        synchronized (this) {
            int itemNumber = ItemUtil.getItemNumber(player, itemStack);
            if (itemNumber < amount) {
                player.sendMessage(MessageConfig.config.getMoreItem());
                callback.onDone(false);
                return;
            }
            List<StockTrade> buys = this.getBuys(currency, item);
            for (StockTrade buy : buys) {
                if (buy.getPrice() >= price) {
                    //direct sell with higher price
                    if (buy.getTradeNumber() < amount) {
                        OperateResult result = BankService.transferTo(SHOP_ACCOUNT, player.getName(), currency, new BigDecimal((buy.getTradeNumber() * buy.getPrice())), TxTypeEnum.SHOP_TRADE_OUT, String.format("buy %s %s with %s %s", amount, item, price, currency));
                        if (!result.getSuccess()) {
                            player.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        amount -= buy.getTradeNumber();
                        dao.delete(buy);
                        pay(player, itemStack, buy.getPlayer(), buy.getTradeNumber());
                    } else {
                        OperateResult result = BankService.transferTo(player.getName(), buy.getPlayer(), currency, new BigDecimal((amount * buy.getPrice())), TxTypeEnum.SHOP_TRADE_OUT, String.format("buy %s %s with %s %s", amount, item, price, currency));
                        if (!result.getSuccess()) {
                            player.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        buy.setTradeNumber(buy.getTradeNumber() - amount);
                        amount = 0;
                        dao.save(buy);
                        pay(player, itemStack, buy.getPlayer(), amount);
                        break;
                    }
                }
            }
            if (amount > 0) {
                //put buy order
                try {
                    ItemUtil.removeItem(player, itemStack, amount);
                    StockTrade trade = new StockTrade();

                    trade.setItem(item);
                    trade.setCurrency(currency);
                    trade.setPrice(price);
                    trade.setSell(true);
                    trade.setPlayer(player.getName());
                    trade.setTradeNumber(amount);
                    dao.save(trade);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            callback.onDone(true);
            return;
        }
    }

    private void pay(Player player, ItemStack item, String buyer, Integer tradeNumber) {
        try {
            ItemUtil.removeItem(player, item, tradeNumber);
            StockStorage storage = new StockStorage();
            storage.setName(buyer);
            storage.setNumber(tradeNumber);
            storage.setSource(player.getName());
            storage.setItem(getLabel(item));
            dao.save(storage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gain(String item, String source, Integer number, String player) {
        StockStorage storage = new StockStorage();
        storage.setItem(item);
        storage.setName(player);
        storage.setNumber(number);
        storage.setSource(source);
        dao.save(storage);
    }
}
