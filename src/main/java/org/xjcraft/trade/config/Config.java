package org.xjcraft.trade.config;

import lombok.Data;
import org.xjcraft.annotation.Instance;
import org.xjcraft.annotation.RConfig;

@RConfig
@Data
public class Config {
    @Instance
    public static Config config = new Config();
    Boolean blockFML = true;
    Boolean shop_enable = true;
    String shop_name = "[Stock Market]";
    String shop_bagName = "[Stock Storage]";
    String shop_offerName = "[My offers]";


}
