package org.xjcraft.trade.bean;

import io.ebean.annotation.Index;
import lombok.Data;
import org.bukkit.inventory.meta.ItemMeta;

import javax.persistence.Id;

/**
 * Created by Ree on 2016/3/23.
 */
@Data
public class StockCustomItem {
    @Id
    Integer id;

    @Index
    String meta;

    ItemMeta flatItem;

}
