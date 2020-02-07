package org.xjcraft.trade;

public class StockMarketManager {
    private StockMarket plugin;
    private Dao dao;

    public StockMarketManager(StockMarket stockMarket, Dao dao) {
        plugin = stockMarket;
        this.dao = dao;
    }
}
