package org.xjcraft.trade.entity;

import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.Index;
import io.ebean.annotation.UpdatedTimestamp;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Ree on 2016/1/20.
 */
@Data
@Entity
@Table(name = "StockMarket_trades")

public class Trade {

    @Id
    int id;

    String player;
    @Index
    Boolean sell;
    @Index
    String item;
    @Index
    String currency;

    Integer price;

    Integer tradeNumber;

    @CreatedTimestamp
    Timestamp createTime;
    @UpdatedTimestamp
    Timestamp updateTime;


}
