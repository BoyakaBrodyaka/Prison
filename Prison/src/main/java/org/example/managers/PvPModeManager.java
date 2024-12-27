package org.example.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.MainClass;

import java.util.*;

public class PvPModeManager implements Listener {
    private final Map<UUID, Long> pvpTimers = new HashMap<>();
    private final Map<UUID, BossBar> pvpBossBars = new HashMap<>();
    private final long PVP_DURATION = 15 * 1000;
    private final Map<UUID, Boolean> playerPvPModes;
    private final MainClass plugin; // Ссылка на основной плагин
    private final PvPManager pvpManager; // Менеджер зон ПВП и безопасных зон

    public PvPModeManager(MainClass plugin, PvPManager pvpManager) {
        this.plugin = plugin;
        this.pvpManager = pvpManager;
        playerPvPModes = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void clearPvPModes() {
        pvpTimers.clear();
        if (playerPvPModes != null) {
            playerPvPModes.clear();
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            if (pvpManager.canPlayerPvP(damager) && pvpManager.canPlayerPvP(victim)) {
                activatePvPMode(damager);
                activatePvPMode(victim);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (isInPvPMode(player)) {
            player.sendMessage(ChatColor.RED + "Во время ПВП запрещено телепортироваться!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInPvPMode(player)) {
            player.setHealth(0); // Убиваем игрока, если он выходит из игры во время ПВП
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (isInPvPMode(player)) {
            player.setHealth(0);
        }
    }

    private void activatePvPMode(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (pvpTimers.containsKey(playerUUID)) {
            return; // ПВП-режим уже активен
        }
        pvpTimers.put(playerUUID, System.currentTimeMillis());

        BossBar bossBar = Bukkit.createBossBar(ChatColor.RED + "ПВП-Режим", BarColor.RED, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);
        pvpBossBars.put(playerUUID, bossBar);

        new BukkitRunnable() {
            @Override
            public void run() {
                Long startTime = pvpTimers.get(playerUUID);
                if (startTime == null) {
                    this.cancel(); // Останавливаем задачу, если игрок больше не в ПВП-режиме
                    return;
                }
                long elapsedTime = System.currentTimeMillis() - startTime;
                double progress = 1.0 - ((double) elapsedTime / PVP_DURATION);
                if (progress <= 0) {
                    pvpTimers.remove(playerUUID);
                    bossBar.removeAll();
                    pvpBossBars.remove(playerUUID);
                    this.cancel(); // Останавливаем задачу
                } else {
                    bossBar.setProgress(progress);
                    bossBar.setTitle(ChatColor.RED + "ПВП-Режим: " + (PVP_DURATION - elapsedTime) / 1000 + " секунд");
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private boolean isInPvPMode(Player player) {
        Long startTime = pvpTimers.get(player.getUniqueId());
        if (startTime == null) {
            return false;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > PVP_DURATION) {
            pvpTimers.remove(player.getUniqueId());
            BossBar bossBar = pvpBossBars.remove(player.getUniqueId());
            if (bossBar != null) {
                bossBar.removeAll();
            }
            return false;
        }
        return true;
    }

    public void setPvPMode(UUID playerUUID, boolean isEnabled) {
        if (!isEnabled) {
            pvpTimers.remove(playerUUID);
            BossBar bossBar = pvpBossBars.remove(playerUUID);
            if (bossBar != null) {
                bossBar.removeAll();
            }
        } else {
            activatePvPMode(Bukkit.getPlayer(playerUUID));
        }
        playerPvPModes.put(playerUUID, isEnabled);
    }

    public boolean getPvPMode(UUID playerUUID) {
        return playerPvPModes.getOrDefault(playerUUID, false);
    }

    public void deactivatePvPMode(Player player) {
        UUID playerUUID = player.getUniqueId();
        pvpTimers.remove(playerUUID);
        BossBar bossBar = pvpBossBars.remove(playerUUID);
        if (bossBar != null) {
            bossBar.removeAll();
        }
        playerPvPModes.put(playerUUID, false);
    }
}
