package org.example.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.example.MainClass;

public class LogProtectionListener implements Listener {
    private MainClass plugin;

    public LogProtectionListener(MainClass plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.OAK_LOG ||
                            event.getClickedBlock().getType() == Material.OAK_WOOD ||
                            event.getClickedBlock().getType() == Material.SPRUCE_LOG ||
                            event.getClickedBlock().getType() == Material.SPRUCE_WOOD ||
                            event.getClickedBlock().getType() == Material.BIRCH_LOG ||
                            event.getClickedBlock().getType() == Material.BIRCH_WOOD ||
                            event.getClickedBlock().getType() == Material.DARK_OAK_LOG ||
                            event.getClickedBlock().getType() == Material.DARK_OAK_WOOD ||
                            event.getClickedBlock().getType() == Material.JUNGLE_WOOD ||
                            event.getClickedBlock().getType() == Material.JUNGLE_LOG ||
                            event.getClickedBlock().getType() == Material.ACACIA_LOG ||
                            event.getClickedBlock().getType() == Material.ACACIA_WOOD ||
                            event.getClickedBlock().getType() == Material.MANGROVE_LOG ||
                            event.getClickedBlock().getType() == Material.MANGROVE_WOOD) {
                        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE ||
                                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STONE_AXE ||
                                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_AXE ||
                                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE ||
                                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE ||
                                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NETHERITE_AXE) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
