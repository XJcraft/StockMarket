package org.xjcraft.trade;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.database.CustomItem;
import org.xjcraft.database.History;
import org.xjcraft.database.Trade;
import org.xjcraft.util.InfoUtil;
import org.xjcraft.util.SerializeUtil;

import javax.persistence.OptimisticLockException;
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
            commandSender.sendMessage(plugin.getConfig().getString("message.noPermission"));
            return false;
        } else if (strings.length == 0) {
            return false;
        } else if (!commandSender.hasPermission("trade.enable")) {
            commandSender.sendMessage(plugin.getConfig().getString("noPermission"));
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
                case "reload":
                    if (commandSender.isOp()) {
                        plugin.reloadConfig();
                        commandSender.sendMessage(plugin.getConfig().getString("message.reload"));
                    } else {
                        commandSender.sendMessage(plugin.getConfig().getString("message.noPermission"));
                    }
                    break;
                case "save":
                    if (commandSender.isOp()) {
                        plugin.saveConfig();
                        commandSender.sendMessage(plugin.getConfig().getString("message.save"));
                    } else {
                        commandSender.sendMessage(plugin.getConfig().getString("message.noPermission"));
                    }
                    break;
                case "add":
                    if (commandSender.isOp()) {
                        excuteAdd(commandSender, command, s, strings);
                    } else {
                        commandSender.sendMessage(plugin.getConfig().getString("message.noPermission"));
                    }
                    break;
                case "get":
                    if (commandSender.isOp()) {
                        excuteGet(commandSender, command, s, strings);
                    } else {
                        commandSender.sendMessage(plugin.getConfig().getString("message.noPermission"));
                    }
                    break;
                case "lists":
                    if (commandSender.isOp()) {
                        excuteLists(commandSender, command, s, strings);
                    } else {
                        commandSender.sendMessage(plugin.getConfig().getString("message.noPermission"));
                    }
                    break;
                case "test":
                    if (commandSender.isOp()) {
                        commandSender.sendMessage(
                                ((Player) commandSender).getItemInHand().getItemMeta().toString());
                    } else {
                        commandSender.sendMessage(plugin.getConfig().getString("message.noPermission"));
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    private void excuteLists(CommandSender commandSender, Command command, String s, String[] strings) {
        List<CustomItem> customs = plugin.getDatabase().find(CustomItem.class).findList();
        commandSender.sendMessage("====Slist=============================");
        for (CustomItem cc : customs) {
            commandSender.sendMessage(cc.getName());
        }
        commandSender.sendMessage("====End=============================");
    }

    private void excuteGet(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 2) {
            try {
                CustomItem custom = plugin.getDatabase().find(CustomItem.class).where().ieq("name", strings[1]).findUnique();
                commandSender.sendMessage(strings[1]);
                commandSender.sendMessage(custom.getFlatItem());
                if (custom != null) {

                    ItemStack item = SerializeUtil.deSerialization(custom.getFlatItem());
                    if (item.getType() == Material.WRITTEN_BOOK) {
                    }
                    ((Player) commandSender).getInventory().addItem(item);
//                    commandSender.sendMessage(item.toString());
                }
            } catch (Exception e) {
                commandSender.sendMessage("not found");
            }

        }
    }

    private void excuteAdd(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 2) {
            ItemStack itemInHand = ((Player) commandSender).getItemInHand();
            itemInHand.setAmount(1);
            String flatItem = SerializeUtil.serialization(itemInHand);
            List<CustomItem> c = plugin.getDatabase().find(CustomItem.class).where().ieq("name", strings[1]).findList();
            List<CustomItem> c2 = plugin.getDatabase().find(CustomItem.class).where().ieq("flat_item", flatItem).findList();
            commandSender.sendMessage(flatItem);

            if (c.size() != 0 || c2.size() != 0) {
                commandSender.sendMessage(plugin.getConfig().getString("message.existName"));
                if (c2.size() != 0)
                    commandSender.sendMessage(String.format(plugin.getConfig().getString("message.existName2"), c2.get(0).getName()));
            } else {
                CustomItem custom = new CustomItem();
                custom.setFlatItem(flatItem);
                custom.setName(strings[1]);
                try {
                    plugin.getDatabase().save(custom);
                } catch (OptimisticLockException e) {
                    e.printStackTrace();
                    commandSender.sendMessage(plugin.getConfig().getString("message.failName"));
                }
                commandSender.sendMessage(plugin.getConfig().getString("message.saveName"));
            }

        } else {
            commandSender.sendMessage(plugin.getConfig().getString("message.help.add"));
        }

    }

    private void excuteMine(CommandSender commandSender, Command command, String s, String[] strings) {
        OfferGUI.OfferGUI(this.plugin, (Player) commandSender);
    }

    private void excuteHand(CommandSender commandSender, Command command, String s, String[] strings) {
        ItemStack hand = ((Player) commandSender).getItemInHand();
        String display = plugin.getConfig().getString("message.itemName") + hand.getType().name();
        if (hand.getDurability() != 0) {
            display = display + ":" + hand.getDurability();
        }
        try {
            ItemStack temp = new ItemStack(hand);
            temp.setAmount(1);
            CustomItem customItem = plugin.getDatabase().find(CustomItem.class).where().ieq("flatItem", SerializeUtil.serialization(temp)).findUnique();
            if (customItem != null)
                display = plugin.getConfig().getString("message.itemName") + customItem.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        commandSender.sendMessage(display);
    }

    private void excutePrice(CommandSender commandSender, Command command, String s, String[] strings) {
        //get specific price detail
        if (strings.length == 1) {
            ItemStack itemStack = ((Player) commandSender).getItemInHand();
            ItemStack itemStack1 = new ItemStack(itemStack);
            itemStack1.setAmount(1);
            sendDetail(commandSender, itemStack1);
        } else if (strings.length == 2) {
            String materialInput = strings[1];
            try {
                Material material = Material.getMaterial(materialInput.toUpperCase());
                sendDetail(commandSender, (new ItemStack(material, 1, (short) 0)));
            } catch (Exception e) {
                try {
                    CustomItem customItem = plugin.getDatabase().find(CustomItem.class).where().ieq("name", materialInput).findUnique();
                    if (customItem == null)
                        throw new Exception("no item");
                    ItemStack itemStack = SerializeUtil.deSerialization(customItem.getFlatItem());
                    sendDetail(commandSender, itemStack);
                } catch (Exception e1) {
//                    e1.printStackTrace();
                }
                commandSender.sendMessage(plugin.getConfig().getString("message.incorrectName"));
            }
        } else {
            commandSender.sendMessage(plugin.getConfig().getString("message.incorrectName"));
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
            commandSender.sendMessage(String.format(plugin.getConfig().getString("message.offers"), b[0], b[1], a));
        }
    }

    private void excuteOn(CommandSender commandSender, Command command, String s, String[] strings) {

        BagGUI.BagGUI(this.plugin, (Player) commandSender);
    }

    private void excuteHelp(CommandSender commandSender, Command command, String s, String[] strings) {
        //commandSender.sendMessage(plugin.getConfig().getString("message.help.help"));
        commandSender.sendMessage(plugin.getConfig().getString("message.help.help"));
        commandSender.sendMessage(plugin.getConfig().getString("message.help.open"));
        commandSender.sendMessage(plugin.getConfig().getString("message.help.list"));
        commandSender.sendMessage(plugin.getConfig().getString("message.help.mine"));
        commandSender.sendMessage(plugin.getConfig().getString("message.help.price"));
        commandSender.sendMessage(plugin.getConfig().getString("message.help.hand"));
        if (commandSender.isOp()) {
            commandSender.sendMessage(plugin.getConfig().getString("message.help.add"));
            commandSender.sendMessage(plugin.getConfig().getString("message.help.get"));
            commandSender.sendMessage(plugin.getConfig().getString("message.help.lists"));
        }
    }

    private void sendDetail(CommandSender commandSender, ItemStack itemStack) {

        Material material = itemStack.getType();
        short durability = itemStack.getDurability();
        String title = material.name();
        // FIXME: 2016/3/25
        List<History> list = this.plugin.getDatabase().find(History.class).where().ieq("material", material.name()).ieq("durability", durability + "").orderBy().desc("id").findList();
        try {
            CustomItem customItem = this.plugin.getDatabase().find(CustomItem.class).where().ieq("flat_item", SerializeUtil.serialization(itemStack)).findUnique();
            if (customItem != null) {
                title = customItem.getName();
                list = this.plugin.getDatabase().find(History.class).where().ieq("material", "S:" + customItem.getName()).ieq("durability", "0").orderBy().desc("id").findList();
            }

        } catch (Exception e) {
        }
        int total = 0;
        int itemP = 0;
        int moneyP = 0;
        for (History history : list) {
            total = total + history.getSold();
            itemP = itemP + history.getItemPrice() * history.getSold();
            moneyP = moneyP + history.getMoneyPrice() * history.getSold();
        }
        commandSender.sendMessage("========" + title + "========");
        if (list.size() == 0) {
            commandSender.sendMessage(plugin.getConfig().getString("message.norecord"));
        } else {
            /*commandSender.sendMessage("Total sold:" + total);
            commandSender.sendMessage("Average price:" + InfoUtil.average(itemP, moneyP));
            commandSender.sendMessage("-----Latest sold-----");
            commandSender.sendMessage("Price:" + list.get(0).getItemPrice() + ":" + list.get(0).getMoneyPrice());
            commandSender.sendMessage("From:" + list.get(0).getSeller() + " to " + list.get(0).getBuyer());
            commandSender.sendMessage("Time:" + list.get(0).getDealDate().getTime().toString());
            */
            commandSender.sendMessage(String.format(plugin.getConfig().getString("message.total1"), total));
            commandSender.sendMessage(String.format(plugin.getConfig().getString("message.total2"), InfoUtil.average(itemP, moneyP)));
            commandSender.sendMessage(String.format(plugin.getConfig().getString("message.total3")));
            commandSender.sendMessage(String.format(plugin.getConfig().getString("message.total4"), list.get(0).getItemPrice(), list.get(0).getMoneyPrice()));
            commandSender.sendMessage(String.format(plugin.getConfig().getString("message.total5"), list.get(0).getSeller(), list.get(0).getBuyer()));
            commandSender.sendMessage(String.format(plugin.getConfig().getString("message.total6"), list.get(0).getDealDate()));
        }
    }
}
