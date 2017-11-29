package com.pauljoda.vulcansrevenge.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

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
public class DamageTypes {

    /*******************************************************************************************************************
     * Vulcan Damage                                                                                                   *
     *******************************************************************************************************************/

    // Instance
    public static final DamageSourceVulcan VULCAN = new DamageSourceVulcan();

    // Damage Source
    public static class DamageSourceVulcan extends DamageSource {
        public DamageSourceVulcan() {
            super("vulcan");
            setDamageBypassesArmor();
            setMagicDamage();
        }
    }

    // Entity Damage
    public static class EntityDamageSourceVulcan extends EntityDamageSource {
        public EntityDamageSourceVulcan(String damageTypeIn, @Nullable Entity damageSourceEntityIn) {
            super(damageTypeIn, damageSourceEntityIn);
            setDamageBypassesArmor();
            setMagicDamage();
        }
    }
}
