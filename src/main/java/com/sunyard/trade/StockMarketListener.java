package com.sunyard.trade;

import com.sunyard.database.History;
import com.sunyard.database.Storage;
import com.sunyard.database.Trade;
import com.sunyard.util.InfoUtil;
import com.sunyard.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Weiyuan on 2016/1/8.
 */
public class StockMarketListener implements Listener {
    private Plugin plugin;

    public StockMarketListener(StockMarket stockMarket) {
        this.plugin = stockMarket;
    }

/*
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        plugin.getLogger().info("SignChangeEvent");
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
        plugin.getLogger().info("BlockBreakEvent");
    }
*/


    @EventHandler
    public void useStorage(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(this.plugin.getConfig().getString("shop.bagName"))) {
            return;
        }
        if (event.getRawSlot() < 54) {
            if (event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                ItemStack itemStack = event.getCurrentItem();

                String[] strings = itemStack.getItemMeta().getDisplayName().split(":");
                String[] lores = itemStack.getItemMeta().getLore().get(4).split("/");
                Storage storage = this.plugin.getDatabase().find(Storage.class).where().ieq("id", strings[1]).findUnique();
                if (lores[1].equals("1")) {
                    this.plugin.getDatabase().delete(storage);
                } else {
                    storage.setItemNumber(storage.getItemNumber() - itemStack.getAmount());
                    this.plugin.getDatabase().save(storage);
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(null);
                itemMeta.setDisplayName(null);
                itemStack.setItemMeta(itemMeta);
            } else {
                event.setCancelled(true);
                BagGUI.BagGUI(this.plugin, (Player) event.getWhoClicked());
            }
        } else {
        }
    }

