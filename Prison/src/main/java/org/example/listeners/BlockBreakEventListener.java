package org.example.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.example.MainClass;
import org.example.donate.boosters.BoosterManager;
import org.example.managers.ZoneManager;
import org.example.stats.AutoSellCommand;
import org.example.stats.NumberFormatter;
import org.example.stats.PlayerStats;

import java.util.UUID;

public class BlockBreakEventListener implements Listener {
    private final PlayerStats playerStats;
    private final ZoneManager zoneManager;
    private final BoosterManager boosterManager;

    public BlockBreakEventListener(PlayerStats playerStats, ZoneManager zoneManager, BoosterManager boosterManager) {
        this.playerStats = playerStats;
        this.zoneManager = zoneManager;
        this.boosterManager = boosterManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (zoneManager.isInZone(event.getBlock().getLocation())) {
            UUID playerUUID = player.getUniqueId();

            // Получаем множитель для блоков
            double multiplier = boosterManager.getBoosterMultiplier(player, "блоков");

            // Увеличиваем количество добытых блоков с учетом множителя
            int newBlockCount = playerStats.getBlocksMined(playerUUID) + (int) (1 * multiplier);
            playerStats.setBlocksMined(playerUUID, newBlockCount);

            // Обновляем счетчик на табло игрока
            MainClass.getInstance().getScoreboardManager().updateScoreboard(player);

            // Проверяем автопродажу
            if (AutoSellCommand.isAutoSellEnabled(playerUUID)) {
                double sellPrice = 0.0;
                Material blockType = event.getBlock().getType();
                if (AutoSellCommand.getSellPrices().containsKey(blockType)) {
                    sellPrice = AutoSellCommand.getSellPrices().get(blockType) * (int) (1 * multiplier);
                }
                double currentMoney = playerStats.getMoney(playerUUID);
                playerStats.setMoney(playerUUID, currentMoney + sellPrice);
                MainClass.getInstance().getScoreboardManager().updateScoreboard(player);
            } else {
                // Добавляем блоки в инвентарь игрока с учетом множителя
                Material blockType = event.getBlock().getType();
                ItemStack itemStack = new ItemStack(blockType, (int) (1 * multiplier));
                player.getInventory().addItem(itemStack);
            }

            event.getBlock().setType(Material.AIR); // Удаляем блок из мира
        } else if (player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(false);
        } else {
            event.setCancelled(true);
        }
    }
}
