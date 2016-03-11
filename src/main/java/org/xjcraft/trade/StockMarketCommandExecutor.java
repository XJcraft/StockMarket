package org.xjcraft.trade;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.database.History;
import org.xjcraft.database.Trade;
import org.xjcraft.util.InfoUtil;

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
                case "h":
                    excuteHelp(commandSender, command, s, strings);
                    break;
                case "on":
                    excuteOn(commandSender, command, s, strings);
                    break;
                case "list":
                case "l":
                    excuteList(commandSender, command, s, strings);
                    break;
                case "price":
                case "p":
                    excutePrice(commandSender, command, s, strings);
                    break;
                case "hand":
                    excuteHand(commandSender, command, s, strings);
                    break;
                case "mine":
                case "m":
                    excuteMine(commandSender, command, s, strings);
                    break;
                case "test":
                    if (commandSender.isOp()) {

                    } else {
                        commandSender.sendMessage("You are not allowed to use this plugin!");
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    private void excuteMine(CommandSender commandSender, Command command, String s, String[] strings) {
        OfferGUI.OfferGUI(this.plugin, (Player) commandSender);
    }

    private void excuteHand(CommandSender commandSender, Command command, String s, String[] strings) {
        ItemStack hand = ((Player) commandSender).getItemInHand();
        String display = "Item name:" + hand.getType().name();
        if (hand.getDurability() != 0) {
            display = display + ":" + hand.getDurability();
        }
        commandSender.sendMessage(display);
    }

    private void excutePrice(CommandSender commandSender, Command command, String s, String[] strings) {
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
    }

    private void excuteList(CommandSender commandSender, Command command, String s, String[] strings) {
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
    }

    private void excuteOn(CommandSender commandSender, Command command, String s, String[] strings) {

        BagGUI.BagGUI(this.plugin, (Player) commandSender);
    }

    private void excuteHelp(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("/t(rade) h(elp) --show helps");
        commandSender.sendMessage("/t(rade) on   --open the storage for stock market");
        commandSender.sendMessage("/t(rade) l(ist) --list all the product in shop");
        commandSender.sendMessage("/t(rade) m(ine) --list my offers and cancel them");
        commandSender.sendMessage("/t(rade) p(rice) <item> --show the price detail of a specific product");
        commandSender.sendMessage("/t(rade) hand --show the name of item on hand");
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
