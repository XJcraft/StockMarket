package org.xjcraft.trade.config;

import lombok.Data;
import org.xjcraft.annotation.Instance;
import org.xjcraft.annotation.RConfig;

@RConfig(configName = "message.yml")
@Data
public class MessageConfig {
    @Instance
    public static MessageConfig config = new MessageConfig();
    String moreMoney = "你没有足够的货币！";
    String moreItem = "你没有足够的物品！";
    String sellButton = "以%d价格卖出%d个%s";
    String buyButton = "以%d价格购买%d个%s";
    String itemOwned = "你拥有%d个%s。";
    String sellInfo = "%size%个出售，起价%price%%currency%";
    String nohighest = "无人出售";
    String buyInfo = "%size%笔交易尝试以%price%%currency%的价格购买";
    String nolowest = "无人购买";
    String stackButton = "点击切换组模式与单个模式";
    String currencyNotFound = "该货币不存在！";
    String storage = "出售者%seller%\n类型%type%:%subtype%\n数量：%number%\n交易时间：%time%\n流水号：%id%";
    String collectAll = "一键收取！";
    String receive = "从仓库中收到来自%player%的%number%个%type%%subtype%";
    String slotNotEnough = "背包空间不足！\n你需要留出%slot%个空格来接受物品！";
    String buyHint = "你从%seller%手中购买了%number%个%type%%subtype%";
    String buyRemain = "没有足够的出售者！\n剩余%number%物品以%currency%%price%的价格发出一笔收购申请！";
    String sellHint = "你卖给%buyer%了%number%个%type%%subtype%";
    String sellRemain = "没有足够的收购者！\n剩余%number%物品以%currency%%price%的价格发出一笔出售申请！";
    String trade = "%operation%%number%个%type%%subtype%\n价格%currency%%price%\n流水号：%id%\n§r§l§4点击取消";

    String switchToBag = "点击打开我的仓库";
    String switchToCounter = "点击打开我的柜台";
    String sellInfoButton = "点击查询当前在售";
    String buyInfoButton = "点击查询当前收购";
    String inputButton = "点击手工输入数字";
    String inputTitle = "请输入一个整数";


    String sellDetailInfo = "%player%以%currency%%price%的价格出售%number%个%type%%subtype%";
    String buyDetailInfo = "%player%以%currency%%price%的价格收购%number%个%type%%subtype%";

//    String createShop = "商店%s已建立！";
//    String enterShop = "进入%s市场中...";
//    String itemMiss = "物品不存在，请检查输入！";
//    String invalidShop = "无效的商店！!";
//    String noItem = "You don't have enough %s!";
//    String deal = "Get %d %s with $%d from %s";
//    String priceButton = "Use %d %s to exchange %d emerald";
//    String createSell = "You place %d sale(s) of %s with price %d:%d";
//    String createBuy = "You spend $%d to buy %s under %d:%d";
//    String itemName = "Item name";
//    String incorrectName = "Incorrect name! Use /trade hand to see the correct item name on hand.";
//    String offers = " There are %d sales and %d purchases for %s.";
//    String norecord = "No trade record found!";
//    String total1 = "Total sold %s";
//    String total2 = "Average price  %s";
//    String total3 = "-----Latest sold-----";
//    String total4 = "Price  %d-%d";
//    String total5 = "From %s To %s";
//    String total6 = "Time  %tc%n";
//    String reload = "This plugin has been reloaded";
//    String save = "This plugin has been saved";
//    String existName = "This iem has already exist!";
//    String existName2 = "The name of this item is %s.";
//    String saveName = "Custom item successful saved!";
//    String failName = "Create new Item fail!";
}
