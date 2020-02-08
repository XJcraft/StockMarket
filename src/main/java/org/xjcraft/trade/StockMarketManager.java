package org.xjcraft.trade;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.entity.CustomItem;
import org.xjcraft.trade.entity.Trade;
import org.xjcraft.utils.JSON;
import org.xjcraft.utils.StringUtil;

import java.util.List;

public class StockMarketManager {
    private StockMarket plugin;
    private Dao dao;

    public StockMarketManager(StockMarket stockMarket, Dao dao) {
        plugin = stockMarket;
        this.dao = dao;
    }

    public List<Trade> getTrades(String currency, ItemStack item) {
        return null;
    }

    public String getLabel(ItemStack item) {
        if (!item.hasItemMeta()) {
            String[] name = {"N", item.getType().name()};
            return StringUtil.join(name, ";");
        } else {
            ItemMeta itemMeta = item.getItemMeta();
            Class<? extends ItemMeta> aClass = itemMeta.getClass();
            String className = aClass.getName();
            List<CustomItem> specials = dao.getSpecials(className);
            for (CustomItem special : specials) {
                String flatItem = special.getFlatItem();
                ItemMeta itemMeta1 = JSON.parseJSON(flatItem, aClass);
                if (itemMeta.equals(itemMeta1)) {
                    String[] name = {"S", item.getType().name()};
                    return StringUtil.join(name, ";");
                }
            }
            CustomItem customItem = new CustomItem();
            customItem.setMeta(className);
            customItem.setFlatItem(JSON.toJSONString(itemMeta.serialize()));
            dao.save(customItem);

            String[] name = {"S", customItem.getId() + ""};
            return StringUtil.join(name, ";");
        }

    }

    public List<Trade> getSells(String currency, String item) {

        return null;
    }

    public List<Trade> getBuys(String currency, String item) {
        return null;
    }
}
