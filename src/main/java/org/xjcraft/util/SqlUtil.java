package org.xjcraft.util;

import org.xjcraft.database.Trade;

import java.util.List;

/**
 * Created by Weiyuan on 2016/1/21.
 */
public class SqlUtil {

    public static Trade getFirst(List<Trade> list) {
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
