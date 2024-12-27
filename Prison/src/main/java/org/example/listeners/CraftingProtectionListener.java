package org.example.listeners;

import com.sun.source.tree.LambdaExpressionTree;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.example.MainClass;

public class CraftingProtectionListener implements Listener {
    public CraftingProtectionListener(MainClass plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        if(event.getRecipe() == null) { return; }
        ItemStack result = event.getRecipe().getResult();
        if (result.getType() == Material.OAK_PLANKS ||
                result.getType() == Material.SPRUCE_PLANKS ||
                result.getType() == Material.BIRCH_PLANKS ||
                result.getType() == Material.JUNGLE_PLANKS ||
                result.getType() == Material.ACACIA_PLANKS ||
                result.getType() == Material.DARK_OAK_PLANKS ||
                result.getType() == Material.MANGROVE_PLANKS) {

            for (HumanEntity player : event.getViewers()) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }
}
