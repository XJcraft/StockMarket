package com.sunyard.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Weiyuan on 2016/1/21.
 */
@Entity
@Table(name = "StockMarket_storages")
public class Storage {
    @Id
    int id;

    @Column
    String playername;

    @Column
    String itemName;

    @Column
    int itemNumber;
}
