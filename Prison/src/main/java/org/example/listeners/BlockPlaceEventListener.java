package org.example.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.example.MainClass;
import org.example.managers.ZoneManager;
import org.example.stats.PlayerStats;

import java.util.UUID;

public class BlockPlaceEventListener implements Listener {
    private PlayerStats playerStats;
    private ZoneManager zoneManager;

    public BlockPlaceEventListener(PlayerStats playerStats, ZoneManager zoneManager) {
        this.playerStats = playerStats;
        this.zoneManager = zoneManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (zoneManager.isInZone(event.getBlock().getLocation())) {
            MainClass.getInstance().getScoreboardManager().updateScoreboard(player);
        } else if (player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(false);
        } else {
            event.setCancelled(true);
        }
    }
}
