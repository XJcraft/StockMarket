package org.xjcraft.trade;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.xjcraft.blockFML.BlockFMLListener;
import org.xjcraft.database.CustomItem;
import org.xjcraft.database.History;
import org.xjcraft.database.Storage;
import org.xjcraft.database.Trade;
import org.xjcraft.util.SerializeUtil;

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
        SerializeUtil.plugin = this;
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
        ServerConfig config = new ServerConfig();
        config.setName("database");
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://192.168.245.132:3306/ree");
        dataSourceConfig.setDriver("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("ree");
        dataSourceConfig.setPassword("111");


        config.setDataSourceConfig(dataSourceConfig);
        config.setDdlGenerate(true);
        config.setDdlRun(true);
        EbeanServer server = EbeanServerFactory.create(config);
        try {
            getLogger().info("Trying to enable database...");
            server.find(CustomItem.class).findRowCount();
            server.find(History.class).findRowCount();
            server.find(Storage.class).findRowCount();
            server.find(Trade.class).findRowCount();
            getLogger().info("Database enable successful!");
        } catch (Exception e) {
            getLogger().info("Fail to enable database, trying to initialize...");
            try {

//                installDDL();
                getLogger().info("Successful import database structure.");
            } catch (Exception e2) {
                getLogger().warning("Fail to create database structure, please make sure the clear of database!");
                this.isEnabled = false;
                e2.printStackTrace();
            }
        }
    }

//    @Override
//    public List<Class<?>> getDatabaseClasses() {
//        List<Class<?>> list = new ArrayList<>();
//        list.add(Trade.class);
//        list.add(Storage.class);
//        list.add(History.class);
//        list.add(CustomItem.class);
//        return list;
//    }

    @Override
    public void onDisable() {
        super.onDisable();
        saveConfig();
        getLogger().info("StockMarket has been disabled");
    }
}
