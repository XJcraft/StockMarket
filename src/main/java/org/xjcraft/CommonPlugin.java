package org.xjcraft;

import org.xjcraft.trade.entity.StockHistory;
import org.xjcraft.trade.entity.StockStorage;
import org.xjcraft.trade.entity.StockTrade;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import java.io.IOException;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.datasource.DataSourceConfig;

public abstract class CommonPlugin extends JavaPlugin {
    private EbeanServer ebeanServer;
    private FileConfiguration config;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    protected void registerListener(Listener listener) {
        // 注册监听器
        getServer().getPluginManager().registerEvents(listener, this);
    }

    protected void registerCommand(CommonCommandExecutor executor) {
        // 注册命令
        for (String command : executor.getCommands().keySet()) {
            getCommand(command).setExecutor(executor);
        }
    }
    }

    protected EbeanServer getEbeanServer(String dbName) {
        // 获取 EbeanServer 实例
        // 填连接属性
        ServerConfig config = new ServerConfig();
        config.setName(dbName);
        config.setDataSourceConfig(new DataSourceConfig());
        config.getDataSourceConfig().setUrl(this.getConfig().getString(dbName + ".url"));
        config.getDataSourceConfig().setUsername(this.getConfig().getString(dbName + ".username"));
        config.getDataSourceConfig().setPassword(this.getConfig().getString(dbName + ".password"));

        // 填被 EbeanServer 管理的实体类
        config.addClass(StockHistory.class);
        config.addClass(StockStorage.class);
        config.addClass(StockTrade.class);


        ebeanServer = EbeanServerFactory.create(config);
        return ebeanServer;
    }

    public void saveConfig(Class<?> configClass) {
        // 根据配置类获取对应的配置文件
        File configFile = new File(this.getDataFolder(), configClass.getSimpleName() + ".yml");
        // 加载配置文件为 YamlConfiguration 对象
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
    
        // 假设你的配置类有一些静态字段需要保存
        // 获取配置类中声明的所有字段
        Field[] fields = configClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                try {
                    // 如果字段是公共的
                    // 将字段名和字段值保存到配置文件中
                    configuration.set(field.getName(), field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    
        try {
            // 将配置保存到文件
            configuration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    protected void loadConfigs() {
        // 加载配置文件
        // 从 .jar 文件中加载默认配置
        this.saveDefaultConfig();

        // 获取 config.yml 文件
        config = this.getConfig();

        // 使用 config.get 来获取配置值
    }
}
