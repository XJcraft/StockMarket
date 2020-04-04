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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.trade.config.Config;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.trade.gui.Bag;
import org.xjcraft.trade.gui.Counter;
import org.xjcraft.trade.gui.Menu;
import org.xjcraft.trade.gui.StockMarketGui;
import org.xjcraft.utils.StringUtil;

import java.util.HashMap;
import java.util.Objects;

public class StockMarketListener implements Listener {
    private final StockMarket plugin;
    private final StockMarketManager manager;

    public StockMarketListener(StockMarket plugin, StockMarketManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void drag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof StockMarketGui) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof StockMarketGui) {
            if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR ||
                    event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                    event.getAction() == InventoryAction.PLACE_SOME ||
                    event.getRawSlot() < 54) {
                event.setCancelled(true);
                ((StockMarketGui) event.getInventory().getHolder()).onClick((Player) event.getWhoClicked(), event.getRawSlot());
            }
//            System.out.println(JSON.toJSONString(event));
        }
    }

    @EventHandler
    public void sign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[s]") && event.getPlayer().hasPermission("trade.create")
                || Objects.requireNonNull(event.getLine(0)).equalsIgnoreCase(Config.config.getShop_name())
                || Objects.requireNonNull(event.getLine(0)).equalsIgnoreCase(Config.config.getShop_name().replace("[", "【").replace("]", "】"))) {
            String currency = event.getLine(1).toUpperCase();
            OperateResult currencyInfo = CurrencyService.getCurrencyInfo(currency);
            if (currencyInfo.getSuccess()) {
                event.setLine(0, Config.config.getShop_name());
                event.setLine(1, String.format("[%s]", currency));
                event.setLine(2, "");
//                event.setLine(3, "");
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
                if (lines[0].contains(Config.config.getShop_name())) {
                    if (StringUtil.isEmpty(lines[2])) {
                        //create shop with item in hand
                        ItemStack itemInHand = player.getItemInHand();
                        if (itemInHand.getType() == Material.AIR) return;
                        updateSign(sign, itemInHand);
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                    } else {
                        //get into a shop
                        String currency = sign.getLine(1);
                        if (currency.length() < 5) return;
                        currency = currency.substring(currency.indexOf("[") + 1, currency.indexOf("]"));
                        String[] split = sign.getLine(2).split("@");
                        String type = split[0];
                        String hashcode;
                        if (!StringUtil.isEmpty(sign.getLine(3))) {
                            hashcode = sign.getLine(3);
                        } else if (split.length > 1) {
                            hashcode = split[1];
                        } else {
                            hashcode = "";
                        }
                        ItemStack itemStack = manager.getItemStack(type, hashcode);
                        Menu shop = new Menu(plugin, player, currency, itemStack, sign);
                        player.openInventory(shop.getInventory());
//                        updateSign(sign, itemStack);

                    }
                } else if (lines[0].contains(Config.config.getShop_nameHide())) {
                    if (sign.getLine(1).contains("@")) {
                        //get into a shop
                        String currency = sign.getLine(1);
                        if (currency.length() < 5) return;
                        currency = currency.substring(currency.indexOf("[") + 1, currency.indexOf("]"));
                        String[] split = sign.getLine(1).trim().split("@");
                        String type = split[1];
                        String hashcode = split.length > 2 ? split[2] : "";
                        ItemStack itemStack = manager.getItemStack(type, hashcode);
                        manager.openIndex(player, sign, currency, itemStack);

                    }
                } else if (lines[0].equals(Config.config.getShop_bagName())) {
                    player.openInventory(new Bag(plugin, player).getInventory());
                } else if (lines[0].equals(Config.config.getShop_offerName())) {
                    player.openInventory(new Counter(plugin, player).getInventory());
                }
            }
        }
    }


    public void updateSign(Sign sign, ItemStack itemInHand) {
        HashMap<String, String> placeHolder = new HashMap<>() {{
            put("shop", Config.config.getShop_nameHide());
            put("item", plugin.getManager().getTranslate(itemInHand));
            put("custom", StringUtil.isEmpty(sign.getLine(3)) ? "" : String.format("§3[%s]", sign.getLine(3)));
            put("currency", "[" + sign.getLine(1).substring(sign.getLine(1).indexOf("[") + 1, sign.getLine(1).indexOf("]")) + "]");
            put("hashcode", "                @" + itemInHand.getType().name() + "@" + plugin.getManager().getSubType(itemInHand));
            put("sell", " - ");
            put("buy", " - ");
        }};
        sign.setLine(0, StringUtil.applyPlaceHolder(Config.config.getLine0() + "%shop%", placeHolder));
        sign.setLine(1, StringUtil.applyPlaceHolder(Config.config.getLine1() + "%hashcode%", placeHolder));
        sign.setLine(2, StringUtil.applyPlaceHolder(Config.config.getLine2(), placeHolder));
        sign.setLine(3, StringUtil.applyPlaceHolder(Config.config.getLine3(), placeHolder));
//                        String label = plugin.getManager().getSubType(itemInHand);
//                        sign.setLine(3, label);

        sign.update();
    }


}
