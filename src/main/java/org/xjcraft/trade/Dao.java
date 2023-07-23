package org.xjcraft.trade;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.config.SpecialItemConfig;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.ResultSetMetaData;

import javax.sql.DataSource;

/**
 * Created by Ree on 2016/1/21.
 */
public class Dao {
    @Getter
    @Setter
    private DataSource hikari;
    private StockMarket plugin;

    public Dao(DataSource db, StockMarket plugin) {
        this.hikari = db;
        this.plugin = plugin;
    }

    public List<Map<String, Object>> getStorage(Player player) {
        String sql = "SELECT * FROM stock_storage WHERE name = ? ORDER BY id ASC LIMIT 53";
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection connection = hikari.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, player.getName());
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public void setStorage(String name,Integer number,Player source,String item,String hash) {
        String sql = "INSERT INTO stock_storage (name, number, source, item, hash) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        try{
            connection = hikari.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, number);
                preparedStatement.setString(3, source.getName());
                preparedStatement.setString(4, item);
                preparedStatement.setString(5, hash);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public ResultSet getCustomItem(String name) {
        String sql = "SELECT * FROM stock_custom_item WHERE meta = ?";
        try (Connection connection = hikari.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> getTrades(Player player) {
        String sql = "SELECT * FROM stock_trade WHERE player = ?";
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection connection = hikari.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, player.getName());
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public Map<String, ItemMeta> getSpecials(String name) {
        return SpecialItemConfig.config.getOrDefault(name, new SpecialItemConfig()).getItemMetas();
    }

    public void save(Integer hashcode, String className, ItemMeta itemMeta) {
        SpecialItemConfig itemConfig = SpecialItemConfig.config.getOrDefault(className, new SpecialItemConfig());
        SpecialItemConfig.config.put(className, itemConfig);
        itemConfig.getItemMetas().put(hashcode + "", itemMeta);
        // o.setId(uuid);
        plugin.saveConfig(SpecialItemConfig.class);
    }

    public List<Map<String, Object>> getSells(String currency, String item, String subType) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "SELECT * FROM stock_trade WHERE currency = ? AND item = ? ";
        if (subType != null) {
            sql += "AND hash = ? ";
        }
        sql += "AND sell = TRUE ORDER BY price ASC";
        try (Connection connection = hikari.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, currency);
            preparedStatement.setString(2, item);
            if (subType != null) {
                preparedStatement.setString(3, subType);
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<Map<String, Object>> getBuys(String currency, String item, String subType) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "SELECT * FROM stock_trade WHERE currency = ? AND item = ? ";
        if (subType != null) {
            sql += "AND hash = ? ";
        }
        sql += "AND sell = FALSE ORDER BY price DESC";
        try (Connection connection = hikari.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, currency);
            preparedStatement.setString(2, item);
            if (subType != null) {
                preparedStatement.setString(3, subType);
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public Map<String, Object> getTrade(int id) {
        String sql = "SELECT * FROM stock_trade WHERE id = ? LIMIT 1";
        Map<String, Object> map = new HashMap<>();
        try (Connection connection = hikari.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public void setTrade(int id,String field,String revise) {
        String sql = "UPDATE stock_trade SET " + field + " = ? WHERE id = ?";
        Connection connection = null;
        try{
            connection = hikari.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, revise); 
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void setTrade(String type,String subType,String currency,Integer price,boolean sell,Player buyer,Integer amount) {
        String sql = "INSERT INTO stock_trade (item, hash, currency, price, sell, player, trade_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        try{
            connection = hikari.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, type);
                preparedStatement.setString(2, subType);
                preparedStatement.setString(3, currency);
                preparedStatement.setInt(4, price);
                preparedStatement.setBoolean(5, sell);
                preparedStatement.setString(6, buyer.getName());
                preparedStatement.setInt(7, amount);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void processSells(Player buyer, Map<String, Object> sell) {
        String insertHistorySql = "INSERT INTO stock_history (buyer, seller, item, hash, price, currency, trade_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String deleteSellSql = "DELETE FROM stock_trade WHERE id = ?";
        Connection connection = null;

        try {
            connection = hikari.getConnection();
            try (PreparedStatement insertHistoryStmt = connection.prepareStatement(insertHistorySql)) {
                insertHistoryStmt.setString(1, buyer.getName());
                insertHistoryStmt.setString(2, (String) sell.get("player"));
                insertHistoryStmt.setString(3, (String) sell.get("item"));
                insertHistoryStmt.setString(4, (String) sell.get("hash"));
                insertHistoryStmt.setInt(5, (Integer) sell.get("price"));
                insertHistoryStmt.setString(6, (String) sell.get("currency"));
                insertHistoryStmt.setInt(7, (Integer) sell.get("trade_number"));
                insertHistoryStmt.executeUpdate();
            }
            // 删除原有的 sell 记录
            try (PreparedStatement deleteSellStmt = connection.prepareStatement(deleteSellSql)) {
                deleteSellStmt.setInt(1, (Integer) sell.get("id"));
                deleteSellStmt.executeUpdate();
            }
            connection.commit(); // 提交事务
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // 在出错时回滚事务
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void processSells(Player buyer, Map<String, Object> sell,Integer amount) {
        String insertHistorySql = "INSERT INTO stock_history (buyer, seller, item, hash, price, currency, trade_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;

        try {
            connection = hikari.getConnection();
            try (PreparedStatement insertHistoryStmt = connection.prepareStatement(insertHistorySql)) {
                insertHistoryStmt.setString(1, buyer.getName());
                insertHistoryStmt.setString(2, (String) sell.get("player"));
                insertHistoryStmt.setString(3, (String) sell.get("item"));
                insertHistoryStmt.setString(4, (String) sell.get("hash"));
                insertHistoryStmt.setInt(5, (Integer) sell.get("price"));
                insertHistoryStmt.setString(6, (String) sell.get("currency"));
                insertHistoryStmt.setInt(7,amount);
                insertHistoryStmt.executeUpdate();
            }
            connection.commit(); // 提交事务
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // 在出错时回滚事务
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }    

    public HashMap<String, String> fetchPlaceHolder(Player buyer,Map<String, Object> sell) {
        HashMap<String, String> map = new HashMap<>();
        map.put("buyer", buyer.getName());
        map.put("seller", (String) sell.get("player"));
        map.put("type", (String) sell.get("item"));
        map.put("subtype", (String) sell.get("hash"));
        map.put("currency", (String) sell.get("currency"));
        map.put("price", String.valueOf(sell.get("price")));
        map.put("number", String.valueOf(sell.get("trade_number")));
        return map;
    }

    public void delete(String field,int id){
       String deleteSellSql = "DELETE FROM "+field+" WHERE id = ?";
        Connection connection = null;
        try{
            connection = hikari.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSellSql)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
