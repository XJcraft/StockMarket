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
    private int id;

    @Column
    private String seller;

    @Column
    private String buyer;

    @Column
    private String material;

    @Column
    private int itemPrice;

    @Column
    private int moneyPrice;

    @Column
    private int sold;

    @Column
    private Calendar dealDate;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeller() {
        return this.seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getMaterial() {
        return this.material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getItemPrice() {
        return this.itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getMoneyPrice() {
        return this.moneyPrice;
    }

    public void setMoneyPrice(int moneyPrice) {
        this.moneyPrice = moneyPrice;
    }

    public int getSold() {
        return this.sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public Calendar getDealDate() {
        return this.dealDate;
    }

    public void setDealDate(Calendar dealDate) {
        this.dealDate = dealDate;
    }
}
