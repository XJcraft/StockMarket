package org.xjcraft.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Weiyuan on 2016/3/23.
 */
@Entity
@Table(name = "StockMarket_customItem")
public class CustomItem {
    @Id
    private int id;

    @Column
    private String name;

    @Column
    private String flatItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlatItem() {
        return flatItem;
    }

    public void setFlatItem(String flatItem) {
        this.flatItem = flatItem;
    }
}
