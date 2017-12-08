package com.pauljoda.vulcansrevenge.managers;

import com.pauljoda.vulcansrevenge.VulcansRevenge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 12/8/17
 */
public class ConfigManager {
    public static final Configuration config = new Configuration(
            new File(VulcansRevenge.configFolderLocation + File.separator + "VulcansRevenge.cfg"));

    public static Property sword_alter_dimensions;

    public static void preInit() {
        config.load();

        sword_alter_dimensions = config.get("generation",
                "sword_alter_dimensions",
                new int[] {0},
                "The dimesion ids the sword alter can generate");
    }
}
