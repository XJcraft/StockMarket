package org.xjcraft.trade.utils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.trade.config.IconConfig;
import org.xjcraft.trade.config.MessageConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Ree on 2016/1/15.
 */
public class ItemUtil {

    public static ItemStack getConfigPrice(int i) {
        i = i % 10;
        switch (i) {
            case 0:
                return IconConfig.config.getBlackNum0();
            case 1:
                return IconConfig.config.getBlackNum1();
            case 2:
                return IconConfig.config.getBlackNum2();
            case 3:
                return IconConfig.config.getBlackNum3();
            case 4:
                return IconConfig.config.getBlackNum4();
            case 5:
                return IconConfig.config.getBlackNum5();
            case 6:
                return IconConfig.config.getBlackNum6();
            case 7:
                return IconConfig.config.getBlackNum7();
            case 8:
                return IconConfig.config.getBlackNum8();
            case 9:
                return IconConfig.config.getBlackNum9();

        }
        return null;
    }

    public static ItemStack getConfigNumber(int i) {
        i = i % 10;
        switch (i) {
            case 0:
                return IconConfig.config.getYellowNum0();
            case 1:
                return IconConfig.config.getYellowNum1();
            case 2:
                return IconConfig.config.getYellowNum2();
            case 3:
                return IconConfig.config.getYellowNum3();
            case 4:
                return IconConfig.config.getYellowNum4();
            case 5:
                return IconConfig.config.getYellowNum5();
            case 6:
                return IconConfig.config.getYellowNum6();
            case 7:
                return IconConfig.config.getYellowNum7();
            case 8:
                return IconConfig.config.getYellowNum8();
            case 9:
                return IconConfig.config.getYellowNum9();

        }
        return null;
    }


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

    public static ItemStack getAmountInfo(String name) {
        ItemStack item = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta bannerMeta = item.getItemMeta();
        bannerMeta.setDisplayName(name);
        // bannerMeta.setLore(Arrays.asList(strings));
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

    public static ItemStack getMine() {
        ItemStack temp = new ItemStack(Material.SKELETON_SKULL, 1);
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getMine());
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getBuy() {
        ItemStack temp = IconConfig.config.getBuy();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getBuyStore());
        List<String> lore = new ArrayList<>();
        lore.add(MessageConfig.config.getBuyStoreIntroduce());
        itemMeta.setLore(lore);
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getSell() {
        ItemStack temp = IconConfig.config.getSell();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getSellStore());
        List<String> lore = new ArrayList<>();
        lore.add(MessageConfig.config.getSellStoreIntroduce());
        itemMeta.setLore(lore);
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getBuyConfirm() {
        ItemStack temp = IconConfig.config.getBuyConfirm();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getBuy());
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getBuySimple() {
        ItemStack temp = IconConfig.config.getBuySimple();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getBuy());
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getSellConfirm() {
        ItemStack temp = IconConfig.config.getSellConfirm();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getSell());
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getClose() {
        ItemStack temp = IconConfig.config.getClose();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getClose());
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getBuymod() {
        ItemStack temp = IconConfig.config.getClose();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getBuymod());
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static ItemStack getSellmod() {
        ItemStack temp = IconConfig.config.getClose();
        ItemMeta itemMeta = temp.getItemMeta();
        itemMeta.setDisplayName(MessageConfig.config.getSellmod());
        temp.setItemMeta(itemMeta);
        return temp;
    }

    public static int getItemNumber(Player player, Material type) {
        return getItemNumber(player, type, (short) 0);
    }

    public static int getItemNumber(Player player, Material type, short durability) {
        ItemStack itemStack = new ItemStack(type, 1, durability);
        return getItemNumber(player, itemStack);
    }

    public static int getItemNumber(Player player, ItemStack itemStack) {
        ItemStack[] bag = player.getInventory().getStorageContents();
        int itemCount = 0;
        for (int i1 = 0; i1 < bag.length; i1++) {
            ItemStack i = bag[i1];
            if (i != null) {
                int amount = i.getAmount();
                itemStack.setAmount(amount);
                if (isEquals(i, itemStack)) {
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
        ItemStack[] bag = player.getInventory().getStorageContents();
        if (getItemNumber(player, itemStack) < number) {
            throw new Exception("not enough items!");
        }
        for (int i1 = 0; i1 < bag.length; i1++) {
            ItemStack i = bag[i1];
            if (i != null) {
                itemStack.setAmount(i.getAmount());
                if (isEquals(itemStack, i)) {
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
        player.getInventory().setStorageContents(bag);
//        if (number != 0) {
//            throw new Exception("not enough items!");
//        }
        return bag;
    }

    public static boolean isEquals(ItemStack a, ItemStack b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.getType() == b.getType() && hashcode(a.getItemMeta()).equals(hashcode(b.getItemMeta()));
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
        ItemStack temp = IconConfig.config.getBag();
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getSwitchToBag());
        List<String> lore = new ArrayList<>();
        lore.add(MessageConfig.config.getSwitchToBagIntroduce());
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getSwitchCounterButton() {
        ItemStack temp = IconConfig.config.getAccount();
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getSwitchToCounter());
        List<String> lore = new ArrayList<>();
        lore.add(MessageConfig.config.getSwitchToCounterIntroduce());
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getSellInfoButton() {
        ItemStack temp = new ItemStack(Material.PAPER, 1);
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getSellInfoButton());
        temp.setItemMeta(im);
        return temp;
    }

    public static ItemStack getBuyInfoButton() {
        ItemStack temp = new ItemStack(Material.PAPER, 1);
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getBuyInfoButton());
        temp.setItemMeta(im);
        return temp;
    }


    public static ItemStack getInputButton() {
        ItemStack temp = new ItemStack(Material.FEATHER, 1);
        ItemMeta im;
        im = temp.getItemMeta();
        im.setDisplayName(MessageConfig.config.getInputButton());
        temp.setItemMeta(im);
        return temp;
    }

    public static Integer hashcode(ConfigurationSerializable itemMeta) {
        int hashcode = 0;
        if (itemMeta == null) return hashcode;
        Map<String, Object> map = itemMeta.serialize();
        ArrayList<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        for (String s : list) {
            int keyHash = s.hashCode();
            Object o = map.get(s);
            int valueHash = 0;
            if ((o instanceof ConfigurationSerializable)) {
                valueHash = hashcode((ConfigurationSerializable) o);
            } else {
                valueHash = o.toString().hashCode();
            }
            hashcode = hashcode * 53 + keyHash * 211 + valueHash;
        }
        return hashcode;
    }
}
