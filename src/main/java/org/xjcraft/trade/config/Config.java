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
    String shop_nameHide = "§c商店§店";
    String shop_bagName = "[仓库]";
    String shop_offerName = "[柜台]";
    String currencyPrefix = "使用货币%currency%交易";


}
