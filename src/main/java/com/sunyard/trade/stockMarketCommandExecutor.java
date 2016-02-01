package com.sunyard.trade;

import com.sunyard.database.History;
import com.sunyard.util.InfoUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Weiyuan on 2016/1/8.
 */
public class StockMarketCommandExecutor implements CommandExecutor {
    private final StockMarket plugin;

    public StockMarketCommandExecutor(StockMarket plugin) {
        this.plugin = plugin;
    }

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

                    break;
                case "on":
                    BagGUI.BagGUI(plugin, (Player) commandSender);

                    break;
                case "list":
                    //TODO get price list
                    commandSender.sendMessage("Not implement");
                    break;
                case "price":
                    //TODO get specific price detail
                    if (strings.length != 2) {
                        commandSender.sendMessage("Check your input!");
                    } else {
                        String materialInput = strings[1];
                        try {
                            Material material = Material.getMaterial(materialInput.toUpperCase());
                            plugin.getLogger().info(material.name());
                            List<History> list = plugin.getDatabase().find(History.class).where().ieq("material", material.name()).orderBy().asc("id").findList();
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
                        } catch (Exception e) {
                            plugin.getLogger().info("Incorrect name! Use /trade hand to see the correct item name on hand.");
                        }
                    }
                    break;
                case "hand":
                    commandSender.sendMessage("Item name:" + ((Player) commandSender).getItemInHand().getType().name());
                    break;
                case "mine":
                    //TODO show my trades and cancel it
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }
}
