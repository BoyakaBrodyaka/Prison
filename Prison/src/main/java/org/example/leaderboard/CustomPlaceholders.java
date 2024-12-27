package org.example.leaderboard;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.example.MainClass;
import org.example.stats.NumberFormatter;
import org.example.stats.PlayerStats;

import java.util.List;
import java.util.UUID;

public class CustomPlaceholders extends PlaceholderExpansion {

    private final MainClass plugin;
    private final PlayerStats playerStats;

    public CustomPlaceholders(MainClass plugin, PlayerStats playerStats) {
        this.plugin = plugin;
        this.playerStats = playerStats;
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the expansion on reload
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "prisonplugin";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        // %prisonplugin_money%
        if (identifier.equals("money")) {
            return NumberFormatter.format(playerStats.getMoney(player.getUniqueId()));
        }

        // %prisonplugin_level%
        if (identifier.equals("level")) {
            return NumberFormatter.format(playerStats.getLevel(player.getUniqueId()));
        }

        // %prisonplugin_blocks%
        if (identifier.equals("blocks")) {
            return NumberFormatter.format(playerStats.getBlocksMined(player.getUniqueId()));
        }

        // Топы
        if (identifier.startsWith("money_top_")) {
            int rank = Integer.parseInt(identifier.replace("money_top_", ""));
            return getTopPlayer(rank, "money");
        }

        if (identifier.startsWith("level_top_")) {
            int rank = Integer.parseInt(identifier.replace("level_top_", ""));
            return getTopPlayer(rank, "level");
        }

        if (identifier.startsWith("blocks_top_")) {
            int rank = Integer.parseInt(identifier.replace("blocks_top_", ""));
            return getTopPlayer(rank, "blocks");
        }

        return null;
    }

    private String getTopPlayer(int rank, String type) {
        List<UUID> topPlayers;
        switch (type) {
            case "money":
                topPlayers = playerStats.getTopPlayersByMoney(); // Получение списка топ игроков по деньгам
                break;
            case "level":
                topPlayers = playerStats.getTopPlayersByLevel(); // Получение списка топ игроков по уровням
                break;
            case "blocks":
                topPlayers = playerStats.getTopPlayersByBlocks(); // Получение списка топ игроков по блокам
                break;
            default:
                return "";
        }

        if (rank > topPlayers.size()) {
            return "";
        }

        UUID playerId = topPlayers.get(rank - 1);
        String playerName = plugin.getServer().getOfflinePlayer(playerId).getName();
        String value;
        switch (type) {
            case "money":
                value = NumberFormatter.format(playerStats.getMoney(playerId));
                break;
            case "level":
                value = NumberFormatter.format(playerStats.getLevel(playerId));
                break;
            case "blocks":
                value = NumberFormatter.format(playerStats.getBlocksMined(playerId));
                break;
            default:
                return "";
        }

        return playerName + " - " + value;
    }
}

