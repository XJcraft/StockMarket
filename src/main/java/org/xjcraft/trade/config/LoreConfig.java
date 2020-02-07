package org.xjcraft.trade.config;

import lombok.Data;
import org.xjcraft.annotation.Instance;
import org.xjcraft.annotation.RConfig;

@RConfig(configName = "lore.yml")
@Data
public class LoreConfig {
    @Instance
    public static LoreConfig config = new LoreConfig();
    String flow = "Flow number";
    String paid = "Paid by %s";
    String from = "Markt from %s";
    String neverSold = "Never sold";
    String sold = "Item not found, check your item name!";
    String bought = "Invalid shop!";
    String packageIndex = "You don't have enough money for this trade!";
    String refund = "You will need to sell more items than price";
    String collect = "You don't have enough %s!";
    String cancel = "Get %d %s with $%d from %s";
    String sell = "Use %d %s to exchange %d emerald";
    String buy = "Sell %d %s with %d emerald";
    String price = "Buy %s with %d emerald with price %d:%d";

}
