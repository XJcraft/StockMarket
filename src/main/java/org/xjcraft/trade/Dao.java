package org.xjcraft.trade;

import io.ebean.EbeanServer;
import io.ebean.ExpressionList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.bean.StockCustomItem;
import org.xjcraft.trade.config.SpecialItemConfig;
import org.xjcraft.trade.entity.StockStorage;
import org.xjcraft.trade.entity.StockTrade;

import java.util.List;
import java.util.Map;

/**
 * Created by Ree on 2016/1/21.
 */
public class Dao {
    @Getter
    @Setter
    private EbeanServer ebeanServer;
    private StockMarket plugin;

    public Dao(EbeanServer db, StockMarket plugin) {
        this.ebeanServer = db;
        this.plugin = plugin;
    }


    public List<StockStorage> getStorage(Player player) {
        return ebeanServer.find(StockStorage.class).where().eq("name", player.getName()).orderBy().asc("id").setMaxRows(53).findList();
    }

    public StockCustomItem getCustomItem(String name) {
        return ebeanServer.find(StockCustomItem.class).where().eq("name", name).findOne();
    }

    public List<StockTrade> getTrades(Player player) {
        return ebeanServer.find(StockTrade.class).where().eq("player", player.getName()).orderBy().asc("id").findList();
    }

    public Map<String, ItemMeta> getSpecials(String name) {
        return SpecialItemConfig.config.getOrDefault(name, new SpecialItemConfig()).getItemMetas();
    }

    public void save(StockCustomItem o) {
//        UUID uuid = UUID.randomUUID();
//        uuid = new UUID(0l,uuid.getLeastSignificantBits());
        SpecialItemConfig itemConfig = SpecialItemConfig.config.getOrDefault(o.getMeta(), new SpecialItemConfig());
        SpecialItemConfig.config.put(o.getMeta(), itemConfig);
        itemConfig.getItemMetas().put(o.getId() + "", o.getFlatItem());
//        o.setId(uuid);
        plugin.saveConfig(SpecialItemConfig.class);
    }

    public void save(Object o) {
        ebeanServer.save(o);
    }

    public List<StockTrade> getSells(String currency, String item, String subType) {
        ExpressionList<StockTrade> expression = ebeanServer.find(StockTrade.class).where().eq("currency", currency).eq("item", item);
        if (subType != null) {
            expression.eq("hash", subType);
        }
        return expression.eq("sell", true).orderBy().asc("price").findList();
    }

    public List<StockTrade> getBuys(String currency, String item, String subType) {
        ExpressionList<StockTrade> expression = ebeanServer.find(StockTrade.class).where().eq("currency", currency).eq("item", item);
        if (subType != null) {
            expression.eq("hash", subType);
        }
        return expression.eq("sell", false).orderBy().desc("price").findList();
    }

    public void delete(Object o) {
        ebeanServer.delete(o);
    }
}
