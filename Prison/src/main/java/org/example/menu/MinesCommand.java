package org.example.menu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.MainClass;

public class MinesCommand implements CommandExecutor {
    private final MainClass plugin;
    private final MinesMenuManager minesMenuManager;

    public MinesCommand(MainClass plugin, MinesMenuManager minesMenuManager) {
        this.plugin = plugin;
        this.minesMenuManager = minesMenuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.closeInventory();
            plugin.getLogger().info("Opening mines menu for player: " + player.getName());
            minesMenuManager.openMinesMenu(player);
            return true;
        }
        return false;
    }
}
