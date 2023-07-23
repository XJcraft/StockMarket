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
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.gui.Callback;
import org.xjcraft.trade.gui.Menu;
import org.xjcraft.trade.gui.Shop;
import org.xjcraft.trade.utils.ItemUtil;
import org.xjcraft.utils.JSON;
import org.xjcraft.trade.utils.StringUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

// StockMarketManager类是负责处理游戏内交易市场相关操作的核心类。
public class StockMarketManager {
    private StockMarket plugin;
    private DataSource hikari;
    private Dao dao;
    private final String SHOP_ACCOUNT = "$SHOP";
    private Map<String, String> map;
    
    public StockMarketManager(StockMarket stockMarket, DataSource hikari) {
        plugin = stockMarket;
        this.hikari = hikari;
        this.dao = new Dao(hikari,plugin);
        InputStream resource = plugin.getResource("zh_cn.json");
         // 使用Gson库将JSON文件解析为Map对象，用于物品名称的翻译
        map = new Gson().fromJson(new InputStreamReader(resource), Map.class);
    }

    // 建立一个基于Guava的缓存，主要用于存储物品和名称之间的关系
    // 该缓存的最大大小为1000，写入后的过期时间为10分钟
    Cache<ItemStack, String> build = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    /**
    * getTranslate方法用于获取给定物品的中文翻译名称
    * @param itemStack 要查询的物品
    * @return 返回物品的翻译名称
    */
    public String getTranslate(ItemStack itemStack) {
        // 如果物品的元数据（如自定义名称）不为空，那么返回该自定义名称
        if (itemStack.getItemMeta() != null && !StringUtil.isEmpty(itemStack.getItemMeta().getDisplayName())) {
            return itemStack.getItemMeta().getDisplayName();
        }
         // 如果没有自定义名称，则从map中获取物品的默认翻译名称
        return map.getOrDefault("block." + itemStack.getType().getKey().getNamespace() + "." + itemStack.getType().getKey().getKey(), map.getOrDefault("item." + itemStack.getType().getKey().getNamespace() + "." + itemStack.getType().getKey().getKey(), itemStack.getType().getKey().getKey()));
    }

    /**
     * getMeta方法根据物品和hashcode来获取物品的meta信息
     * @param itemStack 目标物品
     * @param hashcode 该物品的hashcode
     * @return 返回物品的元数据
     */
    public ItemMeta getMeta(ItemStack itemStack, String hashcode) {
        // 获取物品的元数据
        ItemMeta itemMeta = itemStack.getItemMeta();
        // 获取元数据的类对象，然后获取类名
        Class<? extends ItemMeta> aClass = itemMeta.getClass();
        String className = aClass.getName().replaceAll("org\\.bukkit\\.craftbukkit\\..*?\\.inventory\\.", "");
        // 从数据库中获取特定类名的所有元数据
        Map<String, ItemMeta> specials = dao.getSpecials(className);
        // 根据hashcode返回对应的元数据
        return specials.get(hashcode);
    }

