package org.example.donate.currency;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CurrencyManager {
    private final HashMap<UUID, Integer> playerCurrency = new HashMap<>();
    private static File configFile;
    private static YamlConfiguration config;

    public int getCurrency(UUID player) {
        return playerCurrency.getOrDefault(player, 0);
    }

    public void addCurrency(UUID player, int amount) {
        playerCurrency.put(player, getCurrency(player) + amount);
        saveCurrency();
    }

    public void removeCurrency(UUID player, int amount) {
        playerCurrency.put(player, Math.max(0, getCurrency(player) - amount));
        saveCurrency();
    }

    public void saveCurrency() {
        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("PrisonPlugin").getDataFolder(), "currency.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        for (UUID playerUUID : playerCurrency.keySet()) {
            config.set(playerUUID.toString(), playerCurrency.get(playerUUID));
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCurrency() {
        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("PrisonPlugin").getDataFolder(), "currency.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        for (String key : config.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            int amount = config.getInt(key);
            playerCurrency.put(playerUUID, amount);
        }
    }
}
