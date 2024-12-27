package org.example.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.example.MainClass;
import org.example.stats.NumberFormatter;
import org.example.stats.PlayerStats;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final MainClass plugin;
    private final PlayerStats playerStats;
    private final Map<Player, Scoreboard> scoreboards = new HashMap<>();

    public ScoreboardManager(MainClass plugin, PlayerStats playerStats) {
        this.plugin = plugin;
        this.playerStats = playerStats;
    }

    public void createScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("PrisonStats", "dummy", ChatColor.GOLD + "" + ChatColor.BOLD + "Kladik");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(ChatColor.RED + "     Статистика").setScore(12);
        objective.getScore("      ").setScore(11);
        // Пустые строки для форматирования
        objective.getScore("     ").setScore(9);
        objective.getScore("    ").setScore(7);
        objective.getScore("   ").setScore(5);
        objective.getScore("  ").setScore(3);
        objective.getScore(" ").setScore(1);
        objective.getScore(ChatColor.GREEN + "     kladik.pro").setScore(0);

        player.setScoreboard(board);
        scoreboards.put(player, board);

        // Инициализация значений
        updateScore(objective, "Денег", NumberFormatter.format(playerStats.getMoney(player.getUniqueId())), 10);
        updateScore(objective, "Блоков", NumberFormatter.format(playerStats.getBlocksMined(player.getUniqueId())), 8);
        updateScore(objective, "Уровень", String.valueOf(playerStats.getLevel(player.getUniqueId())), 6);
        updateScore(objective, "Рейтинг", String.valueOf(playerStats.getRank(player.getUniqueId())), 4);
        updateScore(objective, "Убийства", String.valueOf(playerStats.getKills(player.getUniqueId())), 2);
    }

    public void updateScoreboardAsync(Player player, Objective objective) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Асинхронное обновление данных
                UUID playerUUID = player.getUniqueId();
                String money = NumberFormatter.format(playerStats.getMoney(playerUUID));
                String blocks = NumberFormatter.format(playerStats.getBlocksMined(playerUUID));
                String level = NumberFormatter.format(playerStats.getLevel(playerUUID));
                String rank = NumberFormatter.format(playerStats.getRank(playerUUID));
                String kills = NumberFormatter.format(playerStats.getKills(playerUUID));

                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Обновление скорборда в основном потоке
                    updateScore(objective, "Денег", money, 10);
                    updateScore(objective, "Блоков", blocks, 8);
                    updateScore(objective, "Уровень", level, 6);
                    updateScore(objective, "Рейтинг", rank, 4);
                    updateScore(objective, "Убийства", kills, 2);
                });
            }
        }.runTaskAsynchronously(plugin);
    }

    private void updateScore(Objective objective, String label, String newValue, int scorePosition) {
        Scoreboard board = objective.getScoreboard();
        for (String entry : board.getEntries()) {
            if (entry.contains(label)) {
                board.resetScores(entry);
                break;
            }
        }

        String formattedScore = String.format("%s: %s%s", label, ChatColor.GREEN, newValue);
        objective.getScore(formattedScore).setScore(scorePosition);
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = scoreboards.get(player);
        if (board == null) {
            createScoreboard(player);
            board = scoreboards.get(player);
        }
        Objective objective = board.getObjective("PrisonStats");
        if (objective == null) {
            createScoreboard(player);
            board = scoreboards.get(player);
            objective = board.getObjective("PrisonStats");
        }
        updateScoreboardAsync(player, objective);
    }

    public void startScoreboardUpdater() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::updateAllScoreboards, 0L, 40L); // Обновление каждые 2 секунды (40 тиков)
    }

    private void updateAllScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }
}








