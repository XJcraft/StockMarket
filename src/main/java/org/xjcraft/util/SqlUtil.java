package org.xjcraft.util;

import com.avaje.ebean.EbeanServer;
import org.xjcraft.database.Trade;

import java.util.List;

/**
 * Created by Weiyuan on 2016/1/21.
 */
public class SqlUtil {
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

    public static void setEbeanServer(EbeanServer ebeanServer) {
        SqlUtil.ebeanServer = ebeanServer;
    }
}
