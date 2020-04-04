package org.xjcraft.trade.config;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.xjcraft.annotation.Comment;
import org.xjcraft.annotation.Instance;
import org.xjcraft.annotation.RConfig;

@RConfig(configName = "icon.yml")
@Data
public class IconConfig {
    @Instance
    public static IconConfig config = new IconConfig();
    @Comment("选择界面——购买")
    ItemStack buy = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("选择界面——出售")
    ItemStack sell = new ItemStack(Material.RED_BANNER, 1);
    @Comment("我的仓库")
    ItemStack bag = new ItemStack(Material.CHEST, 1);
    @Comment("我的柜台")
    ItemStack account = new ItemStack(Material.LECTERN, 1);
    @Comment("价格信息")
    ItemStack price = new ItemStack(Material.EMERALD, 1);
    @Comment("+")
    ItemStack blackPlus = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("-")
    ItemStack blackMinus = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("0")
    ItemStack blackNum0 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("1")
    ItemStack blackNum1 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("2")
    ItemStack blackNum2 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("3")
    ItemStack blackNum3 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("4")
    ItemStack blackNum4 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("5")
    ItemStack blackNum5 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("6")
    ItemStack blackNum6 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("7")
    ItemStack blackNum7 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("8")
    ItemStack blackNum8 = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("9")
    ItemStack blackNum9 = new ItemStack(Material.BLACK_BANNER, 1);

    @Comment("+")
    ItemStack yellowPlus = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("-")
    ItemStack yellowMinus = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("0")
    ItemStack yellowNum0 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("1")
    ItemStack yellowNum1 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("2")
    ItemStack yellowNum2 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("3")
    ItemStack yellowNum3 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("4")
    ItemStack yellowNum4 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("5")
    ItemStack yellowNum5 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("6")
    ItemStack yellowNum6 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("7")
    ItemStack yellowNum7 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("8")
    ItemStack yellowNum8 = new ItemStack(Material.ORANGE_BANNER, 1);
    @Comment("9")
    ItemStack yellowNum9 = new ItemStack(Material.ORANGE_BANNER, 1);

    @Comment("输入数量")
    ItemStack enter = new ItemStack(Material.FEATHER, 1);
    @Comment("输入价格")
    ItemStack enterPrice = new ItemStack(Material.FEATHER, 1);

    @Comment("关闭")
    ItemStack close = new ItemStack(Material.STRUCTURE_VOID, 1);

    @Comment("收购按钮——购买界面")
    ItemStack buySimple = new ItemStack(Material.WRITABLE_BOOK, 1);
    @Comment("收购按钮")
    ItemStack buyConfirm = new ItemStack(Material.WRITABLE_BOOK, 1);
    @Comment("出售按钮")
    ItemStack sellConfirm = new ItemStack(Material.BLACK_BANNER, 1);
    @Comment("一键收取")
    ItemStack collectAll = new ItemStack(Material.ENDER_CHEST, 1);
    @Comment("我的背包内容")
    ItemStack mine = new ItemStack(Material.SKELETON_SKULL, 1);
    @Comment("空白")
    ItemStack air = new ItemStack(Material.AIR, 1);


}