    @EventHandler
    public void cancelTrade(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(this.plugin.getConfig().getString("shop.offerName"))) {
            return;
        }
        if (event.getRawSlot() < 54) {
            if (event.getAction() == InventoryAction.PICKUP_ALL) {
                ItemStack itemStack = event.getCurrentItem();
                String[] strings = itemStack.getItemMeta().getDisplayName().split(":");
                List<String> lores = itemStack.getItemMeta().getLore();
                Trade trade = this.plugin.getDatabase().find(Trade.class).where().ieq("id", strings[1]).findUnique();
                Storage storage = new Storage();
                storage.setPlayername(trade.getPlayer());
                storage.setShopType(trade.getMaterial());
                if (trade.isSell()) {
                    storage.setItemName(trade.getMaterial());
                } else {
                    storage.setItemName(ItemUtil.getCurrency().name());
                }
                storage.setItemNumber(trade.getTradeNumber());
                storage.setPaidFrom(trade.getPlayer());
                storage.setOrderDate(trade.getTradeDate());
                storage.setBargainDate(null);

                this.plugin.getDatabase().save(storage);
                this.plugin.getDatabase().delete(trade);

            }
            event.setCancelled(true);
            OfferGUI.OfferGUI(this.plugin, (Player) event.getWhoClicked());
        }
    }

    @EventHandler
    public void createShop(SignChangeEvent event) {

        if (InfoUtil.matchPattern(this.plugin.getConfig().getString("shop.name"), event.getLine(1))) {
            if (event.getPlayer().hasPermission("trade.create")) {
                event.setLine(1, this.plugin.getConfig().getString("shop.name"));
                try {
                    String[] item = event.getLine(2).split(":");
                    Material material = Material.getMaterial(item[0].toUpperCase());
                    String display = material.name();
                    short damage = 0;
                    if (item.length > 1) {
                        damage = Short.parseShort(item[1]);
                        plugin.getLogger().info("damage:" + damage + "   Maxdamage:" + material.getMaxDurability());
                        if (damage < 0 || damage > material.getMaxDurability()) {
                            throw new Exception();
                        }
                        display = display + ":" + damage;
                    }
                    event.setLine(2, display);
                    event.getPlayer().setItemInHand(new ItemStack(material, 1, damage));
                    event.getPlayer().sendMessage(String.format(this.plugin.getConfig().getString("message.createShop"), Material.getMaterial(event.getLine(2).toUpperCase()).toString()));

                } catch (Exception e) {
                    event.getPlayer().sendMessage(this.plugin.getConfig().getString("message.itemMiss"));
                    event.setLine(1, "");
                }

            } else {
                event.setLine(1, "");
                event.getPlayer().sendMessage(this.plugin.getConfig().getString("message.noCreatePermission"));
            }
        }
    }

    @EventHandler
    public void openShop(PlayerInteractEvent event) {
        if (event.getPlayer() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase(this.plugin.getConfig().getString("shop.name"))) {
                    try {
                        Material shopType = Material.getMaterial(sign.getLine(2).toUpperCase());
                        event.getPlayer().sendMessage(String.format(this.plugin.getConfig().getString("message.enterShop"), shopType.name()));
                        ShopGUI.shopGUI(this.plugin, event.getPlayer(), shopType, 1, 1, 1, 1, false, false);
                        // open shop gui
                    } catch (Exception e) {
                        this.plugin.getLogger().info(e.toString());
                        event.getPlayer().sendMessage(this.plugin.getConfig().getString("message.invalidShop"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void useShop(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(this.plugin.getConfig().getString("shop.name"))) {
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
        int ownedItem = ItemUtil.getItemNumber(player, shopType);

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
                sell(this.plugin, player, shopType, moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);
                break;

            case 50:
                buy(this.plugin, player, shopType, moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);
                break;

        }
        itemPrice = itemPrice + 10000;
        moneyPrice = moneyPrice + 10000;
        sellNumber = sellNumber + 1000;
        buyNumber = buyNumber + 1000;

        ShopGUI.shopGUI(this.plugin, player, shopType, moneyPrice, itemPrice, sellNumber, buyNumber, itemSize, moneySize);
    }

    private void buy(Plugin plugin, Player player, Material shopType, int moneyPrice, int itemPrice, int sellNumber, int buyNumber, boolean itemSize, boolean moneySize) {

        if (moneySize) {
            buyNumber = buyNumber * ItemUtil.getCurrency().getMaxStackSize();
        }
        if (buyNumber > ItemUtil.getItemNumber(player, ItemUtil.getCurrency())) {
            player.sendMessage("You don't have enough money for this trade!");
            return;
        } else if (buyNumber < moneyPrice) {
            player.sendMessage("You offer is not enough for a single trade");
            return;
        } else if (!(ItemUtil.getItemNumber(player, ItemUtil.getCurrency()) >= buyNumber)) {
            player.sendMessage(String.format("You don't have enough %s!", ItemUtil.getCurrency()));
            return;
        }

        try {
            player.getInventory().setContents(ItemUtil.removeItem(player, ItemUtil.getCurrency(), buyNumber));
        } catch (Exception e) {
            player.sendMessage("Trade failed!");
            return;
        }

        Trade trade = new Trade();
        trade.setPlayer(player.getName());
        trade.setSell(false);
        trade.setMaterial(shopType.name());
        trade.setMoneyPrice(moneyPrice);
        trade.setItemPrice(itemPrice);
        trade.setPrice(((double) moneyPrice) / (double) itemPrice);
        trade.setTradeDate(java.util.Calendar.getInstance());

        int number = 1;
        if (moneySize) {
            number = ItemUtil.getCurrency().getMaxStackSize();
        }
        trade.setTradeNumber(buyNumber);
        plugin.getDatabase().save(trade);

        player.sendMessage(String.format(plugin.getConfig().getString("message.createBuy"), buyNumber, shopType.name(), itemPrice, moneyPrice));
        trade(plugin, player, shopType);
    }

    private void sell(Plugin plugin, Player player, Material shopType, int moneyPrice, int itemPrice, int sellNumber, int buyNumber, boolean itemSize, boolean moneySize) {
        if (itemSize) {
            sellNumber = sellNumber * shopType.getMaxStackSize();
        }
        sellNumber = sellNumber - sellNumber % itemPrice;
        if (sellNumber == 0) {
            player.sendMessage("You will need to sell more items than price");
        }
        if (!(ItemUtil.getItemNumber(player, shopType) >= sellNumber)) {
            player.sendMessage(String.format("You don't have enough %s!", shopType.name()));
            return;
        }
        try {
            player.getInventory().setContents(ItemUtil.removeItem(player, shopType, sellNumber));
        } catch (Exception e) {
            player.sendMessage("Trade failed!");
            return;
        }

        Trade trade = new Trade();
        trade.setPlayer(player.getName());
        trade.setSell(true);
        trade.setMaterial(shopType.name());
        trade.setMoneyPrice(moneyPrice);
        trade.setItemPrice(itemPrice);
        trade.setPrice(((double) moneyPrice) / (double) itemPrice);
        trade.setTradeDate(java.util.Calendar.getInstance());

        int number = 1;
        if (itemSize) {
            number = shopType.getMaxStackSize();
        }
        trade.setTradeNumber(sellNumber);
        plugin.getDatabase().save(trade);

        player.sendMessage(String.format(plugin.getConfig().getString("message.createSell"), sellNumber, shopType.name(), itemPrice, moneyPrice));
        trade(plugin, player, shopType);

    }

    public void trade(Plugin plugin, Player player, Material shopType) {
        List<Trade> sells = plugin.getDatabase().find(Trade.class).where().ieq("sell", "1").ieq("material", shopType.name()).ieq("player", player.getName()).orderBy().asc("price").findList();
        List<Trade> paids = plugin.getDatabase().find(Trade.class).where().ieq("sell", "0").ieq("material", shopType.name()).ieq("player", player.getName()).orderBy().desc("price").findList();
        List<History> histories = new ArrayList<>();

        boolean isNotFinish = true;
        boolean hasTrade = false;
        while (isNotFinish) {
            if (sells.size() == 0 || paids.size() == 0) {
                isNotFinish = false;
                break;
            }
            Trade sell = sells.get(0);
            Trade paid = paids.get(0);
            if (sell.getPrice() > paid.getPrice()) {
                isNotFinish = false;
                break;
            }
            int multi = 1;
            if (sell.getTradeNumber() / sell.getItemPrice() >= paid.getTradeNumber() / sell.getMoneyPrice()) {
                multi = paid.getTradeNumber() / sell.getMoneyPrice();
            } else {
                multi = sell.getTradeNumber() / sell.getItemPrice();
            }

            sell.setTradeNumber(sell.getTradeNumber() - sell.getItemPrice() * multi);
            paid.setTradeNumber(paid.getTradeNumber() - sell.getMoneyPrice() * multi);

            Storage getMoney = new Storage();
            getMoney.setItemNumber(sell.getMoneyPrice() * multi);
            getMoney.setPlayername(sell.getPlayer());
            getMoney.setPaidFrom(paid.getPlayer());
            getMoney.setItemName(ItemUtil.getCurrency().name());
            getMoney.setShopType(sell.getMaterial());
            getMoney.setBargainDate(Calendar.getInstance());
            getMoney.setOrderDate(sell.getTradeDate());
            plugin.getDatabase().save(getMoney);

            Storage getItem = new Storage();
            getItem.setItemNumber(sell.getItemPrice() * multi);
            getItem.setPlayername(paid.getPlayer());
            getItem.setPaidFrom(sell.getPlayer());
            getItem.setItemName(sell.getMaterial());
            getItem.setShopType(sell.getMaterial());
            getItem.setBargainDate(getMoney.getBargainDate());
            getItem.setOrderDate(paid.getTradeDate());
            plugin.getDatabase().save(getItem);

            History history = new History();
            history.setBuyer(paid.getPlayer());
            history.setSeller(sell.getPlayer());
            history.setItemPrice(sell.getItemPrice());
            history.setMoneyPrice(sell.getMoneyPrice());
            history.setDealDate(Calendar.getInstance());
            history.setMaterial(sell.getMaterial());
            histories.add(history);
            player.sendMessage(String.format("Get %d %s with $%d from %s", sell.getItemPrice() * multi, shopType.name(), sell.getMoneyPrice() * multi, sell.getPlayer()));

            if (paid.getTradeNumber() == 0) {
                paids.remove(paid);
                plugin.getDatabase().delete(paid);
            } else if (paid.getTradeNumber() < paid.getMoneyPrice()) {
                Storage remainedMoney = new Storage();
                remainedMoney.setShopType(sell.getMaterial());
                remainedMoney.setItemName(ItemUtil.getCurrency().name());
                remainedMoney.setItemNumber(paid.getTradeNumber());
                remainedMoney.setPlayername(paid.getPlayer());
                remainedMoney.setPaidFrom(paid.getPlayer());
                remainedMoney.setOrderDate(paid.getTradeDate());
                remainedMoney.setBargainDate(null);
                plugin.getDatabase().save(remainedMoney);

                paids.remove(paid);
                plugin.getDatabase().delete(paid);
            }
            if (sell.getTradeNumber() == 0) {
                sells.remove(sell);
                plugin.getDatabase().delete(sell);
            }
        }
        plugin.getDatabase().save(sells);
        plugin.getDatabase().save(paids);
        plugin.getDatabase().save(histories);
        if (hasTrade) {
            BagGUI.BagGUI(plugin, player);
        }
    }
}
