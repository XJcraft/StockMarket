package org.xjcraft.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Weiyuan on 2016/5/18.
 */
public class SerializeUtil {
    public static Plugin plugin;

    public static String serialization(ItemStack item) {
        return serialize3(item);
    }

    public static ItemStack deSerialization(String data) {
        return deSerialize3(data);
    }

    private static String serialize1(ItemStack item) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    private static ItemStack deSerialize1(String data) {
        ItemStack item = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            item = (ItemStack) dataInput.readObject();
            dataInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return item;
    }


    private static String serialize3(ItemStack item) {
        plugin.getConfig().set("item." + item.hashCode(), item);
        return "item." + item.hashCode();
    }

    private static ItemStack deSerialize3(String data) {
        return plugin.getConfig().getItemStack(data);
    }
}
