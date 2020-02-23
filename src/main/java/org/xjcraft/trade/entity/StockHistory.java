package org.xjcraft.trade.entity;

import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.Index;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by Ree on 2016/1/25.
 */
@Entity
@Data
@NoArgsConstructor
//@UniqueConstraint(columnNames = {"item", "hash"})
public class StockHistory {
    public StockHistory(String seller, String buyer, String item, String hash, Integer price, String currency, Integer number) {
        this.seller = seller;
        this.buyer = buyer;
        this.item = item;
        this.hash = hash;
        this.price = price;
        this.currency = currency;
        this.number = number;
    }

    @Id
    int id;
    @Index
    String seller;
    @Index
    String buyer;
    String item;
    String hash;

    Integer price;

    String currency;

    Integer number;

    @CreatedTimestamp
    Timestamp createTime;

    public HashMap<String, String> fetchPlaceHolder() {
        HashMap<String, String> map = new HashMap<>();
        map.put("seller", seller);
        map.put("buyer", buyer);
        map.put("type", item);
        map.put("subtype", hash);
        map.put("currency", currency);
        map.put("price", price + "");
        map.put("number", number + "");
        return map;
    }


}
