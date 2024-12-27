package org.example.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.managers.FractionManager;

public class PayCommand implements CommandExecutor {
    private final PlayerStats playerStats;
    private final FractionManager fractionManager;

    public PayCommand(PlayerStats playerStats, FractionManager fractionManager) {
        this.playerStats = playerStats;
        this.fractionManager = fractionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду могут использовать только игроки.");
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Использование: /pay <игрок> <сумма>");
            return false;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Игрок не найден.");
            return false;
        }

        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "Вы не можете передать деньги себе.");
            return false;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Сумма должна быть положительным числом.");
            return false;
        }

        String playerFraction = fractionManager.getFraction(player);
        String targetFraction = fractionManager.getFraction(target);

        int fee = (playerFraction != null && playerFraction.equals(targetFraction)) ? 0 : (int) (amount * 0.10);
        int finalAmount = amount - fee;

        if (playerStats.getMoney(player.getUniqueId()) < amount) {
            player.sendMessage(ChatColor.RED + "У вас недостаточно денег.");
            return false;
        }

        playerStats.removeMoney(player.getUniqueId(), amount);
        playerStats.addMoney(target.getUniqueId(), finalAmount);

        player.sendMessage(ChatColor.GREEN + "Вы передали " + finalAmount + " монет игроку " + target.getName() + ".");
        target.sendMessage(ChatColor.GREEN + "Вы получили " + finalAmount + " монет от игрока " + player.getName() + ".");

        return true;
    }
}
