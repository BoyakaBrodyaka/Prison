package org.example.stats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.example.MainClass;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AutoSellCommand implements CommandExecutor {
    private static final Set<UUID> autoSellEnabledPlayers = new HashSet<>();
    private static final Map<Material, Double> sellPrices = new HashMap<>();
    private static File dataFile;
    private static FileConfiguration config;

    static {
        // Define the prices for each material
        sellPrices.put(Material.DIRT, 0.1);
        sellPrices.put(Material.COARSE_DIRT, 0.2);
        sellPrices.put(Material.STONE, 0.5);
        sellPrices.put(Material.COBBLESTONE, 1.0);
        sellPrices.put(Material.COAL_ORE, 1.0);
        sellPrices.put(Material.COAL_BLOCK, 2.0);
        sellPrices.put(Material.IRON_ORE, 3.0);
        sellPrices.put(Material.GOLD_ORE, 4.0);
        sellPrices.put(Material.DIAMOND_ORE, 5.0);
        sellPrices.put(Material.OAK_PLANKS, 4.0);
        sellPrices.put(Material.OAK_WOOD, 5.0);
        sellPrices.put(Material.WHITE_WOOL, 6.0);
        sellPrices.put(Material.PINK_WOOL, 7.0);
        sellPrices.put(Material.QUARTZ_BLOCK, 10.0);
        sellPrices.put(Material.QUARTZ_BRICKS, 12.0);
        sellPrices.put(Material.CHISELED_QUARTZ_BLOCK, 15.0);
        sellPrices.put(Material.SPRUCE_PLANKS, 12.0);
        sellPrices.put(Material.SPRUCE_WOOD, 15.0);
        // Add more materials and their sell prices as needed
    }

    public AutoSellCommand(MainClass plugin) {
        dataFile = new File(plugin.getDataFolder(), "autosell.yml");
        config = YamlConfiguration.loadConfiguration(dataFile);
        loadAutoSellData();
    }

    public static boolean isAutoSellEnabled(UUID playerUUID) {
        return autoSellEnabledPlayers.contains(playerUUID);
    }

    public static Map<Material, Double> getSellPrices() {
        return sellPrices;
    }

    public void saveAutoSellData() {
        for (UUID playerUUID : autoSellEnabledPlayers) {
            config.set("autosell." + playerUUID.toString(), true);
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAutoSellData() {
        if (config.contains("autosell")) {
            for (String uuidString : config.getConfigurationSection("autosell").getKeys(false)) {
                UUID playerUUID = UUID.fromString(uuidString);
                autoSellEnabledPlayers.add(playerUUID);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if (!player.hasPermission("autosell.use")) {
                player.sendMessage("У вас нет прав на использование автопродажи.");
                return true;
            }

            if (autoSellEnabledPlayers.contains(playerUUID)) {
                autoSellEnabledPlayers.remove(playerUUID);
                player.sendMessage("Автопродажа отключена.");
            } else {
                autoSellEnabledPlayers.add(playerUUID);
                player.sendMessage("Автопродажа включена.");
            }
            saveAutoSellData();
        }
        return true;
    }
}
