package com.sunyard.trade;

import com.sunyard.database.History;
import com.sunyard.database.Trade;
import com.sunyard.util.InfoUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Weiyuan on 2016/1/8.
 */
public class StockMarketCommandExecutor implements CommandExecutor {
    private final StockMarket plugin;

    public StockMarketCommandExecutor(StockMarket plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be run by a player.");
            return false;
        } else if (strings.length == 0) {
            return false;
        } else if (!commandSender.hasPermission("trade.enable")) {
            commandSender.sendMessage("You don't have this permission!");
            return false;
        } else if (strings.length >= 1) {
            switch (strings[0]) {
                case "help":
                    commandSender.sendMessage("/trade help --show helps");
                    commandSender.sendMessage("/trade on   --open the storage for stock market");
                    commandSender.sendMessage("/trade list --list all the product in shop");
                    commandSender.sendMessage("/trade mine --list my offers and cancel them");
                    commandSender.sendMessage("/trade price <item> --show the price detail of a specific product");
                    commandSender.sendMessage("/trade hand --show the name of item on hand");

//                    ((Player) commandSender).getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
                    break;
                case "on":
                    BagGUI.BagGUI(this.plugin, (Player) commandSender);

                    break;
                case "list":
                    //get price list
                    commandSender.sendMessage("==Offers=======================");
                    List<Trade> Tradelist = this.plugin.getDatabase().find(Trade.class).setDistinct(true).where().orderBy().asc("material").findList();
                    HashMap<String, int[]> map = new HashMap<>();
                    for (Trade trade : Tradelist) {
                        if (!map.containsKey(trade.getMaterial())) {
                            int[] ints = {0, 0};
                            map.put(trade.getMaterial(), ints);
                        }
                        int[] a = map.get(trade.getMaterial());
                        if (trade.isSell()) {
                            a[0]++;
                        } else {
                            a[1]++;
                        }
                        map.put(trade.getMaterial(), a);
                    }
                    Iterator<Map.Entry<String, int[]>> iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, int[]> entry = iterator.next();
                        String a = (String) entry.getKey();
                        int[] b = (int[]) entry.getValue();
                        commandSender.sendMessage(String.format("%d sales and %d purchases for %s.", b[0], b[1], a));
                    }

                    break;
                case "price":
                    //get specific price detail
                    if (strings.length == 1) {
                        ItemStack itemStack = ((Player) commandSender).getItemInHand();
                        sendDetail(commandSender, ((Player) commandSender).getItemInHand());
                    } else if (strings.length == 2) {
                        String materialInput = strings[1];
                        try {
                            Material material = Material.getMaterial(materialInput.toUpperCase());
                            sendDetail(commandSender, new ItemStack(material, 1, (short) 0));
                        } catch (Exception e) {
                            this.plugin.getLogger().info("Incorrect name! Use /trade hand to see the correct item name on hand.");
                        }
                    } else {
                        commandSender.sendMessage("Check your input!");
                    }
                    break;
                case "hand":
                    ItemStack hand = ((Player) commandSender).getItemInHand();
                    String display = "Item name:" + hand.getType().name();
                    if (hand.getDurability() != 0) {
                        display = display + ":" + hand.getDurability();
                    }
                    commandSender.sendMessage(display);
                    break;
                case "mine":
                    OfferGUI.OfferGUI(this.plugin, (Player) commandSender);
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    private void sendDetail(CommandSender commandSender, ItemStack itemStack) {
        Material material = itemStack.getType();
        short durability = itemStack.getDurability();
        List<History> list = this.plugin.getDatabase().find(History.class).where().ieq("material", material.name()).ieq("durability", durability + "").orderBy().asc("id").findList();
        int total = 0;
        int itemP = 0;
        int moneyP = 0;
        for (History history : list) {
            total = total + history.getSold();
            itemP = itemP + history.getItemPrice();
            moneyP = moneyP + history.getMoneyPrice();
        }
        commandSender.sendMessage("========" + material.name() + "========");
        if (list.size() == 0) {
            commandSender.sendMessage("No trade record found!");
        } else {
            commandSender.sendMessage("Total sold:" + total);
            commandSender.sendMessage("Average price:" + InfoUtil.average(itemP, moneyP));
            commandSender.sendMessage("-----Latest sold-----");
            commandSender.sendMessage("Price:" + list.get(0).getItemPrice() + ":" + list.get(0).getMoneyPrice());
            commandSender.sendMessage("From:" + list.get(0).getSeller() + " to " + list.get(0).getBuyer());
            commandSender.sendMessage("Time:" + list.get(0).getDealDate().getTime().toString());
        }
    }
}
