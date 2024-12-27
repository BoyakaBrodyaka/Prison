package org.example.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.MainClass;
import org.example.stats.NumberFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class BossManager implements Listener {
    private final MainClass plugin;
    private final Map<UUID, Boss> bosses;
    private Boss activeBoss;
    private long bossRespawnCooldown = 20L; // По умолчанию 10 минут (12000 тиков)
    private final Logger logger;

    public BossManager(MainClass plugin) {
        this.plugin = plugin;
        this.bosses = new HashMap<>();
        this.logger = plugin.getLogger();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void setBossRespawnCooldown(long ticks) {
        this.bossRespawnCooldown = ticks;
    }

    public void startBossSpawnTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeBoss == null || activeBoss.getEntity().isDead()) {
                    spawnBoss("Мертвец", new Location(Bukkit.getWorld("PrisonServer"), 34, 69, 59), EntityType.ZOMBIE, 20);
                    if (activeBoss != null) {
                        notifyPlayers("§6§lБосс §c§lМертвец §6заспавнен с §c" + activeBoss.getEntity().getHealth() + "§6 ХП!");
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, bossRespawnCooldown);
    }

    public void spawnBoss(String bossName, Location location, EntityType entityType, double health) {
        World world = location.getWorld();
        LivingEntity bossEntity = (LivingEntity) world.spawnEntity(location, entityType);

        bossEntity.setCustomName(bossName + " §c§l[" + health + "§c ХП]");

        // Установим максимальное здоровье
        bossEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        bossEntity.setHealth(health);
        bossEntity.setRemoveWhenFarAway(false); // Отключение удаления при удалении игрока

        Boss boss = new Boss(bossName, health, location, bossEntity);
        bosses.put(bossEntity.getUniqueId(), boss);
        activeBoss = boss;

    }

    public void notifyPlayers(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    @EventHandler
    public void onBossDamage(EntityDamageByEntityEvent event) {
        if (activeBoss != null && event.getEntity().getUniqueId().equals(activeBoss.getEntity().getUniqueId())) {
            LivingEntity bossEntity = activeBoss.getEntity();
            double newHealth = bossEntity.getHealth() - event.getDamage();
            newHealth = Math.max(0, newHealth); // Убедимся, что здоровье не уйдет в отрицательные значения
            bossEntity.setHealth(newHealth);
            bossEntity.setCustomName(activeBoss.getName() + " §c§l[" + newHealth + "/" + bossEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + "§c ХП]");

            if (newHealth <= 0) {
                activeBoss = null;
            }
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        if (bosses.containsKey(event.getEntity().getUniqueId())) {
            event.getDrops().clear(); // Очищаем дропы
            bosses.remove(event.getEntity().getUniqueId());
            activeBoss = null;
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.getHeight() < 1.95) { // Проверка на младших зомби по их высоте
                entity.remove(); // Удаляем младших зомби
            }
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if (activeBoss != null && event.getEntity().getUniqueId().equals(activeBoss.getEntity().getUniqueId())) {
            event.setCancelled(true); // Отменяем горение босса
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (activeBoss != null && event.getEntity().getUniqueId().equals(activeBoss.getEntity().getUniqueId())) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                    event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                    event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                event.setCancelled(true); // Отменяем урон от огня, лавы и огненных тиков
            }
        }
    }
}



