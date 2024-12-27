package org.example.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.MainClass;

public class giveMenuManager implements Listener {
    public static void startDisplayingMessages(MainClass plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setFoodLevel(20);
                    clearClocks(player);
                    giveStartingItems(player);
            }
        }, 0L, 20L); // Обновление каждую секунду (20 тиков)
    }

    public static void giveStartingItems(Player player) {
        ItemStack bone = new ItemStack(Material.PAPER);
        ItemMeta boneMeta = bone.getItemMeta();
        boneMeta.setDisplayName("§aМеню");
        bone.setItemMeta(boneMeta);
        player.getInventory().setItem(8, bone);
    }

    private static void clearClocks(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.CLOCK) {
                player.getInventory().remove(item);
            }
        }
    }
}
