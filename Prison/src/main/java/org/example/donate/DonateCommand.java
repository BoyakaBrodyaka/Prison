package org.example.donate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonateCommand implements CommandExecutor {
    private final DonateMenuManager donateMenuManager;

    public DonateCommand(DonateMenuManager donateMenuManager) {
        this.donateMenuManager = donateMenuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            donateMenuManager.openDonateMenu(player);
            return true;
        }
        return false;
    }
}
