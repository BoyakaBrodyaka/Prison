package org.example.donate.boosters;

import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class disableBoostersCommand implements CommandExecutor {
    private final BoosterManager boosterManager;

    public disableBoostersCommand(BoosterManager boosterManager) {
        this.boosterManager = boosterManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("disableboosters")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("booster.disable")) {
                    boosterManager.disableAllBoosters();
                    player.sendMessage(ChatColor.RED + "Все бустеры отключены.");
                } else {
                    player.sendMessage(ChatColor.RED + "У вас нет прав для выполнения этой команды.");
                }
            } else {
                boosterManager.disableAllBoosters();
                sender.sendMessage("Все бустеры отключены.");
            }
            return true;
        }
        return false;
    }
}
