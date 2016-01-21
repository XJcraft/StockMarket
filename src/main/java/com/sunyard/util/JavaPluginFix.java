package com.sunyard.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

/**
 * 修复Bukkit自带配置文件编码问题
 * 统一使用UTF-8
 *
 * @author jimliang
 */
public class JavaPluginFix extends JavaPlugin {

    private FileConfiguration newConfig;

    public FileConfiguration getConfig() {
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }

    @Override
    public void reloadConfig() {
        newConfig = YamlFix.loadConfigurationFix(getConfigFile());

        InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlFix
                    .loadConfigurationFix(defConfigStream);
            newConfig.setDefaults(defConfig);
        }
    }

    protected File getConfigFile() {
        return new File(getDataFolder(), "config.yml");
    }
}