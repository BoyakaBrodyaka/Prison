package org.example.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.example.MainClass;
import org.example.managers.PvPModeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerKillEventListener implements Listener {
    private final PvPModeManager pvpModeManager;
    private final Map<UUID, BossBar> playerBossBars;

    public PlayerKillEventListener(PvPModeManager pvpModeManager) {
        this.pvpModeManager = pvpModeManager;
        this.playerBossBars = new HashMap<>();
    }

    public void addPlayerBossBar(UUID playerUUID, BossBar bossBar) {
        playerBossBars.put(playerUUID, bossBar);
    }

    public void removePlayerBossBar(UUID playerUUID) {
        BossBar bossBar = playerBossBars.remove(playerUUID);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();

        // Clear all BossBars for the player
        removePlayerBossBar(playerUUID);

        // Clear PvP Modes for the player
        pvpModeManager.clearPvPModes();
    }
}
