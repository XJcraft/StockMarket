package org.xjcraft.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * Created by Weiyuan on 2016/1/21.
 */
@Entity
@Table(name = "StockMarket_storages")
public class Storage {
    @Id
    private int id;

    @Column
    private String playername;

    @Column
    private String itemName;

    @Column
    private short durability;

    @Column
    private int itemNumber;

    @Column
    private String paidFrom;

    @Column
    private String shopType;

    @Column
    private Calendar orderDate;

    @Column
    private Calendar bargainDate;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayername() {
        return this.playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public int getItemNumber() {
        return this.itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getPaidFrom() {
        return this.paidFrom;
    }

    public void setPaidFrom(String paidFrom) {
        this.paidFrom = paidFrom;
    }

    public String getShopType() {
        return this.shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public Calendar getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public Calendar getBargainDate() {
        return this.bargainDate;
    }

    public void setBargainDate(Calendar bargainDate) {
        this.bargainDate = bargainDate;
    }
}
