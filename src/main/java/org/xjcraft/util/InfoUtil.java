package org.xjcraft.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weiyuan on 2016/1/27.
 */
public class InfoUtil {
	private static Map<String, String> tempSave = new HashMap<> ();

	public static String removeColor(String str) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("(?i)&[0-F]", "");
        return str;
    }

    public static boolean matchPattern(String input, String line) {
        return removeColor(line).equalsIgnoreCase(input);
    }

    public static String average(int itemP, int moneyP) {
        if (itemP > moneyP) {
            double price = (double) itemP / (double) moneyP;
            return String.format("%.3f", price) + ":1";
        } else {
            double price = (double) moneyP / (double) itemP;
            return "1:" + String.format("%.3f", price);
        }
	}

	public static void setName (Player player, String info) {
        if (tempSave.containsKey(player.getName())) {
            tempSave.remove(player.getName());
        }
        tempSave.put (player.getName (), info);
	}

	public static String getName (Player player) {
		String callback = tempSave.get (player.getName ());
		tempSave.remove (player.getName ());
		return callback;

	}
}
