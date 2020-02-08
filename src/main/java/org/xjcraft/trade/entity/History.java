package org.xjcraft.trade.entity;

import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.Index;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Ree on 2016/1/25.
 */
@Entity
@Data
@Table(name = "dev_history")
public class History {
    @Id
    int id;
    @Index
    String seller;
    @Index
    String buyer;
    @Index
    String item;

    Integer price;

    String currency;

    Integer number;

    @CreatedTimestamp
    Timestamp createTime;


}
