package org.xjcraft.trade.entity;

import io.ebean.annotation.Index;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Ree on 2016/3/23.
 */
@Data
@Entity
@Table(name = "dev_customItem")
public class CustomItem {
    @Id
    int id;

    @Index
    String meta;

    String flatItem;

}
