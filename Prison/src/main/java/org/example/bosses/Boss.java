package org.example.bosses;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class Boss {
    private final String name;
    private final double health;
    private final Location spawnLocation;
    private final LivingEntity entity;

    public Boss(String name, double health, Location spawnLocation, LivingEntity entity) {
        this.name = name;
        this.health = health;
        this.spawnLocation = spawnLocation;
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
