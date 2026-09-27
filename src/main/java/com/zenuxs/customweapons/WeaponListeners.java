package com.zenuxs.customweapons;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponListeners implements Listener {

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.getType() == Material.DIAMOND_SWORD) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("Lightning Sword")) {
                    event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow arrow) {
            if (arrow.getShooter() instanceof Player player) {
                ItemStack bow = player.getInventory().getItemInMainHand();
                if (bow != null && bow.getType() == Material.BOW) {
                    ItemMeta meta = bow.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("Explosive Bow")) {
                        arrow.getWorld().createExplosion(arrow.getLocation(), 2.5f, false, false);
                        arrow.remove();
                    }
                }
            }
        }
    }
}
