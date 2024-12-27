package org.example.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerDropEventListener implements Listener {
    private final Set<Material> blockedItems = new HashSet<>();

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        blockedItems.add(Material.WOODEN_SWORD);
        blockedItems.add(Material.STONE_SWORD);
        blockedItems.add(Material.IRON_SWORD);
        blockedItems.add(Material.DIAMOND_SWORD);
        blockedItems.add(Material.GOLDEN_SWORD);
        blockedItems.add(Material.NETHERITE_SWORD);
        blockedItems.add(Material.WOODEN_PICKAXE);
        blockedItems.add(Material.STONE_PICKAXE);
        blockedItems.add(Material.IRON_PICKAXE);
        blockedItems.add(Material.DIAMOND_PICKAXE);
        blockedItems.add(Material.NETHERITE_PICKAXE);
        blockedItems.add(Material.WOODEN_AXE);
        blockedItems.add(Material.STONE_AXE);
        blockedItems.add(Material.IRON_AXE);
        blockedItems.add(Material.DIAMOND_AXE);
        blockedItems.add(Material.GOLDEN_AXE);
        blockedItems.add(Material.NETHERITE_AXE);
        blockedItems.add(Material.WOODEN_SHOVEL);
        blockedItems.add(Material.STONE_SHOVEL);
        blockedItems.add(Material.IRON_SHOVEL);
        blockedItems.add(Material.DIAMOND_SHOVEL);
        blockedItems.add(Material.GOLDEN_SHOVEL);
        blockedItems.add(Material.NETHERITE_SHOVEL);
        blockedItems.add(Material.LEATHER_HELMET);
        blockedItems.add(Material.LEATHER_CHESTPLATE);
        blockedItems.add(Material.LEATHER_LEGGINGS);
        blockedItems.add(Material.LEATHER_BOOTS);
        blockedItems.add(Material.IRON_BOOTS);
        blockedItems.add(Material.DIAMOND_BOOTS);
        blockedItems.add(Material.GOLDEN_BOOTS);
        blockedItems.add(Material.NETHERITE_BOOTS);
        blockedItems.add(Material.CHAINMAIL_BOOTS);
        blockedItems.add(Material.IRON_LEGGINGS);
        blockedItems.add(Material.DIAMOND_LEGGINGS);
        blockedItems.add(Material.GOLDEN_LEGGINGS);
        blockedItems.add(Material.NETHERITE_LEGGINGS);
        blockedItems.add(Material.CHAINMAIL_LEGGINGS);
        blockedItems.add(Material.IRON_HELMET);
        blockedItems.add(Material.DIAMOND_HELMET);
        blockedItems.add(Material.GOLDEN_HELMET);
        blockedItems.add(Material.NETHERITE_HELMET);
        blockedItems.add(Material.CHAINMAIL_HELMET);
        blockedItems.add(Material.IRON_CHESTPLATE);
        blockedItems.add(Material.DIAMOND_CHESTPLATE);
        blockedItems.add(Material.GOLDEN_CHESTPLATE);
        blockedItems.add(Material.NETHERITE_CHESTPLATE);
        blockedItems.add(Material.CHAINMAIL_CHESTPLATE);
        blockedItems.add(Material.PAPER);

        Player player = event.getPlayer();
        Material droppedItem = event.getItemDrop().getItemStack().getType();
        if (player.getGameMode() != GameMode.CREATIVE) {
                if (blockedItems.contains(droppedItem)) {
                    event.setCancelled(true);
                }
        } else {
            event.setCancelled(false);
        }
    }
}
