package org.example.bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.example.MainClass;

public class InventoryListener implements Listener {
    private final MainClass plugin;

    public InventoryListener(MainClass plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Боссы")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();

            if (slot >= 0 && slot < 9) {
                int requiredLevel = (slot + 1) * 3;
                if (player.getLevel() >= requiredLevel) {
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        plugin.getBossManager().notifyPlayers("A boss will spawn in " + (slot + 1) * 3 + " minutes.");
                        Location bossLocation = new Location(Bukkit.getWorld("world"), 100, 64, 100 + slot * 10);
                        plugin.getBossManager().spawnBoss("Boss " + (slot + 1) * 3 + " Level", bossLocation, EntityType.ZOMBIE, 1000);
                    }, 100L);
                } else {
                    player.sendMessage("You need to be at least level " + requiredLevel + " to fight this boss.");
                }
            }
        }
    }
}
