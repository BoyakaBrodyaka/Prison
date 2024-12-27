package org.example.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.MainClass;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnCommandManager implements CommandExecutor, Listener {
    private final MainClass plugin;
    private final Map<UUID, Location> teleportLocations = new HashMap<>();
    private final Map<UUID, BukkitRunnable> teleportTasks = new HashMap<>();
    private final Map<UUID, Long> teleportCooldowns = new HashMap<>();

    public SpawnCommandManager(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
                teleportWithDelay(player, new Location(player.getWorld(), -11 ,65 ,3));
        }
        return true;
    }

    public void teleportWithDelay(Player player, Location targetLocation) {
        UUID playerUUID = player.getUniqueId();

        // Check cooldown
        long currentTime = System.currentTimeMillis();
        if (teleportCooldowns.containsKey(playerUUID)) {
            long lastTeleportTime = teleportCooldowns.get(playerUUID);
            if (currentTime - lastTeleportTime < 30000) { // 30 seconds cooldown
                long remainingTime = (30000 - (currentTime - lastTeleportTime)) / 1000;
                player.sendMessage(ChatColor.RED + "Вы должны подождать - " + remainingTime + " секунд.");
                return;
            }
        }
        teleportCooldowns.put(playerUUID, currentTime);

        player.sendMessage(ChatColor.YELLOW + "Телепортация начнется через 5 секунд. Пожалуйста, не двигайтесь.");
        Location originalLocation = player.getLocation().clone();
        teleportLocations.put(playerUUID, originalLocation);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (teleportLocations.containsKey(playerUUID)) {
                    if (player.isOnline() && hasNotMoved(originalLocation, player.getLocation())) {
                        player.teleport(targetLocation);
                        player.sendMessage(ChatColor.GREEN + "Вы успешно телепортированы.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Телепортация прервана, поскольку вы двинулись.");
                    }
                    teleportLocations.remove(playerUUID);
                    teleportTasks.remove(playerUUID);
                }
            }
        };
        teleportTasks.put(playerUUID, task);
        task.runTaskLater(plugin, 100); // 100 ticks = 5 seconds

        // Particle effect during teleport delay
        new BukkitRunnable() {
            @Override
            public void run() {
                if (teleportLocations.containsKey(playerUUID)) {
                    if (player.isOnline() && hasNotMoved(originalLocation, player.getLocation())) {
                        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation(), 100, 0.5, 1, 0.5, 0);
                    } else {
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 5); // Show particles every 5 ticks (0.25 seconds)
    }

    private boolean hasNotMoved(Location originalLocation, Location currentLocation) {
        return originalLocation.getBlockX() == currentLocation.getBlockX()
                && originalLocation.getBlockY() == currentLocation.getBlockY()
                && originalLocation.getBlockZ() == currentLocation.getBlockZ();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (teleportLocations.containsKey(playerUUID)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (hasNotMoved(from, to)) {
                return; // Allow head movement and rotation
            }

            BukkitRunnable task = teleportTasks.remove(playerUUID);
            if (task != null) {
                task.cancel();
            }
            teleportLocations.remove(playerUUID);
            player.sendMessage(ChatColor.RED + "Телепортация прервана, поскольку вы двинулись.");
        }
    }
}
