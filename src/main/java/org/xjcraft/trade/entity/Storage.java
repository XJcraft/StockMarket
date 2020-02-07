package org.xjcraft.trade.entity;

import io.ebean.annotation.CreatedTimestamp;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Ree on 2016/1/21.
 */
@Entity
@Data
@Table(name = "StockMarket_storages")
public class Storage {
    @Id
    int id;

    String name;

    String item;

    Integer number;

    String source;

    Timestamp tradeTime;

    @CreatedTimestamp
    Timestamp createTime;


}
