package org.example.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.example.MainClass;

public class TrashCommand implements Listener, CommandExecutor {
    private final MainClass plugin;

    public TrashCommand(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду могут использовать только игроки.");
            return false;
        }

        Player player = (Player) sender;
        Inventory trashInventory = Bukkit.createInventory(null, 27, ChatColor.RED + "Мусорка");
        player.openInventory(trashInventory);
        return true;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.RED + "Мусорка")) {
            Inventory inventory = event.getInventory();
            inventory.clear();
            Player player = (Player) event.getPlayer();
            player.sendMessage(ChatColor.GREEN + "Все предметы в мусорке были уничтожены.");
        }
    }
}
