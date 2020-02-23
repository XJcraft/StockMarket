package org.xjcraft.trade;

import com.zjyl1994.minecraftplugin.multicurrency.services.CurrencyService;
import com.zjyl1994.minecraftplugin.multicurrency.utils.OperateResult;
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
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.gui.Bag;
import org.xjcraft.trade.gui.Counter;
import org.xjcraft.trade.gui.Shop;
import org.xjcraft.trade.gui.StockMarketGui;
import org.xjcraft.utils.StringUtil;

import java.util.Objects;

public class StockMarketListener implements Listener {
    private final StockMarket plugin;
    private final StockMarketManager manager;

    public StockMarketListener(StockMarket plugin, StockMarketManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof StockMarketGui && event.getRawSlot() < 54) {
            event.setCancelled(true);
            ((StockMarketGui) event.getInventory().getHolder()).onClick((Player) event.getWhoClicked(), event.getRawSlot());
        }

//        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() instanceof BannerMeta) {
//            BannerMeta meta = (BannerMeta) event.getCurrentItem().getItemMeta();
//            System.out.println(meta.getBaseColor());
//            for (Pattern pattern : meta.getPatterns()) {
//                System.out.println(pattern.getColor() + ":" + pattern.getPattern());
//            }
//
//        }
    }

    @EventHandler
    public void sign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[s]") && event.getPlayer().hasPermission("trade.create")
                || Objects.requireNonNull(event.getLine(0)).equalsIgnoreCase(Config.config.getShop_name())) {
            String currency = event.getLine(1).toUpperCase();
            OperateResult currencyInfo = CurrencyService.getCurrencyInfo(currency);
            if (currencyInfo.getSuccess()) {
                event.setLine(0, Config.config.getShop_name());
                event.setLine(1, String.format("[%s]", currency));
                event.setLine(2, "");
                event.setLine(3, "");
            } else {
                event.getPlayer().sendMessage(MessageConfig.config.getCurrencyNotFound());
                for (int i = 0; i < 4; i++) {
                    event.setLine(i, "");
                }
            }
        }
    }

    @EventHandler
    public void create(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                String[] lines = sign.getLines();
                if (lines[0].equals(Config.config.getShop_name())) {
                    if (StringUtil.isEmpty(lines[2])) {
                        //create shop with item in hand
                        ItemStack itemInHand = player.getItemInHand();
                        if (itemInHand.getType() == Material.AIR) return;
                        sign.setLine(1, sign.getLine(1) + "#" + plugin.getManager().getTranslate(itemInHand));
                        sign.setLine(2, itemInHand.getType().name());
                        String label = plugin.getManager().getSubType(itemInHand);
                        sign.setLine(3, label);

                        sign.update();
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                    } else {
                        //get into a shop
                        String type = sign.getLine(2);
                        String hashcode = sign.getLine(3);
                        ItemStack itemStack = manager.getItemStack(type, hashcode);
                        Shop shop = new Shop(plugin, player, "GOV", itemStack);
                        player.openInventory(shop.getInventory());
                    }
                } else if (lines[0].equals(Config.config.getShop_bagName())) {
                    player.openInventory(new Bag(plugin, player).getInventory());
                } else if (lines[0].equals(Config.config.getShop_offerName())) {
                    player.openInventory(new Counter(plugin, player).getInventory());
                }
            }
        }
    }


}
