package com.pauljoda.vulcansrevenge.managers;

import com.pauljoda.vulcansrevenge.common.sword.ItemVulcanSword;
import com.teambr.nucleus.annotation.RegisteringItem;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/28/17
 */
public class ToolManager {

    /*******************************************************************************************************************
     * Tools                                                                                                           *
     *******************************************************************************************************************/

    @RegisteringItem
    public static ItemVulcanSword vulcanSword = new ItemVulcanSword();
}
