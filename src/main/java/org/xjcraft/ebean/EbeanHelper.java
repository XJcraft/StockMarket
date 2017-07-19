package org.xjcraft.ebean;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;


/**
 * A set of ebean helper methods for use in Bukkit plugins.
 * These were mostly methods of JavaPlugin before they were removed.
 */
public class EbeanHelper {


    /**
     * Generate database table structure based on registered entity classes
     *
     * @param db database object
     */
    public static void installDDL(EbeanServer db) {
        SpiEbeanServer serv = (SpiEbeanServer) db;
        DdlGenerator gen = serv.getDdlGenerator();
        gen.runScript(false, gen.generateCreateDdl());
    }


    /**
     * Trash the database
     *
     * @param db database object
     */
    public static void removeDDL(EbeanServer db) {
        SpiEbeanServer serv = (SpiEbeanServer) db;
        DdlGenerator gen = serv.getDdlGenerator();
        gen.runScript(true, gen.generateDropDdl());
    }


}