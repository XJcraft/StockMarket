package com.sunyard.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * Created by Weiyuan on 2016/1/25.
 */
@Entity
@Table(name = "StockMarket_history")
public class History {
    @Id
    int id;

    @Column
    String seller;

    @Column
    String buyer;

    @Column
    String material;

    @Column
    int itemPrice;

    @Column
    int moneyPrice;

    @Column
    Calendar dealDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getMoneyPrice() {
        return moneyPrice;
    }

    public void setMoneyPrice(int moneyPrice) {
        this.moneyPrice = moneyPrice;
    }


    public Calendar getDealDate() {
        return dealDate;
    }

    public void setDealDate(Calendar dealDate) {
        this.dealDate = dealDate;
    }
}
