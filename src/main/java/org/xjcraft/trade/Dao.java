package org.xjcraft.trade;

import io.ebean.EbeanServer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.xjcraft.trade.entity.CustomItem;
import org.xjcraft.trade.entity.Storage;
import org.xjcraft.trade.entity.Trade;

import java.util.List;

/**
 * Created by Ree on 2016/1/21.
 */
public class Dao {
    @Getter
    @Setter
    private EbeanServer ebeanServer;

    public Dao(EbeanServer db) {
        this.ebeanServer = db;
    }


    public List<Storage> getStorage(Player player) {
        return ebeanServer.find(Storage.class).where().eq("playername", player.getName()).orderBy().asc("id").findList();
    }

    public CustomItem getCustomItem(String name) {
        return ebeanServer.find(CustomItem.class).where().eq("name", name).findOne();
    }

    public List<Trade> getTrades(Player player) {
        return ebeanServer.find(Trade.class).where().eq("player", player.getName()).orderBy().asc("id").findList();
    }

    public List<CustomItem> getSpecials(String name) {
        return ebeanServer.find(CustomItem.class).where().eq("meta", name).findList();
    }

    public void save(Object o) {
        ebeanServer.save(o);
    }
}
