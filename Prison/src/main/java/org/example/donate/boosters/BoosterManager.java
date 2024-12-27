package org.example.donate.boosters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.MainClass;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class BoosterManager implements Listener {
    private final MainClass plugin;
    private final Map<String, Map<String, Long>> localBoosters = new HashMap<>();
    private final Map<String, Long> globalBoosters = new HashMap<>();
    private final Map<String, BossBar> playerBossBars = new HashMap<>();
    private final Map<String, BossBar> globalBossBars = new HashMap<>();
    private final Map<String, Long> commissionDisabledUntil = new HashMap<>();
    private final File dataFile;
    private final FileConfiguration config;
    private final Map<String, String> globalBoosterPlayerNames = new HashMap<>();

    public BoosterManager(MainClass plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "boosters.yml");
        this.config = YamlConfiguration.loadConfiguration(dataFile);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        loadBoosters();
    }

    public void saveBoosters() {
        for (Map.Entry<String, Map<String, Long>> entry : localBoosters.entrySet()) {
            String playerUUID = entry.getKey();
            for (Map.Entry<String, Long> booster : entry.getValue().entrySet()) {
                config.set("local." + playerUUID + "." + booster.getKey(), booster.getValue());
            }
        }
        for (Map.Entry<String, Long> entry : globalBoosters.entrySet()) {
            config.set("global." + entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Long> entry : commissionDisabledUntil.entrySet()) {
            config.set("commission." + entry.getKey(), entry.getValue());
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBoosters() {
        if (config.contains("local")) {
            for (String playerUUID : config.getConfigurationSection("local").getKeys(false)) {
                for (String boosterType : config.getConfigurationSection("local." + playerUUID).getKeys(false)) {
                    long endTime = config.getLong("local." + playerUUID + "." + boosterType);
                    localBoosters.putIfAbsent(playerUUID, new HashMap<>());
                    localBoosters.get(playerUUID).put(boosterType, endTime);
                }
            }
        }
        if (config.contains("global")) {
            for (String boosterType : config.getConfigurationSection("global").getKeys(false)) {
                long endTime = config.getLong("global." + boosterType);
                globalBoosters.put(boosterType, endTime);
            }
        }
        if (config.contains("commission")) {
            for (String playerUUID : config.getConfigurationSection("commission").getKeys(false)) {
                long endTime = config.getLong("commission." + playerUUID);
                commissionDisabledUntil.put(playerUUID, endTime);
            }
        }
        startBoosters();
    }

    public void startBoosters() {
        long currentTime = System.currentTimeMillis();

        Iterator<Map.Entry<String, Map<String, Long>>> localIterator = localBoosters.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry<String, Map<String, Long>> entry = localIterator.next();
            String playerUUID = entry.getKey();
            Iterator<Map.Entry<String, Long>> boosterIterator = entry.getValue().entrySet().iterator();
            while (boosterIterator.hasNext()) {
                Map.Entry<String, Long> booster = boosterIterator.next();
                String type = booster.getKey();
                long endTime = booster.getValue();
                if (endTime > currentTime) {
                    continueLocalBooster(playerUUID, type, endTime);
                } else {
                    boosterIterator.remove();
                }
            }
            if (entry.getValue().isEmpty()) {
                localIterator.remove();
            }
        }

        Iterator<Map.Entry<String, Long>> globalIterator = globalBoosters.entrySet().iterator();
        while (globalIterator.hasNext()) {
            Map.Entry<String, Long> entry = globalIterator.next();
            String type = entry.getKey();
            long endTime = entry.getValue();
            if (endTime > currentTime) {
                continueGlobalBooster(type, endTime, "");
            } else {
                globalIterator.remove();
            }
        }
    }

    private void continueLocalBooster(String playerUUID, String type, long endTime) {
        BossBar bossBar = Bukkit.createBossBar(ChatColor.GREEN + "Локальный бустер " + type, BarColor.GREEN, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        if (player != null) {
            bossBar.addPlayer(player);
        }
        playerBossBars.put(playerUUID + "_" + type, bossBar);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= endTime) {
                    localBoosters.get(playerUUID).remove(type);
                    if (player != null) {
                        player.sendMessage(ChatColor.RED + "Локальный бустер " + type + " истек.");
                        bossBar.removePlayer(player);
                    }
                    playerBossBars.remove(playerUUID + "_" + type);
                    cancel();
                    saveBoosters();
                } else {
                    double progress = (endTime - System.currentTimeMillis()) / (30.0 * 60.0 * 1000.0);
                    bossBar.setProgress(progress);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void continueGlobalBooster(String type, long endTime, String playerName) {
        BossBar bossBar = Bukkit.createBossBar(ChatColor.GREEN + "Глобальный бустер " + type + " запущен " + playerName, BarColor.BLUE, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
        globalBossBars.put(type, bossBar);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= endTime) {
                    globalBoosters.remove(type);
                    Bukkit.broadcastMessage(ChatColor.RED + "Глобальный бустер " + type + " игрока " + playerName + " истек.");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        bossBar.removePlayer(player);
                    }
                    globalBossBars.remove(type);
                    globalBoosterPlayerNames.remove(type);
                    cancel();
                    saveBoosters();
                } else {
                    double progress = (endTime - System.currentTimeMillis()) / (30.0 * 60.0 * 1000.0);
                    bossBar.setProgress(progress);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void activateLocalBooster(Player player, String type) {
        String playerUUID = player.getUniqueId().toString();
        if (isLocalBoosterActive(player, type)) {
            return;
        }

        long endTime = System.currentTimeMillis() + 30 * 60 * 1000;
        localBoosters.putIfAbsent(playerUUID, new HashMap<>());
        localBoosters.get(playerUUID).put(type, endTime);
        player.sendMessage(ChatColor.GREEN + "Локальный бустер " + type + " активирован!");
        saveBoosters();

        BossBar bossBar = Bukkit.createBossBar(ChatColor.GREEN + "Локальный бустер " + type, BarColor.GREEN, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);
        playerBossBars.put(playerUUID + "_" + type, bossBar);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= endTime) {
                    localBoosters.get(playerUUID).remove(type);
                    player.sendMessage(ChatColor.RED + "Локальный бустер " + type + " истек.");
                    bossBar.removePlayer(player);
                    playerBossBars.remove(playerUUID + "_" + type);
                    cancel();
                    saveBoosters();
                } else {
                    double progress = (endTime - System.currentTimeMillis()) / (30.0 * 60.0 * 1000.0);
                    bossBar.setProgress(progress);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void activateGlobalBooster(Player player, String type) {
        if (isGlobalBoosterActive(type)) {
            return;
        }

        String playerName = player.getName();
        globalBoosterPlayerNames.put(type, playerName);
        long endTime = System.currentTimeMillis() + 30 * 60 * 1000;
        globalBoosters.put(type, endTime);
        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " активировал глобальный бустер " + type + "!");
        saveBoosters();

        BossBar bossBar = Bukkit.createBossBar(ChatColor.GREEN + "Глобальный бустер " + type + " запущен " + playerName, BarColor.BLUE, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(onlinePlayer);
        }
        globalBossBars.put(type, bossBar);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= endTime) {
                    globalBoosters.remove(type);
                    Bukkit.broadcastMessage(ChatColor.RED + "Глобальный бустер " + type + " игрока " + player + " истек.");
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        bossBar.removePlayer(onlinePlayer);
                    }
                    globalBossBars.remove(type);
                    globalBoosterPlayerNames.remove(type);
                    cancel();
                    saveBoosters();
                } else {
                    double progress = (endTime - System.currentTimeMillis()) / (30.0 * 60.0 * 1000.0);
                    bossBar.setProgress(progress);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void disableCommission(Player player) {
        long endTime = System.currentTimeMillis() + 60 * 60 * 1000;
        String playerUUID = player.getUniqueId().toString();
        commissionDisabledUntil.put(playerUUID, endTime);
        player.sendMessage(ChatColor.GREEN + "Комиссия отключена на 1 час!");
        saveBoosters();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= endTime) {
                    commissionDisabledUntil.remove(playerUUID);
                    player.sendMessage(ChatColor.RED + "Время отключения комиссии истекло.");
                    cancel();
                    saveBoosters();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public boolean isLocalBoosterActive(Player player, String type) {
        String playerUUID = player.getUniqueId().toString();
        return localBoosters.containsKey(playerUUID) &&
                localBoosters.get(playerUUID).containsKey(type) &&
                localBoosters.get(playerUUID).get(type) > System.currentTimeMillis();
    }

    public boolean isGlobalBoosterActive(String type) {
        return globalBoosters.containsKey(type) && globalBoosters.get(type) > System.currentTimeMillis();
    }

    public double getBoosterMultiplier(Player player, String type) {
        String playerUUID = player.getUniqueId().toString();
        boolean localActive = isLocalBoosterActive(player, type);
        boolean globalActive = isGlobalBoosterActive(type);

        if (localActive && globalActive) {
            return 4.0; // Если оба бустера активны, множитель равен 4
        } else if (localActive || globalActive) {
            return 2.0; // Если активен один из бустеров, множитель равен 2
        } else {
            return 1.0; // Если бустеры не активны, множитель равен 1
        }
    }

    public boolean isCommissionDisabled(Player player) {
        String playerUUID = player.getUniqueId().toString();
        return commissionDisabledUntil.containsKey(playerUUID) &&
                commissionDisabledUntil.get(playerUUID) > System.currentTimeMillis();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Map.Entry<String, BossBar> entry : globalBossBars.entrySet()) {
            BossBar bossBar = entry.getValue();
            bossBar.addPlayer(player);

            // Обновляем имя игрока в боссбаре при каждом заходе игрока
            String type = entry.getKey();
            String playerName = globalBoosterPlayerNames.get(type);
            if (playerName != null) {
                bossBar.setTitle(ChatColor.GREEN + "Глобальный бустер " + type + " запущен " + playerName);
            }
        }
    }

    public void disableAllBoosters() {
        // Отключаем все локальные бустеры
        for (Map.Entry<String, Map<String, Long>> entry : localBoosters.entrySet()) {
            String playerUUID = entry.getKey();
            for (Map.Entry<String, Long> booster : entry.getValue().entrySet()) {
                String type = booster.getKey();
                BossBar bossBar = playerBossBars.remove(playerUUID + "_" + type);
                if (bossBar != null) {
                    bossBar.removeAll();
                }
            }
        }
        localBoosters.clear();

        // Отключаем все глобальные бустеры
        for (Map.Entry<String, BossBar> entry : globalBossBars.entrySet()) {
            BossBar bossBar = entry.getValue();
            bossBar.removeAll();
        }
        globalBoosters.clear();
        globalBossBars.clear();
        globalBoosterPlayerNames.clear();
        saveBoosters();
    }
}
