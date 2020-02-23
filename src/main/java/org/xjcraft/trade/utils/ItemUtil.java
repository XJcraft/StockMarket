package org.xjcraft.trade.utils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.config.MessageConfig;
import org.xjcraft.utils.StringUtil;

import java.util.Arrays;

/**
 * Created by Ree on 2016/1/15.
 */
public class ItemUtil {
    public static ItemStack getNumberStack(int i) {

        i = i % 10;
        ItemStack itemStack = new ItemStack(Material.WHITE_BANNER, 1);
        BannerMeta bannerMeta = (BannerMeta) itemStack.getItemMeta();
        bannerMeta.setBaseColor(DyeColor.WHITE);

        switch (i) {
            case 0:
                bannerMeta.setDisplayName("0");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
                break;
            case 1:
                bannerMeta.setDisplayName("1");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                break;
            case 2:
                bannerMeta.setDisplayName("2");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                break;
            case 3:
                bannerMeta.setDisplayName("3");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                break;
            case 4:
                bannerMeta.setDisplayName("4");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                break;
            case 5:
                bannerMeta.setBaseColor(DyeColor.BLACK);
                bannerMeta.setDisplayName("5");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT));
                break;
            case 6:
                bannerMeta.setDisplayName("6");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                break;
            case 7:
                bannerMeta.setDisplayName("7");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                break;
            case 8:
                bannerMeta.setDisplayName("8");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                break;
            case 9:
                bannerMeta.setDisplayName("9");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                break;
        }
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        itemStack.setItemMeta(bannerMeta);
        return itemStack;
    }

    public static ItemStack getUpArrow() {
        ItemStack upArrow = new ItemStack(Material.BLACK_BANNER, 1);
        BannerMeta bannerMeta = (BannerMeta) upArrow.getItemMeta();
        bannerMeta.setDisplayName("+");
        bannerMeta.setBaseColor(DyeColor.BLACK);
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_LEFT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT_MIRROR));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));

        upArrow.setItemMeta(bannerMeta);
        return upArrow;
    }

    public static ItemStack getDownArrow() {
        ItemStack downArrow = new ItemStack(Material.BLACK_BANNER, 1);
        BannerMeta bannerMeta = (BannerMeta) downArrow.getItemMeta();
        bannerMeta.setDisplayName("-");
        bannerMeta.setBaseColor(DyeColor.BLACK);
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_LEFT_MIRROR));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        downArrow.setItemMeta(bannerMeta);
        return downArrow;
    }

    public static ItemStack getMarketInfo(String[] strings) {
        ItemStack item = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta bannerMeta = item.getItemMeta();
        bannerMeta.setDisplayName("Price");
//        bannerMeta.setBaseColor(DyeColor.WHITE);
//        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
//        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
//        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR));
//        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
//        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
//        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        bannerMeta.setLore(Arrays.asList(strings));
        item.setItemMeta(bannerMeta);
        return item;
    }

    public static ItemStack getLowest() {
        ItemStack lowest = new ItemStack(Material.LEATHER, 1);
        return lowest;
    }

    public static ItemStack getHighest() {
        ItemStack Highest = new ItemStack(Material.PAPER, 1);
        return Highest;
    }


    public static ItemStack button(Material shopType, short durability, String info) {
        ItemStack button = button(shopType, info);
        button.setDurability(durability);
        return button;
    }

    public static ItemStack button(Material shopType, String info) {
        ItemStack sell = new ItemStack(shopType, 1);
        return button(sell, info);
    }

    public static ItemStack button(ItemStack itemStack, String info) {
        ItemStack temp = new ItemStack(itemStack.getType(), 1, itemStack.getDurability());

        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(info);
//        im.setLore(null);
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getStackButton(ItemStack itemStack, Boolean stack, String displayName, String... info) {
        ItemStack temp = new ItemStack(itemStack.getType(), stack ? itemStack.getMaxStackSize() : 1, itemStack.getDurability());

        ItemMeta im;
        im = temp.getItemMeta();
        if (!StringUtil.isEmpty(displayName)) {
            im.setDisplayName(displayName);
        }
        im.setLore(Arrays.asList(info));
//        im.setLore(null);
        temp.setItemMeta(im);
        return temp;
    }

    public static Material getCurrency() {
        return Material.EMERALD;
    }

    public static int getItemNumber(Player player, Material type) {
        return getItemNumber(player, type, (short) 0);
    }

    public static int getItemNumber(Player player, Material type, short durability) {
        ItemStack itemStack = new ItemStack(type, 1, durability);
        return getItemNumber(player, itemStack);
    }

    public static int getItemNumber(Player player, ItemStack itemStack) {
        ItemStack[] bag = player.getInventory().getContents();
        int itemCount = 0;
        for (ItemStack i : bag) {
            if (i != null) {
                int amount = i.getAmount();
                itemStack.setAmount(amount);
                if (SerializeUtil.serialization(itemStack).equals(SerializeUtil.serialization(i))) {
                    itemCount = itemCount + amount;
                }
            }
        }
        return itemCount;
    }

    public static ItemStack[] removeItem(Player player, Material material, int number) throws Exception {
        return removeItem(player, material, (short) 0, number);
    }

    public static ItemStack[] removeItem(Player player, Material material, short durability, int number) throws Exception {
        return removeItem(player, new ItemStack(material, 1, durability), number);
    }

    public static ItemStack[] removeItem(Player player, ItemStack itemStack, int number) throws Exception {
        ItemStack[] bag = player.getInventory().getContents();
        if (getItemNumber(player, itemStack) < number) {
            throw new Exception("not enough items!");
        }
        for (ItemStack i : bag) {
            if (i != null) {
                itemStack.setAmount(i.getAmount());
                if (i.hashCode() == itemStack.hashCode()) {
                    if (i.getAmount() <= number) {
                        number = number - i.getAmount();
                        i.setType(Material.AIR);
                    } else {
                        i.setAmount(i.getAmount() - number);
                        number = 0;
                    }
                    if (number == 0) {
                        break;
                    }
                }
            }
        }
        player.getInventory().setContents(bag);
//        if (number != 0) {
//            throw new Exception("not enough items!");
//        }
        return bag;
    }

    public static ItemStack[] addItem(Player player, Material material, int number) throws Exception {
        ItemStack[] itemStacks = player.getInventory().getContents();
        return addItem(itemStacks, material, number);
    }

    public static ItemStack[] addItem(ItemStack[] itemStacks, Material material, int number) throws Exception {

        for (int n = 0; n < 36; n++) {
            if (number == 0) {
                break;
            }
            if (itemStacks[n] == null) {
                itemStacks[n] = new ItemStack(material, 1);
                if (number <= material.getMaxStackSize()) {
                    itemStacks[n].setAmount(number);
                    number = 0;
                } else {
                    itemStacks[n].setAmount(material.getMaxStackSize());
                    number = number - material.getMaxStackSize();
                }
            } else if (itemStacks[n].getType().equals(material)) {
                if (number <= (material.getMaxStackSize() - itemStacks[n].getAmount())) {
                    itemStacks[n].setAmount(itemStacks[n].getAmount() + number);
                    number = 0;
                } else {
                    number = number - material.getMaxStackSize() + itemStacks[n].getAmount();
                    itemStacks[n].setAmount(material.getMaxStackSize());
                }
            }
        }
        if (number > 0) {
            throw new Exception();
        }
        return itemStacks;
    }

    public static boolean hasEmptySlot(Player player) {
//        System.out.println("inv:"+player.getInventory().firstEmpty());

        return (player.getInventory().firstEmpty() != -1);
        /*
        ItemStack[] itemStacks = player.getInventory().getContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                return true;
            }
        }
        return false;*/
    }

    public static int countEmptySlot(Player player) {
        int empty = 0;
        ItemStack[] itemStacks = player.getInventory().getStorageContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                empty++;
            }
        }
        return empty;
    }

    public static ItemStack getCollectAll() {
        ItemStack temp = new ItemStack(Material.ENDER_CHEST, 1);

        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getCollectAll());
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getSwitchBagButton() {
        ItemStack temp = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getSwitchToBag());
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getSwitchCounterButton() {
        ItemStack temp = new ItemStack(Material.OAK_DOOR, 1);
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getSwitchToCounter());
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getSellInfoButton() {
        ItemStack temp = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getSellInfoButton());
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getBuyInfoButton() {
        ItemStack temp = new ItemStack(Material.OAK_DOOR, 1);
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getBuyInfoButton());
        temp.setItemMeta(im);
        return temp;
    }


}