    /**
     * getItemStack方法根据物品的类型和hashcode来获取实际的物品
     * @param type 物品的类型
     * @param hashcode 物品的hashcode
     * @return 返回对应的物品
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
     * 获取或生成新的hashcode
     * 这个方法主要用于获取给定物品的子类型。
     * 如果物品已经有子类型（即特殊的元数据），它将从缓存或数据库中获取这个子类型。
     * 如果物品没有子类型，将返回一个空字符串。
     *
     * @param item 物品
     * @return 物品的子类型，如果没有子类型则返回空字符串
     */
    public String getSubType(ItemStack item) {
    // System.out.println(item.getItemMeta().hashCode());
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
                            // StockCustomItem stockCustomItem = new StockCustomItem();
                            // stockCustomItem.setId(hashcode);
                            // stockCustomItem.setMeta(className);
                            // stockCustomItem.setFlatItem(itemMeta);
                            dao.save(hashcode,className,itemMeta);
                            return hashcode + "";
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

    public List<Map<String, Object>> getSells(String currency, String type, String subType) {
        return dao.getSells(currency, type, subType);
    }

    public List<Map<String, Object>> getBuys(String currency, String itemLabel, String itemHash) {
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

            List<Map<String, Object>> sells = this.getSells(currency, type, subType);
            for (Map<String, Object> sell : sells) {
                if ((int) sell.get("price") <= price) {
                    //direct buy
                    if ((int) sell.get("trade_number") <= amount) {
                        OperateResult result = BankService.transferTo(buyer.getName(), (String) sell.get("player"), currency, new BigDecimal(((int) sell.get("trade_number") * (int) sell.get("price"))), TxTypeEnum.SHOP_TRADE_OUT, String.format("buy %s %s %s with %s %s each", sell.get("trade_number"), type, subType, sell.get("price"), currency));
                        if (!result.getSuccess()) {
                            buyer.sendMessage("error:" + operateResult.getReason());
                            callback.onDone(false);
                            return;
                        }
                        amount -= (int) sell.get("trade_number");
                        dao.processSells(buyer,sell);
                        gain(type, subType,(String) sell.get("player"),(Integer) sell.get("trade_number"), buyer.getName());
                        buyer.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getBuyHint(), dao.fetchPlaceHolder(buyer,sell)));
                    } else {
                        OperateResult result = BankService.transferTo(buyer.getName(), (String) sell.get("player"), currency, new BigDecimal((amount * (int) sell.get("price"))), TxTypeEnum.SHOP_TRADE_OUT, String.format("buy %s %s %s with %s %s each", amount, type, subType, (int) sell.get("price"), currency));
                        if (!result.getSuccess()) {
                            buyer.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }

                        dao.setTrade((int) sell.get("id"),"trade_number",String.valueOf((int) sell.get("trade_number") - amount));
                        dao.processSells(buyer,sell,amount);

                        gain(type, subType, (String) sell.get("player"), amount, buyer.getName());
                        amount = 0;
                        buyer.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getBuyHint(), dao.fetchPlaceHolder(buyer,sell)));
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

                dao.setTrade(type,subType,currency,price,false,buyer,amount);

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
            //这段是老是提示没有足够物品的核心原因，先放着，明天再说吧
            int itemNumber = ItemUtil.getItemNumber(seller, itemStack);
            if (itemNumber < amount) {
                seller.sendMessage(MessageConfig.config.getMoreItem());
                callback.onDone(false);
                return;
            }
            List<Map<String, Object>> buys = this.getBuys(currency, type, subType);
            for (Map<String, Object> buy : buys) {
                if ((int) buy.get("price") >= price) {
                    //direct sell with higher price
                    if ((int) buy.get("trade_number") <= amount) {
                        OperateResult result = BankService.transferTo(SHOP_ACCOUNT, seller.getName(), currency, new BigDecimal(((int) buy.get("trade_number") * (int) buy.get("price"))), TxTypeEnum.SHOP_TRADE_OUT, String.format("sell %s %s %s with %s %s each", buy.get("trade_number"), type, subType, buy.get("price"), currency));
                        if (!result.getSuccess()) {
                            seller.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        amount -= (int) buy.get("trade_number");

                        dao.processSells(seller,buy);

                        pay(seller, itemStack, (String) buy.get("player"), (int) buy.get("trade_number"));
                        seller.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSellHint(), dao.fetchPlaceHolder(seller,buy)));
                    } else {
                        OperateResult result = BankService.transferTo(SHOP_ACCOUNT, seller.getName(), currency, new BigDecimal((amount * (int) buy.get("price"))), TxTypeEnum.SHOP_TRADE_OUT, String.format("sell %s %s %s with %s %s each", amount, type, subType, buy.get("price"), currency));
                        if (!result.getSuccess()) {
                            seller.sendMessage("error:" + result.getReason());
                            callback.onDone(false);
                            return;
                        }
                        dao.setTrade((int) buy.get("id"),"trade_number",String.valueOf((int) buy.get("trade_number") - amount));
                        dao.processSells(seller,buy,amount);

                        pay(seller, itemStack, (String) buy.get("player"), amount);
                        amount = 0;
                        seller.sendMessage(StringUtil.applyPlaceHolder(MessageConfig.config.getSellHint(), dao.fetchPlaceHolder(seller,buy)));
                        break;
                    }
                }
            }
            if (amount > 0) {
                //put buy order
                try {
                    ItemUtil.removeItem(seller, itemStack, amount);

                    dao.setTrade(type,subType,currency,price,false,seller,amount);

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
            dao.setStorage(buyer, tradeNumber, seller, buyer, buyer);

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
        String sql = "INSERT INTO stock_storage (item, hash, name, number, source) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = hikari.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, subtype);
            preparedStatement.setString(3, player);
            preparedStatement.setInt(4, number);
            preparedStatement.setString(5, source);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getStorage(Player player) {
        return dao.getStorage(player);
    }

    public List<Map<String, Object>> getTrades(Player player) {
        return dao.getTrades(player);
    }

    public void cancelTrade(Player player, Map<String, Object> trade) {
        if ((boolean) trade.get("sell")) {
            gain((String) trade.get("item"), (String) trade.get("hash"), "refund", (Integer) trade.get("trade_number"), player.getName());
        } else {
            int total =(int) trade.get("price") * (int) trade.get("trade_number");
            OperateResult result = BankService.transferTo(SHOP_ACCOUNT, player.getName(),(String) trade.get("currency"), new BigDecimal(total), TxTypeEnum.SHOP_TRADE_OUT, String.format("refound %s %s with %s %s", total,(String) trade.get("item") + (String) trade.get("hash"), trade.get("price"), trade.get("currency")));
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

    // public Map<String, Object> getTradeById(Map<String, Object> trade) {
    //     return dao.getTrade((int) trade.get("id"));
    // }

    public void delete(String field,int id){
        dao.delete(field,id);
    }
}
