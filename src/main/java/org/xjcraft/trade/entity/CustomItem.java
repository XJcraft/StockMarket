package org.xjcraft.trade.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Ree on 2016/3/23.
 */
@Data
@Entity
@Table(name = "StockMarket_customItem")
public class CustomItem {
    @Id
    int id;

    String name;

    String flatItem;

}
