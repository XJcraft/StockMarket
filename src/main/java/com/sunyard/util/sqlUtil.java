package com.sunyard.util;

import com.sunyard.database.Trade;

import java.util.List;

/**
 * Created by Weiyuan on 2016/1/21.
 */
public class sqlUtil {

    public static Trade getFirst(List<Trade> list) {
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
