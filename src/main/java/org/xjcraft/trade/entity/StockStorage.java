package org.xjcraft.trade.entity;

import io.ebean.annotation.CreatedTimestamp;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by Ree on 2016/1/21.
 */
@Entity
@Data
public class StockStorage {
    @Id
    int id;

    String name;

    String item;

    Integer number;

    String source;

    @CreatedTimestamp
    Timestamp createTime;


}
