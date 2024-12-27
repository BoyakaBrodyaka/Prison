package org.example.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PvPManager {
    private final Set<ZonePvP> pvpZones = new HashSet<>();
    private final Set<ZonePvP> safeZones = new HashSet<>();

    public void addPvPZone(Location corner1, Location corner2) {
        pvpZones.add(new ZonePvP(corner1, corner2));
    }

    public void addSafeZone(Location corner1, Location corner2) {
        safeZones.add(new ZonePvP(corner1, corner2));
    }

    public boolean isInPvPZone(Location location) {
        return pvpZones.stream().anyMatch(zone -> zone.isWithinZone(location));
    }

    public boolean isInSafeZone(Location location) {
        return safeZones.stream().anyMatch(zone -> zone.isWithinZone(location));
    }

    public boolean canPlayerPvP(Player player) {
        Location location = player.getLocation();
        return isInPvPZone(location) && !isInSafeZone(location);
    }

    public static class ZonePvP {
        private final Location corner1;
        private final Location corner2;

        public ZonePvP(Location corner1, Location corner2) {
            this.corner1 = corner1;
            this.corner2 = corner2;
        }

        public boolean isWithinZone(Location loc) {
            return loc.getX() >= Math.min(corner1.getX(), corner2.getX()) &&
                    loc.getX() <= Math.max(corner1.getX(), corner2.getX()) &&
                    loc.getY() >= Math.min(corner1.getY(), corner2.getY()) &&
                    loc.getY() <= Math.max(corner1.getY(), corner2.getY()) &&
                    loc.getZ() >= Math.min(corner1.getZ(), corner2.getZ()) &&
                    loc.getZ() <= Math.max(corner1.getZ(), corner2.getZ());
        }
    }
}
