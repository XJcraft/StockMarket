package com.sunyard.trade;

import com.sunyard.util.itemUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Weiyuan on 2016/1/8.
 */
public class stockMarketCommandExecutor implements CommandExecutor {
    private final stockMarket plugin;

    public stockMarketCommandExecutor(stockMarket plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be run by a player.");
        } else if (!commandSender.hasPermission("trade.enable")) {
            commandSender.sendMessage("You don't have this permission!");
        } else if (strings.length == 0) {
            commandSender.sendMessage("Trade mode enabled");
            ((Player) commandSender).setItemInHand(itemUtil.getNumberStack(0));
            for (int i = 0; i < 10; i++) {
                ((Player) commandSender).getInventory().addItem(itemUtil.getNumberStack(i));
            }

            return true;
        }


        return false;
    }
}
