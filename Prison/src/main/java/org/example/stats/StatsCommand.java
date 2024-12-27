package org.example.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.MainClass;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {
    private final PlayerStats playerStats;

    public StatsCommand(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 4) {
                player.sendMessage(ChatColor.RED + "Usage: /stats <player> <add/set/reset> <money/blocks/level/rank/kills> <amount>");
                return false;
            }
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "У вас нет прав!");
                return false;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            UUID playerUUID = target.getUniqueId();
            String action = args[1];
            String stat = args[2];

            if (action.equalsIgnoreCase("reset")) {
                resetStats(playerUUID);
                player.sendMessage(ChatColor.GREEN + "Statistics for " + target.getName() + " have been reset.");
                return true;
            }

            try {
                int amount = parseValue(args[3]);

                switch (stat.toLowerCase()) {
                    case "money":
                        if (action.equalsIgnoreCase("add")) {
                            playerStats.setMoney(playerUUID, playerStats.getMoney(playerUUID) + amount);
                        } else if (action.equalsIgnoreCase("set")) {
                            playerStats.setMoney(playerUUID, amount);
                        }
                        player.sendMessage(ChatColor.GREEN + "Money updated for " + target.getName() + " to " + NumberFormatter.format((playerStats.getMoney(playerUUID))));
                        break;
                    case "blocks":
                        if (action.equalsIgnoreCase("add")) {
                            playerStats.setBlocksMined(playerUUID, playerStats.getBlocksMined(playerUUID) + amount);
                        } else if (action.equalsIgnoreCase("set")) {
                            playerStats.setBlocksMined(playerUUID, amount);
                        }
                        player.sendMessage(ChatColor.GREEN + "Blocks mined updated for " + target.getName() + " to " + NumberFormatter.format((playerStats.getBlocksMined(playerUUID))));
                        break;
                    case "level":
                        if (action.equalsIgnoreCase("add")) {
                            int newLevel = Math.min(playerStats.getLevel(playerUUID) + amount, 30); // Ensure the level doesn't exceed 30
                            playerStats.setLevel(playerUUID, newLevel);
                        } else if (action.equalsIgnoreCase("set")) {
                            if (amount > 30) {
                                player.sendMessage(ChatColor.RED + "Level cannot exceed 30.");
                                return false;
                            }
                            playerStats.setLevel(playerUUID, amount);
                        }
                        player.sendMessage(ChatColor.GREEN + "Level updated for " + target.getName() + " to " + NumberFormatter.format((playerStats.getLevel(playerUUID))));
                        break;
                    case "rank":
                        if (action.equalsIgnoreCase("add")) {
                            playerStats.setRank(playerUUID, playerStats.getRank(playerUUID) + amount);
                        } else if (action.equalsIgnoreCase("set")) {
                            playerStats.setRank(playerUUID, amount);
                        }
                        player.sendMessage(ChatColor.GREEN + "Rank updated for " + target.getName() + " to " + NumberFormatter.format((playerStats.getRank(playerUUID))));
                        break;
                    case "kills":
                        if (action.equalsIgnoreCase("add")) {
                            playerStats.setKills(playerUUID, playerStats.getKills(playerUUID) + amount);
                        } else if (action.equalsIgnoreCase("set")) {
                            playerStats.setKills(playerUUID, amount);
                        }
                        player.sendMessage(ChatColor.GREEN + "Kills updated for " + target.getName() + " to " + NumberFormatter.format((playerStats.getBlocksMined(playerUUID))));
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "Invalid stat. Use: money, blocks, level, rank, kills.");
                        return false;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Amount must be a number.");
                return false;
            }

            // Update scoreboard for online players
            Player onlinePlayer = Bukkit.getPlayer(playerUUID);
            if (onlinePlayer != null) {
                MainClass.getInstance().getScoreboardManager().updateScoreboard(onlinePlayer);
            }
        }

        return true;
    }

    private void resetStats(UUID playerUUID) {
        playerStats.setMoney(playerUUID, 0);
        playerStats.setBlocksMined(playerUUID, 0);
        playerStats.setLevel(playerUUID, 1);
        playerStats.setRank(playerUUID, 0);
        playerStats.setKills(playerUUID, 0);
    }

    private int parseValue(String value) {
        char suffix = value.charAt(value.length() - 1);
        if (Character.isDigit(suffix)) {
            return Integer.parseInt(value);
        } else {
            int baseValue = Integer.parseInt(value.substring(0, value.length() - 1));
            switch (suffix) {
                case 'K':
                case 'k':
                    return baseValue * 1_000;
                case 'M':
                case 'm':
                    return baseValue * 1_000_000;
                case 'B':
                case 'b':
                    return baseValue * 1_000_000_000;
                default:
                    throw new NumberFormatException("Invalid suffix");
            }
        }
    }
}

