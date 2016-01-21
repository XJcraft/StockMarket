package com.sunyard.trade;

import com.sunyard.database.Trade;
import com.sunyard.util.itemUtil;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Created by Weiyuan on 2016/1/8.
 */
public class stockMarketListener implements Listener {
    private Plugin plugin;

    public stockMarketListener(stockMarket stockMarket) {
        plugin = stockMarket;
    }

    public static String removeColor(String str) {
        if (str == null)
            return null;
        str = str.replaceAll("(?i)&[0-F]", "");
        return str;
    }


    @EventHandler
    public void createShop(SignChangeEvent event) {

        if (matchPattern(event.getLine(1))) {
            if (event.getPlayer().hasPermission("trade.create")) {
                event.setLine(1, "Shop");
                try {
                    event.setLine(2, Material.getMaterial(event.getLine(2).toUpperCase()).name());
                    event.getPlayer().setItemInHand(new ItemStack(Material.getMaterial(event.getLine(2).toUpperCase())));
                    event.getPlayer().sendMessage(String.format(plugin.getConfig().getString("message.createShop"), Material.getMaterial(event.getLine(2).toUpperCase()).toString()));

                } catch (Exception e) {
                    event.getPlayer().sendMessage(plugin.getConfig().getString("message.itemMiss"));
                }

            } else {
                event.setLine(1, "");
                event.getPlayer().sendMessage(plugin.getConfig().getString("message.noCreatePermission"));
            }
        }
    }

    @EventHandler
    public void openShop(PlayerInteractEvent event) {
        if (event.getPlayer() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase("shop")) {
                    try {
                        Material shopType = Material.getMaterial(sign.getLine(2).toUpperCase());
                        event.getPlayer().sendMessage(String.format(plugin.getConfig().getString("message.enterShop"), shopType.name()));
                        shopGUI.shopGUI(plugin, event.getPlayer(), shopType, 1, 1, 1, 1, false, false);
                        //open shop gui
                    } catch (Exception e) {
                        plugin.getLogger().info(e.toString());
                        event.getPlayer().sendMessage(plugin.getConfig().getString("message.invalidShop"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void useShop(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(plugin.getConfig().getString("shop.name"))) {
            return;
        }
        event.setCancelled(true);
        if (event.getRawSlot() >= 54) {
            return;
        }
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        String bill = event.getInventory().getItem(40).getItemMeta().getDisplayName();
        String[] bills = bill.split(";");
        Material shopType = Material.getMaterial(bills[0]);
        int moneyPrice = Integer.parseInt(bills[1]);
        int itemPrice = Integer.parseInt(bills[2]);
        int sellNumber = Integer.parseInt(bills[3]);
        int buyNumber = Integer.parseInt(bills[4]);
        boolean itemSize = Boolean.parseBoolean(bills[5]);
        boolean moneySize = Boolean.parseBoolean(bills[6]);
        int ownedItem = itemUtil.getItemNumber(player,shopType);

        switch (event.getSlot()) {
            case 0:
                itemPrice = itemPrice + 1000;
                moneyPrice = 1;
                break;
            case 1:
                itemPrice = itemPrice + 100;
                moneyPrice = 1;
                break;
            case 2:
                itemPrice = itemPrice + 10;
                moneyPrice = 1;
                break;
            case 3:
                itemPrice = itemPrice + 1;
                moneyPrice = 1;
                break;
            case 5:
                moneyPrice = moneyPrice + 1000;
                itemPrice = 1;
                break;
            case 6:
                moneyPrice = moneyPrice + 100;
                itemPrice = 1;
                break;
            case 7:
                moneyPrice = moneyPrice + 10;
                itemPrice = 1;
                break;
            case 8:
                moneyPrice = moneyPrice + 1;
                itemPrice = 1;
                break;
            case 18:
                itemPrice = itemPrice - 1000;
                moneyPrice = 1;
                break;
            case 19:
                itemPrice = itemPrice - 100;
                moneyPrice = 1;
                break;
            case 20:
                itemPrice = itemPrice - 10;
                moneyPrice = 1;
                break;
            case 21:
                itemPrice = itemPrice - 1;
                moneyPrice = 1;
                break;
            case 23:
                moneyPrice = moneyPrice - 1000;
                itemPrice = 1;
                break;
            case 24:
                moneyPrice = moneyPrice - 100;
                itemPrice = 1;
                break;
            case 25:
                moneyPrice = moneyPrice - 10;
                itemPrice = 1;
                break;
            case 26:
                moneyPrice = moneyPrice - 1;
                itemPrice = 1;
                break;
            case 27:
                sellNumber = sellNumber + 100;
                break;
            case 28:
                sellNumber = sellNumber + 10;
                break;
            case 29:
                sellNumber = sellNumber + 1;
                break;
            case 33:
                buyNumber = buyNumber + 100;
                break;
            case 34:
                buyNumber = buyNumber + 10;
                break;
            case 35:
                buyNumber = buyNumber + 1;
                break;
            case 45:
                sellNumber = sellNumber - 100;
                break;
            case 46:
                sellNumber = sellNumber - 10;
                break;
            case 47:
                sellNumber = sellNumber - 1;
                break;
            case 51:
                buyNumber = buyNumber - 100;
                break;
            case 52:
                buyNumber = buyNumber - 10;
                break;
            case 53:
                buyNumber = buyNumber - 1;
                break;

            case 39:
                itemSize = !itemSize;
                break;
            case 41:
                moneySize = !moneySize;
                break;

            case 48:
                if (!(itemUtil.getItemNumber(player,shopType)>=sellNumber)){
                    player.sendMessage(String.format("You don't have enough %s!",shopType.name()));
                    break;}
                try {
                    player.getInventory().setContents(itemUtil.removeItem(player,shopType,sellNumber));
                }catch (Exception e){
                    player.sendMessage("Trade failed!");
                }

                Trade trade = new Trade();
                trade.setPlayer(player.getName());
                trade.setSell(true);
                trade.setMaterial(shopType);
                trade.setMoneyPrice(moneyPrice);
                trade.setItemPrice(itemPrice);

                int number = 1;
                if (itemSize) {
                    number = shopType.getMaxStackSize();
                }
                trade.setTradeNumber(sellNumber);
                plugin.getDatabase().save(trade);

                player.sendMessage(String.format(plugin.getConfig().getString("message.createSell"),sellNumber,shopType.name(),itemPrice,moneyPrice));
//                buy(plugin, player, shopType, moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);
                break;

            case 50:
                sell(plugin, player, shopType, moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);
                break;

        }
        itemPrice = itemPrice + 10000;
        moneyPrice = moneyPrice + 10000;
        sellNumber = sellNumber + 1000;
        buyNumber = buyNumber + 1000;

        shopGUI.shopGUI(plugin, player, shopType, moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);
    }

    private void buy(Plugin plugin, Player player, Material shopType, int moneyPrice, int itemPrice, int sellNumber, int buyNumber, boolean itemSize, boolean moneySize) {
        //TODO buy method
    }

    private void sell(Plugin plugin, Player player, Material shopType, int moneyPrice, int itemPrice, int sellNumber, int buyNumber, boolean itemSize, boolean moneySize) {


    }

    private boolean matchPattern(String line) {
        return removeColor(line).equalsIgnoreCase(plugin.getConfig().getString("shop.name"));
    }
}
