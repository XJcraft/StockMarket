package com.sunyard.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Weiyuan on 2016/1/15.
 */
public class ItemUtil {
    public static ItemStack getNumberStack(int i) {

        i = i % 10;
        ItemStack itemStack = new ItemStack(Material.BANNER, 1);
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
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
                break;
            case 6:
                bannerMeta.setDisplayName("6");
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM));
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
        ItemStack upArrow = new ItemStack(Material.BANNER, 1);
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
        ItemStack downArrow = new ItemStack(Material.BANNER, 1);
        BannerMeta bannerMeta = (BannerMeta) downArrow.getItemMeta();
        bannerMeta.setDisplayName("-");
        bannerMeta.setBaseColor(DyeColor.BLACK);
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_LEFT_MIRROR));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        downArrow.setItemMeta(bannerMeta);
        return downArrow;
    }

    public static ItemStack getLowest() {
        ItemStack lowest = new ItemStack(Material.LEATHER, 1);
        return lowest;
    }

    public static ItemStack getHighest() {
        ItemStack Highest = new ItemStack(Material.PAPER, 1);
        return Highest;
    }

    public static ItemStack getDetail(String name, short durability, int moneyPrice, int itemPrice, int sellNumber, int buyNumber, boolean itemSize, boolean moneySize) {
        ItemStack detailPaper = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta im = detailPaper.getItemMeta();
        im.setDisplayName(name + ";" + durability + ";" + moneyPrice + ";" + itemPrice + ";" + sellNumber + ";" + buyNumber + ";" + itemSize + ";" + moneySize);
        detailPaper.setItemMeta(im);
        return detailPaper;
    }

    public static ItemStack button(Material shopType, short durability, String info) {
        ItemStack button = button(shopType, info);
        button.setDurability(durability);
        return button;
    }

    public static ItemStack button(Material shopType, String info) {
        ItemStack sell = new ItemStack(shopType, 1);
        ItemMeta im;
        im = sell.getItemMeta();
        im.setDisplayName(info);
        sell.setItemMeta(im);
        return sell;
    }

    public static Material getCurrency() {
        return Material.EMERALD;
    }

    public static int getItemNumber(Player player, Material type) {
        return getItemNumber(player, type, (short) 0);
    }

    public static int getItemNumber(Player player, Material type, short durability) {
        ItemStack[] bag = player.getInventory().getContents();
        int itemCount = 0;
        for (ItemStack i : bag) {
            if (i != null) {
                if (i.getType().equals(type) && i.getDurability() == durability) {
                    itemCount = i.getAmount() + itemCount;
                }
            }
        }
        return itemCount;
    }

    public static ItemStack[] removeItem(Player player, Material material, int number) throws Exception {
        return removeItem(player, material, (short) 0, number);
    }


    public static ItemStack[] removeItem(Player player, Material material, short durability, int number) throws Exception {
        ItemStack[] itemStacks = player.getInventory().getContents();

        for (ItemStack i : itemStacks) {
            if (i != null) {
                if (i.getType().equals(material) && i.getDurability() == durability) {
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
        if (number != 0) {
            throw new Exception("not enough items!");
        }
        return itemStacks;
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
        ItemStack[] itemStacks = player.getInventory().getContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                return true;
            }
        }
        return false;
    }
}
