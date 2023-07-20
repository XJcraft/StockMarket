package org.xjcraft.trade;

import io.ebean.EbeanServer;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.xjcraft.CommonPlugin;
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.utils.SerializeUtil;

/**
 * Created by Ree on 2016/1/7.
 */
public class StockMarket extends CommonPlugin{
    Plugin plugin;
    @Getter
    StockMarketManager manager;

    @Override
    public void onEnable() {
        super.onEnable();
        this.plugin = this;
        SerializeUtil.plugin = this;
        loadConfigs();
        EbeanServer db = getEbeanServer(this.plugin.getName());
        Dao dao = new Dao(db, this);

        manager = new StockMarketManager(this, dao);
        setupListeners(manager);
        registerCommand(new StockMarketCommands(this, manager));
        getLogger().info("StockMarket has been enabled");
    }

    private void setupListeners(StockMarketManager manager) {
        if (Config.config.getShop_enable()) {
//            getServer().getPluginManager().registerEvents(new StockMarketListener(this,manager), this);
            getServer().getPluginManager().registerEvents(new StockMarketListener(this, manager), this);
        }

    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("StockMarket has been disabled");
    }
}
