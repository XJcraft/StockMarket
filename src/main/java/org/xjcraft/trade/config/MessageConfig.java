package org.xjcraft.trade.config;

import lombok.Data;
import org.xjcraft.annotation.Instance;
import org.xjcraft.annotation.RConfig;

@RConfig(configName = "message.yml")
@Data
public class MessageConfig {
    @Instance
    public static MessageConfig config = new MessageConfig();
    String noPermission = "权限不足！";
    String blockFML = "请更换无mod的纯净客户端！";
    String createShop = "商店%s已建立！";
    String enterShop = "进入%s市场中...";
    String itemMiss = "物品不存在，请检查输入！";
    String invalidShop = "无效的商店！!";
    String moreMoney = "You don't have enough money for this trade!";
    String moreItem = "You will need to sell more items than price";
    String noItem = "You don't have enough %s!";
    String deal = "Get %d %s with $%d from %s";
    String priceButton = "Use %d %s to exchange %d emerald";
    String sellButton = "Sell %d %s with %d emerald";
    String buyButton = "Buy %s with %d emerald with price %d:%d";
    String itemOwned = "你拥有%d个%s。";
    String moneyOwned = "You have $%d in your bag.";
    String createSell = "You place %d sale(s) of %s with price %d:%d";
    String createBuy = "You spend $%d to buy %s under %d:%d";
    String highest = "Highest buy price %d:%d by %s";
    String nohighest = "Nobody buying";
    String lowest = "Lowest sell price %d:%d by %s";
    String nolowest = "Nobody selling";
    String itemName = "Item name";
    String incorrectName = "Incorrect name! Use /trade hand to see the correct item name on hand.";
    String offers = " There are %d sales and %d purchases for %s.";
    String norecord = "No trade record found!";
    String total1 = "Total sold %s";
    String total2 = "Average price  %s";
    String total3 = "-----Latest sold-----";
    String total4 = "Price  %d-%d";
    String total5 = "From %s To %s";
    String total6 = "Time  %tc%n";
    String reload = "This plugin has been reloaded";
    String save = "This plugin has been saved";
    String existName = "This iem has already exist!";
    String existName2 = "The name of this item is %s.";
    String saveName = "Custom item successful saved!";
    String failName = "Create new Item fail!";
    String stackButton = "点击切换组模式与单个模式";
}
