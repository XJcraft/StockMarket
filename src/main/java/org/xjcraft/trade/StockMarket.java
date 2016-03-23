package org.xjcraft.trade;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.xjcraft.blockFML.BlockFMLListener;
import org.xjcraft.database.CustomItem;
import org.xjcraft.database.History;
import org.xjcraft.database.Storage;
import org.xjcraft.database.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiyuan on 2016/1/7.
 */
public class StockMarket extends JavaPlugin {
    // public final String prefix = getConfig().getString("shop.sql.prefix");
    // TODO add dynamic prefix for sql tables
    Plugin plugin;
    boolean isEnabled;

    @Override
    public void onEnable() {
        super.onEnable();
        this.plugin = this;
        setupConfig();
        setupDatabase();
        setupListeners();
        setupCommand();
        getLogger().info("StockMarket has been enabled");
    }

    private void setupListeners() {
        this.isEnabled = getConfig().getBoolean("shop.enable");
        if (this.isEnabled) {
            getServer().getPluginManager().registerEvents(new StockMarketListener(this), this);
        }
        if (this.plugin.getConfig().getBoolean("blockFML")) {
            getServer().getPluginManager().registerEvents(new BlockFMLListener(this), this);
        }
    }

    private void setupCommand() {
        getCommand("trade").setExecutor(new StockMarketCommandExecutor(this));
        getCommand("tr").setExecutor(new StockMarketCommandExecutor(this));
    }

    private void setupConfig() {
        saveDefaultConfig();
        getConfig();
        saveConfig();
        YamlConfiguration conf = (YamlConfiguration) getConfig();

    }

    private void setupDatabase() {
        try {
            getLogger().info("Trying to enable database...");
            getDatabase().find(Trade.class).findRowCount();
            getDatabase().find(Storage.class).findRowCount();
            getDatabase().find(History.class).findRowCount();
            getDatabase().find(CustomItem.class).findRowCount();
            getLogger().info("Database enable successful!");
        } catch (Exception e) {
            getLogger().info(getConfig().getString("Fail to enable database, trying to initialize..."));
            try {
                installDDL();
                getLogger().info("Successful import database structure.");
            } catch (Exception e2) {
                getLogger().warning("Fail to create database structure, please make sure the clear of database!");
                this.isEnabled = false;
                e2.printStackTrace();
            }
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(Trade.class);
        list.add(Storage.class);
        list.add(History.class);
        list.add(CustomItem.class);
        return list;
    }

    @Override
    public void onDisable() {
        super.onDisable();
//        saveConfig();
        getLogger().info("StockMarket has been disabled");
    }
}
