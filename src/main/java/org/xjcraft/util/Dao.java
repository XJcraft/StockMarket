package org.xjcraft.util;

import com.avaje.ebean.EbeanServer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.xjcraft.database.CustomItem;
import org.xjcraft.database.Storage;
import org.xjcraft.database.Trade;

import java.util.List;

/**
 * Created by Weiyuan on 2016/1/21.
 */
public class Dao {
    @Getter
    @Setter
    private static EbeanServer ebeanServer;

    public static Trade getFirst(List<Trade> list) {
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public static EbeanServer getEbeanServer() {
        return ebeanServer;
    }

    public static List<Storage> getStorage(Player player) {
        return ebeanServer.find(Storage.class).where().ieq("playername", player.getName()).orderBy().asc("id").findList();
    }

    public static CustomItem getCustomItem(String name) {
        return ebeanServer.find(CustomItem.class).where().ieq("name", name).findUnique();
    }

    public static List<Trade> getTrades(Player player) {
        return ebeanServer.find(Trade.class).where().ieq("player", player.getName()).orderBy().asc("id").findList();
    }

}
