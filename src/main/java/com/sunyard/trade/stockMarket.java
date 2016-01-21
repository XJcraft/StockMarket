package com.sunyard.trade;

import com.sunyard.blockFML.blockFMLListener;
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
public class stockMarket extends JavaPlugin {


    public final String prefix = getConfig().getString("shop.sql.prefix");
    //TODO add dynamic prefix for sql tables
    Plugin plugin;
    Connection connection = null;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;


        setupConfig();
        setupListeners();
        setupCommand();
        setupDatabase();
        getLogger().info(getConfig().getString("message.enable"));
    }

    private void setupListeners() {
        if (plugin.getConfig().getBoolean("shop.enabled")) {
            getServer().getPluginManager().registerEvents(new stockMarketListener(this), this);
        }
        if (plugin.getConfig().getBoolean("blockFML")) {
            getServer().getPluginManager().registerEvents(new blockFMLListener(this), this);
        }
    }

    private void setupCommand() {
        getCommand("trade").setExecutor(new stockMarketCommandExecutor(this));
    }

    private void setupConfig() {
        saveDefaultConfig();
        YamlConfiguration conf = (YamlConfiguration) getConfig();
    }

    private void setupDatabase() {
        try {
            getLogger().info("Trying to enable database...");
            getDatabase().find(Trade.class).findRowCount();
            getLogger().info("Database enable successful!");
        } catch (Exception e) {
            getLogger().info("Fail to enable database, trying to initialize...");
            try {
                installDDL();
            } catch (Exception e2) {
                getLogger().warning("Fail to create database structure, please make sure the clear of database!");
                e2.printStackTrace();
            }
            getLogger().info("Successful import database structure.");
        }

        //TODO
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Trade.class);
        return list;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        saveConfig();
        getLogger().info(getConfig().getString("message.disable"));
    }

}
