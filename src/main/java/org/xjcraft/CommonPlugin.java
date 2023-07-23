package org.xjcraft;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import java.io.IOException;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class CommonPlugin extends JavaPlugin {
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
            if (command != null && !command.isEmpty()) {
                PluginCommand pluginCommand = getCommand(command);
                if (pluginCommand != null) {
                    pluginCommand.setExecutor(executor);
                } else {
                    // 添加日志输出，提示找不到对应的命令
                    System.out.println("Command not found: " + command);
                }
            }
        }
    }

    protected HikariDataSource getDataSource(String name) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find JDBC driver", e);
        }
    
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.getConfig().getString(name + ".jdbcUrl"));
        config.setUsername(this.getConfig().getString(name + ".username"));
        config.setPassword(this.getConfig().getString(name + ".password"));
        config.addDataSourceProperty("cachePrepStmts", this.getConfig().getBoolean(name + ".cachePrepStmts", true));
        config.addDataSourceProperty("prepStmtCacheSize", this.getConfig().getInt(name + ".prepStmtCacheSize", 250));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", this.getConfig().getInt(name + ".prepStmtCacheSqlLimit", 2048));
        config.setAutoCommit(false);
        return new HikariDataSource(config);
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
