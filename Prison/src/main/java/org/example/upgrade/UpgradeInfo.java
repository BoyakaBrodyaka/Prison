package org.example.upgrade;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class UpgradeInfo {
    private final Material nextMaterial;
    private final int cost;
    private final int requiredBlocks;
    private final int requiredLevel;
    private final Map<Enchantment, Integer> enchantments;
    private final boolean isUnbreakable;
    private final String customName;
    private final String customDescription;

    public UpgradeInfo(Material nextMaterial, int cost, int requiredBlocks, int requiredLevel, Map<Enchantment, Integer> enchantments, boolean isUnbreakable, String customName, String customDescription) {
        this.nextMaterial = nextMaterial;
        this.cost = cost;
        this.requiredBlocks = requiredBlocks;
        this.requiredLevel = requiredLevel;
        this.enchantments = enchantments;
        this.isUnbreakable = isUnbreakable;
        this.customName = customName;
        this.customDescription = customDescription;
    }

    public Material getNextMaterial() {
        return nextMaterial;
    }

    public int getCost() {
        return cost;
    }

    public int getRequiredBlocks() {
        return requiredBlocks;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public boolean isUnbreakable() {
        return isUnbreakable;
    }

    public String getCustomName() {
        return customName;
    }

    public String getCustomDescription() {
        return customDescription;
    }
}
