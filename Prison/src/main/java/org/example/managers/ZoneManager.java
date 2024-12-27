package org.example.managers;

import com.sun.tools.javac.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.example.MainClass;

import java.util.*;

public class ZoneManager implements Listener {
    private final List<Zone> zones = new ArrayList<>();
    private final MainClass mainClass;
    private final Map<UUID, Location> teleportLocations = new HashMap<>();
    private final Map<UUID, BukkitRunnable> teleportTasks = new HashMap<>();
    private final Map<UUID, Long> teleportCooldowns = new HashMap<>();
    private final Set<UUID> noFallDamagePlayers = new HashSet<>();
    private final long noFallDamageDuration = 5000;

    public ZoneManager(MainClass mainClass) {
        this.mainClass = mainClass;
        Bukkit.getPluginManager().registerEvents(this, mainClass);
    }

    public void addZone(Location corner1, Location corner2, Map<Material, Double> materialProbabilities, long delayInMinutes) {
        if (corner1.getWorld() == null || corner2.getWorld() == null) {
            return;
            }

        Zone zone = new Zone(corner1, corner2, materialProbabilities);
        zones.add(zone);
        scheduleZoneRegeneration(zone, delayInMinutes);
    }

    private void scheduleZoneRegeneration(Zone zone, long delayInMinutes) {
        new BukkitRunnable() {
            @Override
            public void run() {
                zone.regenerate();
                launchPlayersInZone(zone);
            }
        }.runTaskTimer(mainClass, 0, delayInMinutes * 1200); // 1200 тиков = 1 минута
    }

    private void launchPlayersInZone(Zone zone) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (zone.isWithinZone(player.getLocation())) {
                launchPlayer(player, zone);
            }
        }
    }

    private void launchPlayer(Player player, Zone zone) {
        // Телепортируем игрока на максимальную высоту относительно координат зоны
        Location zoneMaxHeight = zone.getMaxHeightLocation();
        Location targetLocation = zoneMaxHeight.clone().add(0, 10, 0); // Телепортируем игрока на 10 блоков выше максимальной высоты зоны

        player.teleport(targetLocation);

        // Отключаем урон от падения
        noFallDamagePlayers.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                noFallDamagePlayers.remove(player.getUniqueId());
            }
        }.runTaskLater(mainClass, noFallDamageDuration / 50); // 5000 миллисекунд = 100 тиков
    }

    public boolean isInZone(Location location) {
        for (Zone zone : zones) {
            if (zone.isWithinZone(location)) {
                return true;
            }
        }
        return false;
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
        task.runTaskLater(mainClass, 100); // 100 ticks = 5 seconds

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
        }.runTaskTimer(mainClass, 0, 5); // Show particles every 5 ticks (0.25 seconds)
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

    @EventHandler
    public void onEntityDamageFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && noFallDamagePlayers.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    public static class Zone {
        private final Location corner1;
        private final Location corner2;
        private final Map<Material, Double> materialProbabilities;
        private final Random random = new Random();

        public Zone(Location corner1, Location corner2, Map<Material, Double> materialProbabilities) {
            this.corner1 = corner1;
            this.corner2 = corner2;
            this.materialProbabilities = materialProbabilities;
        }

        public boolean isWithinZone(Location loc) {
            return loc.getX() >= Math.min(corner1.getX(), corner2.getX()) && loc.getX() <= Math.max(corner1.getX(), corner2.getX()) &&
                    loc.getY() >= Math.min(corner1.getY(), corner2.getY()) && loc.getY() <= Math.max(corner1.getY(), corner2.getY()) &&
                    loc.getZ() >= Math.min(corner1.getZ(), corner2.getZ()) && loc.getZ() <= Math.max(corner1.getZ(), corner2.getZ());
        }

        public void regenerate() {
            World world = corner1.getWorld();
            if (world == null) {
                return;
            }

            for (int x = (int) Math.min(corner1.getX(), corner2.getX()); x <= Math.max(corner1.getX(), corner2.getX()); x++) {
                for (int y = (int) Math.min(corner1.getY(), corner2.getY()); y <= Math.max(corner1.getY(), corner2.getY()); y++) {
                    for (int z = (int) Math.min(corner1.getZ(), corner2.getZ()); z <= Math.max(corner1.getZ(), corner2.getZ()); z++) {
                        Location loc = new Location(corner1.getWorld(), x, y, z);
                        Material material = getRandomMaterial();
                        loc.getBlock().setType(material);
                    }
                }
            }
        }


        private Material getRandomMaterial() {
            if (materialProbabilities.isEmpty()) {
                throw new NoSuchElementException("No materials available");
            }

            double totalProbability = materialProbabilities.values().stream().mapToDouble(Double::doubleValue).sum();
            double randomValue = random.nextDouble() * totalProbability;
            for (Map.Entry<Material, Double> entry : materialProbabilities.entrySet()) {
                randomValue -= entry.getValue();
                if (randomValue <= 0.0) {
                    return entry.getKey();
                }
            }

            // В качестве резервного варианта
            Iterator<Material> iterator = materialProbabilities.keySet().iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                throw new NoSuchElementException("No materials found for random selection");
            }
        }

        public Location getMaxHeightLocation() {
            double maxY = Math.max(corner1.getY(), corner2.getY());
            double centerX = (corner1.getX() + corner2.getX()) / 2.0;
            double centerZ = (corner1.getZ() + corner2.getZ()) / 2.0;
            return new Location(corner1.getWorld(), centerX, maxY, centerZ);
        }
    }

}
