package org.example.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.example.MainClass;
import org.example.donate.boosters.BoosterManager;
import org.example.managers.FractionManager;
import org.example.managers.InventoryManager;
import org.example.managers.StatusManager;
import org.example.stats.PlayerStats;

import java.util.*;

public class PlayerJoinQuitListener implements Listener {
    private final MainClass plugin;
    private final InventoryManager inventoryManager;
    private final PlayerStats playerStats;
    private final StatusManager statusManager;
    private final FractionManager fractionManager;
    private final BoosterManager boosterManager;

    public PlayerJoinQuitListener(MainClass plugin, InventoryManager inventoryManager, PlayerStats playerStats, StatusManager statusManager, FractionManager fractionManager, BoosterManager boosterManager) {
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.playerStats = playerStats;
        this.statusManager = statusManager;
        this.fractionManager = fractionManager;
        this.boosterManager = boosterManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        double offlineMoney = playerStats.getOfflineMoney(playerId);
        updateTabName(event.getPlayer());
        inventoryManager.restoreInventory(event.getPlayer());
        fractionManager.setFraction(event.getPlayer(), fractionManager.getFraction(event.getPlayer()));
        boosterManager.loadBoosters();

        if (offlineMoney > 0) {
            playerStats.addMoney(playerId, offlineMoney);
            playerStats.clearOfflineMoney(playerId);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Вам начислено " + offlineMoney + " монет, пока вы были оффлайн.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        inventoryManager.saveInventory(event.getPlayer());
        fractionManager.setFraction(event.getPlayer(), fractionManager.getFraction(event.getPlayer()));
        boosterManager.saveBoosters();
    }

    public void updateTabName(Player player) {
        int level = playerStats.getLevel(player.getUniqueId());
        String prefix = statusManager.getPrefix(player);
        ChatColor prefixColor = statusManager.getPrefixColor(player);

        String newName = ChatColor.GOLD + "[" + level + "] " + prefixColor + prefix + " " + player.getName();
        player.setPlayerListName(newName);
    }

    public void updateAllPlayersTabNames() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        // Сортировка по уровню и приоритету префикса
        players.sort(new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                int level1 = playerStats.getLevel(p1.getUniqueId());
                int level2 = playerStats.getLevel(p2.getUniqueId());

                if (level1 != level2) {
                    return Integer.compare(level2, level1); // Сортировка по уровню (по убыванию)
                }

                int priority1 = statusManager.getPrefixPriority(p1);
                int priority2 = statusManager.getPrefixPriority(p2);
                return Integer.compare(priority1, priority2); // Сортировка по приоритету префикса (по возрастанию)
            }
        });

        for (Player player : players) {
            updateTabName(player);
        }
    }
}
