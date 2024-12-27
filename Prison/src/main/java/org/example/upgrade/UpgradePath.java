package org.example.upgrade;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class UpgradePath {
    private final Map<Integer, UpgradeInfo> upgrades = new HashMap<>();

    public void addUpgradeLevel(UpgradeInfo upgradeInfo) {
        upgrades.put(upgrades.size() + 1, upgradeInfo); // Level starts from 1
    }

    public UpgradeInfo getNextUpgrade(ItemStack item) {
        String itemName = item.getItemMeta().getDisplayName();
        int currentLevel = extractLevelFromName(itemName);

        // Fetch the next upgrade level
        return upgrades.getOrDefault(currentLevel + 1, upgrades.get(0)); // Advance to the next level
    }

    public int extractLevelFromName(String name) {
        try {
            String[] parts = name.split(" ");
            String levelPart = parts[parts.length - 1];
            return Integer.parseInt(levelPart.replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            return 0; // Default to level 0 if parsing fails
        }
    }
}
