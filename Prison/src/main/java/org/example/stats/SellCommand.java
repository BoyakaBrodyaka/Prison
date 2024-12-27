package org.example.stats;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.MainClass;
import org.example.donate.boosters.BoosterManager;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellCommand implements CommandExecutor {
    private final PlayerStats playerStats;
    private final Map<Material, Double> sellPrices;
    private final BoosterManager boosterManager;

    public SellCommand(PlayerStats playerStats, BoosterManager boosterManager) {
        this.playerStats = playerStats;
        this.sellPrices = new HashMap<>();
        this.boosterManager = boosterManager;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            double totalSellPrice = 0.0;

            // Iterate through player's inventory
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && sellPrices.containsKey(item.getType())) {
                    double price = sellPrices.get(item.getType());
                    double multiplier = boosterManager.getBoosterMultiplier(player, "денег");
                    totalSellPrice += price * item.getAmount() * multiplier;
                    player.getInventory().remove(item);
                }
            }

            // Update player's money
            double currentMoney = playerStats.getMoney(playerUUID);
            playerStats.setMoney(playerUUID, currentMoney + totalSellPrice);

            // Notify the player
            player.sendMessage("§fТы продал все блоки за §a" + NumberFormatter.format(totalSellPrice));
            MainClass.getInstance().getScoreboardManager().updateScoreboard(player);
        }
        return true;
    }
}
