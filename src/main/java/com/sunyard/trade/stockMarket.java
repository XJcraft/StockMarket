package com.sunyard.trade;

import com.sunyard.blockFML.BlockFMLListener;
import com.sunyard.database.Storage;
import com.sunyard.database.Trade;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiyuan on 2016/1/7.
 */
public class StockMarket extends JavaPlugin {


    public final String prefix = getConfig().getString("shop.sql.prefix");
    //TODO add dynamic prefix for sql tables
    Plugin plugin;
    Connection connection = null;
    boolean isEnabled = getConfig().getBoolean("shop.enable");

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        setupConfig();
        setupDatabase();
        setupListeners();
        setupCommand();
        getLogger().info(getConfig().getString("message.enable"));
    }

    private void setupListeners() {
        if (isEnabled) {
            getServer().getPluginManager().registerEvents(new StockMarketListener(this), this);
        }
        if (plugin.getConfig().getBoolean("blockFML")) {
            getServer().getPluginManager().registerEvents(new BlockFMLListener(this), this);
        }
    }

    private void setupCommand() {
        getCommand("trade").setExecutor(new StockMarketCommandExecutor(this));
    }

    private void setupConfig() {
        saveDefaultConfig();
        YamlConfiguration conf = (YamlConfiguration) getConfig();
        //TODO encode problems
    }

    private void setupDatabase() {
        try {
            getLogger().info(getConfig().getString("message.enableDB"));
            getDatabase().find(Trade.class).findRowCount();
            getDatabase().find(Storage.class).findRowCount();
            getLogger().info(getConfig().getString("message.DBenabled"));
        } catch (Exception e) {
            getLogger().info(getConfig().getString("message.DBdisabled"));
            try {
                installDDL();
                getLogger().info(getConfig().getString("message.DBpass"));
            } catch (Exception e2) {
                getLogger().warning(getConfig().getString("message.DBfail"));
                isEnabled = false;
                e2.printStackTrace();
            }
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(Trade.class);
        list.add(Storage.class);
        return list;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        saveConfig();
        getLogger().info(getConfig().getString("message.disable"));
    }
}
