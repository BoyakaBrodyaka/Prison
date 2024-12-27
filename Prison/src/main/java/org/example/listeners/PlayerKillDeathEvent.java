package org.example.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.MainClass;
import org.example.donate.boosters.BoosterManager;
import org.example.managers.InventoryManager;
import org.example.stats.NumberFormatter;
import org.example.stats.PlayerStats;

import java.util.*;

public class PlayerKillDeathEvent implements Listener {
    private final PlayerStats playerStats;
    private final MainClass plugin;
    private final InventoryManager inventoryManager;
    private final BoosterManager boosterManager;
    private final Map<UUID, List<ItemStack>> savedItems = new HashMap<>();

    public PlayerKillDeathEvent(PlayerStats playerStats, MainClass plugin, InventoryManager inventoryManager, BoosterManager boosterManager) {
        this.playerStats = playerStats;
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.boosterManager = boosterManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deceased = event.getEntity();
        Player killer = deceased.getKiller();

        // Добавляем +1 к числу убийств
        if (killer != null) {
            UUID killerUUID = killer.getUniqueId();
            double deceasedBalance = playerStats.getMoney(deceased.getUniqueId());
            double amountToTransfer = deceasedBalance * 0.01;
            playerStats.addMoney(killerUUID, amountToTransfer);
            playerStats.removeMoney(deceased.getUniqueId(), amountToTransfer);

            // Получаем множитель для киллов
            double multiplier = boosterManager.getBoosterMultiplier(killer, "киллов");
            int newKillCount = playerStats.getKills(killerUUID) + (int) multiplier;
            playerStats.setKills(killerUUID, newKillCount);

            killer.sendMessage(ChatColor.GREEN + "Вы убили " + ChatColor.RED + deceased.getName() + ChatColor.RESET + "! "  + ChatColor.GREEN + "Вы получили " + NumberFormatter.format(amountToTransfer) + " от его баланса!");
            deceased.sendMessage(ChatColor.RED + "Вас убил игрок " + killer.getName() + "! Вы потеряли " + NumberFormatter.format(amountToTransfer) + " от вашего баланса");
        }

        // Фильтруем выпадающие предметы и сохраняем инструменты и броню
        List<ItemStack> toSave = new ArrayList<>();
        Iterator<ItemStack> iter = event.getDrops().iterator();

        while (iter.hasNext()) {
            ItemStack item = iter.next();
            if (isProtectedItem(item)) {
                toSave.add(item);
                iter.remove();
            }
        }

        // Сохраняем предметы для последующего возврата при возрождении
        savedItems.put(deceased.getUniqueId(), toSave);
        inventoryManager.saveInventory(deceased);
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Возвращаем сохраненные предметы
        if (savedItems.containsKey(playerId)) {
            List<ItemStack> items = savedItems.get(playerId);
            for (ItemStack item : items) {
                player.getInventory().addItem(item);
            }
            savedItems.remove(playerId);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 0)); // 1200 тиков = 1 минута
            }
        }.runTaskLater(plugin, 1); // Задержка на 1 тик
    }

    private boolean isProtectedItem(ItemStack item) {
        if (item == null) return false;

        Material material = item.getType();
        return material.name().endsWith("_SWORD") ||
                material.name().endsWith("_AXE") ||
                material.name().endsWith("_PICKAXE") ||
                material.name().endsWith("_SHOVEL") ||
                material.name().endsWith("_HOE") ||
                material.name().endsWith("_HELMET") ||
                material.name().endsWith("_CHESTPLATE") ||
                material.name().endsWith("_LEGGINGS") ||
                material.name().endsWith("_BOOTS") ||
                material.name().endsWith("SHEARS");
    }
}
