package com.sunyard.trade;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        } else if (!commandSender.hasPermission("trade.enable")) {
            commandSender.sendMessage("You don't have this permission!");
        } else if (strings.length == 0) {
            BagGUI.BagGUI(plugin, (Player) commandSender);

            return true;
        } else if (strings.length == 1) {
            if (strings[1].equals("list")) {
                //TODO get price list
            }
        } else if (strings.length == 2) {
            if (strings[1].equals("list")) {
                String material = strings[2];
                //TODO get specific price detail
            }
        }
        return false;
    }


}
