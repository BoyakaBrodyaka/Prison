package org.example.bosses;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.MainClass;

import java.util.Arrays;

public class BossCommand implements CommandExecutor {
    private final MainClass plugin;

    public BossCommand(MainClass plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            openBossesInventory(player);
            return true;
        }
        return false;
    }

    private void openBossesInventory(Player player) {
        Inventory bossesInventory = plugin.getServer().createInventory(null, 27, "Боссы");

        for (int i = 0; i < 9; i++) {
            ItemStack bossItem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            ItemMeta meta = bossItem.getItemMeta();
            meta.setDisplayName("Босс");
            meta.setLore(Arrays.asList("Требуемый уровень: " + (i + 1) * 3));
            bossItem.setItemMeta(meta);
            bossesInventory.setItem(i, bossItem);
        }

        player.openInventory(bossesInventory);
    }
}
