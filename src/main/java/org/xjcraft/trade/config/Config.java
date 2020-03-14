package org.xjcraft.trade.config;

import lombok.Data;
import org.xjcraft.annotation.Instance;
import org.xjcraft.annotation.RConfig;

@RConfig
@Data
public class Config {
    @Instance
    public static Config config = new Config();
    Boolean shop_enable = true;
    String shop_name = "[商店]";
    String shop_nameHide = "§商§店§店";
    String shop_bagName = "[仓库]";
    String shop_offerName = "[柜台]";
    String line0 = "§b§l[%item%]%custom%%shop%";
    String line1 = " §6使用%currency%货币交易";
    String line2 = " §c§l购买价%sell%元/个";
    String line3 = "§2§l收购价%buy%元/个";


}
