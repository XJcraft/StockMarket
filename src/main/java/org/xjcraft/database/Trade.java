package org.xjcraft.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * Created by Weiyuan on 2016/1/20.
 */
@Entity
@Table(name = "StockMarket_trades")
public class Trade {

    @Id
    private int id;

    @Column
    private String player;

    @Column
    private boolean sell;

    @Column
    private String material;

    @Column
    private short durability;

    @Column
    private int itemPrice;

    @Column
    private int moneyPrice;

    @Column
    private int tradeNumber;

    @Column
    private double price;

    @Column
    private Calendar tradeDate;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public boolean isSell() {
        return this.sell;
    }

    public void setSell(boolean sell) {
        this.sell = sell;
    }

    public String getMaterial() {
        return this.material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
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

    public int getTradeNumber() {
        return this.tradeNumber;
    }

    public void setTradeNumber(int tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Calendar getTradeDate() {
        return this.tradeDate;
    }

    public void setTradeDate(Calendar tradeDate) {
        this.tradeDate = tradeDate;
    }
}
