package org.example.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.MainClass;
import org.example.managers.PvPModeManager;

public class ToolDamageListener implements Listener {
    private final MainClass plugin;
    private final PvPModeManager pvpModeManager;

    public ToolDamageListener(MainClass plugin, PvPModeManager pvpModeManager) {
        this.plugin = plugin;
        this.pvpModeManager = pvpModeManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                ItemStack itemInHand = damager.getInventory().getItemInMainHand();
                if (itemInHand != null && isProtectedTool(itemInHand.getType())) {
                    event.setCancelled(true); // Отменяем нанесение урона
                    pvpModeManager.deactivatePvPMode(player); // Деактивируем PvP-режим для игрока
                    pvpModeManager.deactivatePvPMode(damager); // Деактивируем PvP-режим для нападающего
                }
            }
        }
    }

    private boolean isProtectedTool(Material material) {
        return material == Material.WOODEN_SHOVEL || material == Material.STONE_SHOVEL || material == Material.IRON_SHOVEL ||
                material == Material.GOLDEN_SHOVEL || material == Material.DIAMOND_SHOVEL || material == Material.NETHERITE_SHOVEL ||
                material == Material.WOODEN_PICKAXE || material == Material.STONE_PICKAXE || material == Material.IRON_PICKAXE ||
                material == Material.GOLDEN_PICKAXE || material == Material.DIAMOND_PICKAXE || material == Material.NETHERITE_PICKAXE ||
                material == Material.WOODEN_AXE || material == Material.STONE_AXE || material == Material.IRON_AXE ||
                material == Material.GOLDEN_AXE || material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE ||
                material == Material.WOODEN_HOE || material == Material.STONE_HOE || material == Material.IRON_HOE ||
                material == Material.GOLDEN_HOE || material == Material.DIAMOND_HOE || material == Material.NETHERITE_HOE;
    }
}
