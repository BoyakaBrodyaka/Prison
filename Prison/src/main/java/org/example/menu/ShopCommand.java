package org.example.menu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.MainClass;

public class ShopCommand implements CommandExecutor {
    private final MainClass plugin;
    private final ShopMenuManager shopMenuManager;

    public ShopCommand(MainClass plugin, ShopMenuManager shopMenuManager) {
        this.plugin = plugin;
        this.shopMenuManager = shopMenuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.closeInventory();
            shopMenuManager.openShopMenu(player);
            return true;
        }
        return false;
    }
}
