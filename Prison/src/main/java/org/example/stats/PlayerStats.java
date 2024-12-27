package org.example.stats;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.example.MainClass;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerStats {
    private final HashMap<UUID, Integer> blocksMined = new HashMap<>();
    private final HashMap<UUID, Double> money = new HashMap<>();
    private final HashMap<UUID, Double> offlineMoney = new HashMap<>();
    private final HashMap<UUID, Integer> levels = new HashMap<>();
    private final HashMap<UUID, Integer> ranks = new HashMap<>();
    private final HashMap<UUID, Integer> kills = new HashMap<>();
    private final File dataFile;
    private final Map<UUID, ItemStack[]> inventories = new HashMap<>();
    private final File offlineMoneyFile;
    private final FileConfiguration offlineMoneyConfig;
    private final MainClass plugin;

    public PlayerStats(File dataFolder, MainClass plugin) {
        this.plugin = plugin;
        // Ensure data folder exists
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        dataFile = new File(dataFolder, "playerStats.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadStats();

        this.offlineMoneyFile = new File(plugin.getDataFolder(), "offline_money.yml");
        this.offlineMoneyConfig = YamlConfiguration.loadConfiguration(offlineMoneyFile);
        loadOfflineMoney();
    }

    public void initializePlayer(UUID playerUUID) {
        blocksMined.put(playerUUID, 0);
        money.put(playerUUID, 0.0);
        levels.put(playerUUID, 1);
        ranks.put(playerUUID, 0);
        kills.put(playerUUID, 0);
    }

    public boolean hasPlayer(UUID playerUUID) {
        return blocksMined.containsKey(playerUUID);
    }

    public int getBlocksMined(UUID playerUUID) {
        return blocksMined.getOrDefault(playerUUID, 0);
    }

    public void setBlocksMined(UUID playerUUID, int blocks) {
        blocksMined.put(playerUUID, blocks);
        saveStatsForPlayer(playerUUID);
    }

    public void addBlocks(UUID playerUUID, int amount) {
        blocksMined.put(playerUUID, blocksMined.get(playerUUID) + amount);
    }

    public double getMoney(UUID playerUUID) {
        return money.getOrDefault(playerUUID, 0.0);
    }

    public void setMoney(UUID playerUUID, double amount) {
        money.put(playerUUID, amount);
        saveStatsForPlayer(playerUUID);
    }

    public void addMoney(UUID playerUUID, double amount) {
        money.put(playerUUID, money.get(playerUUID) + amount);
    }

    public void removeMoney(UUID playerUUID, double amount) {
        money.put(playerUUID, money.get(playerUUID) - amount);
    }

    public void addOfflineMoney(UUID playerId, double amount) {
        offlineMoney.put(playerId, offlineMoney.getOrDefault(playerId, 0.0) + amount);
        saveOfflineMoney();
    }

    public double getOfflineMoney(UUID playerId) {
        return offlineMoney.getOrDefault(playerId, 0.0);
    }

    public void clearOfflineMoney(UUID playerId) {
        offlineMoney.remove(playerId);
        saveOfflineMoney();
    }

    private void loadOfflineMoney() {
        if (offlineMoneyConfig.contains("offline_money")) {
            for (String key : offlineMoneyConfig.getConfigurationSection("offline_money").getKeys(false)) {
                UUID playerId = UUID.fromString(key);
                double amount = offlineMoneyConfig.getDouble("offline_money." + key);
                offlineMoney.put(playerId, amount);
            }
        }
    }

    private void saveOfflineMoney() {
        offlineMoneyConfig.set("offline_money", null);
        for (Map.Entry<UUID, Double> entry : offlineMoney.entrySet()) {
            offlineMoneyConfig.set("offline_money." + entry.getKey().toString(), entry.getValue());
        }
        try {
            offlineMoneyConfig.save(offlineMoneyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLevel(UUID playerUUID) {
        return levels.getOrDefault(playerUUID, 1);
    }

    public void setLevel(UUID playerUUID, int level) {
        levels.put(playerUUID, level);
        saveStatsForPlayer(playerUUID);
    }

    public int getRank(UUID playerUUID) {
        return ranks.getOrDefault(playerUUID, 0);
    }

    public void setRank(UUID playerUUID, int rank) {
        ranks.put(playerUUID, rank);
        saveStatsForPlayer(playerUUID);
    }

    public int getKills(UUID playerUUID) {
        return kills.getOrDefault(playerUUID, 0);
    }

    public void setKills(UUID playerUUID, int kill) {
        kills.put(playerUUID, kill);
        saveStatsForPlayer(playerUUID);
    }

    public void addKills(UUID playerUUID, int amount) {
        kills.put(playerUUID, kills.get(playerUUID) + amount);
    }

    public void addKill(UUID playerId) {
        kills.put(playerId, getKills(playerId) + 1);
    }

    public ItemStack[] getInventory(UUID playerId) {
        return inventories.getOrDefault(playerId, new ItemStack[0]);
    }

    public void setInventory(UUID playerId, ItemStack[] inventory) {
        inventories.put(playerId, inventory);
    }

    public List<UUID> getTopPlayersByMoney() {
        return money.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<UUID> getTopPlayersByLevel() {
        return levels.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<UUID> getTopPlayersByBlocks() {
        return blocksMined.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void saveStatsForPlayer(UUID playerUUID) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        config.set(playerUUID.toString() + ".blocksMined", blocksMined.get(playerUUID));
        config.set(playerUUID.toString() + ".money", money.get(playerUUID));
        config.set(playerUUID.toString() + ".levels", levels.get(playerUUID));
        config.set(playerUUID.toString() + ".ranks", ranks.get(playerUUID));
        config.set(playerUUID.toString() + ".kills", kills.get(playerUUID));
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadStatsForPlayer(UUID playerUUID) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        blocksMined.put(playerUUID, config.getInt(playerUUID.toString() + ".blocksMined", 0));
        money.put(playerUUID, config.getDouble(playerUUID.toString() + ".money", 0.0));
        levels.put(playerUUID, config.getInt(playerUUID.toString() + ".levels", 1));
        ranks.put(playerUUID, config.getInt(playerUUID.toString() + ".ranks", 0));
        kills.put(playerUUID, config.getInt(playerUUID.toString() + ".kills", 0));
    }

    public void saveStats() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (UUID uuid : blocksMined.keySet()) {
            config.set(uuid.toString() + ".blocksMined", blocksMined.get(uuid));
            config.set(uuid.toString() + ".money", money.get(uuid)); config.set(uuid.toString() + ".levels", levels.get(uuid));
            config.set(uuid.toString() + ".ranks", ranks.get(uuid)); config.set(uuid.toString() + ".kills", kills.get(uuid));
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadStats() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (String uuidStr : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            blocksMined.put(uuid, config.getInt(uuidStr + ".blocksMined", 0));
            money.put(uuid, config.getDouble(uuidStr + ".money", 0.0));
            levels.put(uuid, config.getInt(uuidStr + ".levels", 1));
            ranks.put(uuid, config.getInt(uuidStr + ".ranks", 0));
            kills.put(uuid, config.getInt(uuidStr + ".kills", 0));
        }
    }
}