package org.example.stats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.example.managers.FractionManager;

public class FractionCommand implements CommandExecutor, Listener {
    private final PlayerStats playerStats;
    private final FractionManager fractionManager;

    public FractionCommand(PlayerStats playerStats, FractionManager fractionManager) {
        this.playerStats = playerStats;
        this.fractionManager = fractionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (playerStats.getLevel(player.getUniqueId()) >= 5) {
                fractionManager.openFractionSelection(player);
            } else {
                player.sendMessage("Вы должны быть минимум 5 уровня, чтобы выбрать фракцию.");
            }
        } else {
            sender.sendMessage("Эту команду могут использовать только игроки.");
        }
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Выберите фракцию")) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null) {
                if (clickedItem.getType() == Material.YELLOW_WOOL) {
                    fractionManager.joinFraction(player, "Азиаты");
                } else if (clickedItem.getType() == Material.WHITE_WOOL) {
                    fractionManager.joinFraction(player, "Белые");
                } else if (clickedItem.getType() == Material.GRAY_WOOL) {
                    fractionManager.joinFraction(player, "Нигеры");
                }
            }
            player.closeInventory();
            event.setCancelled(true);
        }
    }
}
