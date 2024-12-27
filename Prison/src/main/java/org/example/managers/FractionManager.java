package org.example.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.MainClass;
import org.example.stats.PlayerStats;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FractionManager {
    private final MainClass plugin;
    private final PlayerStats playerStats;
    private final Map<UUID, String> playerFractions = new HashMap<>();
    private final File dataFile;

    public FractionManager(MainClass plugin, PlayerStats playerStats) {
        this.plugin = plugin;
        this.playerStats = playerStats;
        this.dataFile = new File(plugin.getDataFolder(), "fractions.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadFractions();
    }

    public String getFraction(Player player) {
        return playerFractions.get(player.getUniqueId());
    }

    public void setFraction(Player player, String fraction) {
        playerFractions.put(player.getUniqueId(), fraction);
        saveFractionsToFile();
    }

    public void openFractionSelection(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Выберите фракцию");

        ItemStack yellowWool = createItem(new ItemStack(Material.YELLOW_WOOL), ChatColor.YELLOW + "Азиаты");
        ItemStack whiteWool = createItem(new ItemStack(Material.WHITE_WOOL), ChatColor.WHITE + "Белые");
        ItemStack grayWool = createItem(new ItemStack(Material.GRAY_WOOL), ChatColor.GRAY + "Нигеры");

        inv.setItem(3, yellowWool);
        inv.setItem(4, whiteWool);
        inv.setItem(5, grayWool);

        player.openInventory(inv);
    }

    private ItemStack createItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public boolean canChangeFraction(Player player) {
        return playerStats.getMoney(player.getUniqueId()) >= 1000;
    }

    public void changeFraction(Player player, String newFraction) {
        if (canChangeFraction(player)) {
            playerStats.removeMoney(player.getUniqueId(), 1000);
            setFraction(player, newFraction);
            player.sendMessage(ChatColor.GREEN + "Вы успешно сменили фракцию на " + newFraction);
        } else {
            player.sendMessage(ChatColor.RED + "У вас недостаточно монет для смены фракции.");
        }
    }

    public void joinFraction(Player player, String fraction) {
        if (getFraction(player) == null) {
            setFraction(player, fraction);
            player.sendMessage(ChatColor.GREEN + "Вы присоединились к фракции " + fraction);
        } else {
            changeFraction(player, fraction);
        }
    }

    public void saveFractionsToFile() {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, String> entry : playerFractions.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFractions() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : config.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            String fraction = config.getString(key);
            playerFractions.put(playerUUID, fraction);
        }
    }
}
