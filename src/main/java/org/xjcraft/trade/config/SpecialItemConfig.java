package org.xjcraft.trade.config;

import lombok.Data;
import org.bukkit.inventory.meta.ItemMeta;
import org.xjcraft.annotation.Folder;
import org.xjcraft.annotation.RConfig;

import java.util.HashMap;
import java.util.Map;


@RConfig(configName = "specialItem")
@Data
public class SpecialItemConfig {
    @Folder
    public static final Map<String, SpecialItemConfig> config = new HashMap<>();
    Map<Integer, ItemMeta> itemMetas = new HashMap<>();


}
