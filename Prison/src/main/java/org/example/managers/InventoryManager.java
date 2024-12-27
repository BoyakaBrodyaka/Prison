package org.example.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {
    private final HashMap<UUID, ItemStack[]> savedInventories = new HashMap<>();
    private final File dataFile;

    public InventoryManager(File dataFolder) {
        dataFile = new File(dataFolder, "inventories.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadInventories();
    }

    public void saveInventory(Player player) {
        UUID playerUUID = player.getUniqueId();
        savedInventories.put(playerUUID, player.getInventory().getContents());
        saveInventoriesToFile();
    }

    public void restoreInventory(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (savedInventories.containsKey(playerUUID)) {
            player.getInventory().setContents(savedInventories.get(playerUUID));
        }
    }

    private void saveInventoriesToFile() {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, ItemStack[]> entry : savedInventories.entrySet()) {
            config.set(entry.getKey().toString(), encodeItemStacks(entry.getValue()));
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadInventories() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : config.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            try {
                ItemStack[] items = decodeItemStacks(config.getString(key));
                if (items != null) {
                    savedInventories.put(playerUUID, items);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Error decoding inventory for player " + key + ": " + e.getMessage());
            }
        }
    }

    private String encodeItemStacks(ItemStack[] items) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(byteOut);
            out.writeObject(items);
            out.close();
            return Base64.getEncoder().encodeToString(byteOut.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ItemStack[] decodeItemStacks(String data) {
        if (data == null || data.isEmpty()) {
            System.err.println("Data is null or empty for decodeItemStacks");
            return new ItemStack[0];
        }
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream in = new BukkitObjectInputStream(byteIn);
            ItemStack[] items = (ItemStack[]) in.readObject();
            in.close();
            return items;
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
    }
}
