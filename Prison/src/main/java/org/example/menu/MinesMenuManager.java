package org.example.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.example.MainClass;

import java.util.List;

public class MinesMenuManager implements Listener {
    private final MainClass plugin;

    public MinesMenuManager(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openMinesMenu(Player player) {
        Inventory minesMenu = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Шахты");
        int playerLevel = plugin.getPlayerStats().getLevel(player.getUniqueId());

        minesMenu.setItem(10, createMineItem(playerLevel, 1, Material.DARK_OAK_LOG, "Особняки", "Требуется 1 уровень", "PVP: §aВыключено"));
        minesMenu.setItem(11, createMineItem(playerLevel, 2, Material.RED_WOOL, "Город", "Требуется 2 уровень", "PVP: §aВыключено"));
        minesMenu.setItem(12, createMineItem(playerLevel, 3, Material.BROWN_WOOL, "Ярмарка", "Требуется 3 уровень", "PVP: §aВыключено"));
        minesMenu.setItem(13, createMineItem(playerLevel, 4, Material.QUARTZ_BLOCK, "Парк", "Требуется 4 уровень", "PVP: §aВыключено"));
        minesMenu.setItem(14, createMineItem(playerLevel, 5, Material.REDSTONE_LAMP, "Ресторан", "Требуется 5 уровень", "PVP: §cВключено"));
        minesMenu.setItem(15, createMineItem(playerLevel, 6, Material.OAK_WOOD, "Таинственный лес", "Требуется 6 уровень", "PVP: §cВключено"));
        minesMenu.setItem(16, createMineItem(playerLevel, 7, Material.MUD_BRICKS, "Фестиваль", "Требуется 7 уровень", "PVP: §cВключено"));
        minesMenu.setItem(19, createMineItem(playerLevel, 8, Material.PINK_WOOL, "Сладкий мир", "Требуется 8 уровень", "PVP: §cВключено"));
        minesMenu.setItem(20, createMineItem(playerLevel, 9, Material.QUARTZ_BRICKS, "Рай", "Требуется 9 уровень", "PVP: §cВключено"));
        minesMenu.setItem(21, createMineItem(playerLevel, 10, Material.SPRUCE_PLANKS, "Спальня", "Требуется 10 уровень", "PVP: §cВключено"));
        minesMenu.setItem(22, createMineItem(playerLevel, 11, Material.BARRIER, "coming soon", "Требуется 11 уровень", "PVP: §cВключено"));
        minesMenu.setItem(23, createMineItem(playerLevel, 12, Material.BARRIER, "coming soon", "Требуется 12 уровень", "PVP: §cВключено"));
        minesMenu.setItem(24, createMineItem(playerLevel, 13, Material.BARRIER, "coming soon", "Требуется 13 уровень", "PVP: §cВключено"));
        minesMenu.setItem(25, createMineItem(playerLevel, 14, Material.BARRIER, "coming soon", "Требуется 14 уровень", "PVP: §cВключено"));
        minesMenu.setItem(28, createMineItem(playerLevel, 15, Material.BARRIER, "coming soon", "Требуется 15 уровень", "PVP: §cВключено"));
        minesMenu.setItem(29, createMineItem(playerLevel, 16, Material.BARRIER, "coming soon", "Требуется 16 уровень", "PVP: §cВключено"));
        minesMenu.setItem(30, createMineItem(playerLevel, 17, Material.BARRIER, "coming soon", "Требуется 17 уровень", "PVP: §cВключено"));
        minesMenu.setItem(31, createMineItem(playerLevel, 18, Material.BARRIER, "coming soon", "Требуется 18 уровень", "PVP: §cВключено"));
        minesMenu.setItem(32, createMineItem(playerLevel, 19, Material.BARRIER, "coming soon", "Требуется 19 уровень", "PVP: §cВключено"));
        minesMenu.setItem(33, createMineItem(playerLevel, 20, Material.BARRIER, "coming soon", "Требуется 20 уровень", "PVP: §cВключено"));
        minesMenu.setItem(34, createMineItem(playerLevel, 21, Material.BARRIER, "coming soon", "Требуется 21 уровень", "PVP: §cВключено"));
        minesMenu.setItem(37, createMineItem(playerLevel, 22, Material.BARRIER, "coming soon", "Требуется 22 уровень", "PVP: §cВключено"));
        minesMenu.setItem(38, createMineItem(playerLevel, 23, Material.BARRIER, "coming soon", "Требуется 23 уровень", "PVP: §cВключено"));
        minesMenu.setItem(39, createMineItem(playerLevel, 24, Material.BARRIER, "coming soon", "Требуется 24 уровень", "PVP: §cВключено"));
        minesMenu.setItem(40, createMineItem(playerLevel, 25, Material.BARRIER, "coming soon", "Требуется 25 уровень", "PVP: §cВключено"));
        minesMenu.setItem(41, createMineItem(playerLevel, 26, Material.BARRIER, "coming soon", "Требуется 26 уровень", "PVP: §cВключено"));
        minesMenu.setItem(42, createMineItem(playerLevel, 27, Material.BARRIER, "coming soon", "Требуется 27 уровень", "PVP: §cВключено"));
        minesMenu.setItem(43, createMineItem(playerLevel, 28, Material.BARRIER, "coming soon", "Требуется 28 уровень", "PVP: §cВключено"));
        // Continue adding mines as needed

        player.openInventory(minesMenu);
    }

    private ItemStack createMineItem(int playerLevel, int requiredLevel, Material material, String name, String lore, String loreUnlockedMine) {
        ItemStack item;
        if (playerLevel >= requiredLevel) {
            item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + name);
            meta.setLore(List.of(" ", "§7", loreUnlockedMine));
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.BARRIER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("???");
            meta.setLore(List.of(" ", "§7" + lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Шахты")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                if (itemName.equals(ChatColor.RED + "Особняки")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), -249, 63, -86));
                } else if (itemName.equals(ChatColor.RED + "Город")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), -119, 63, -68));
                } else if (itemName.equals(ChatColor.RED + "Ярмарка")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), -37, 67, 433));
                } else if (itemName.equals(ChatColor.RED + "Парк")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), 19, 62, -48));
                } else if (itemName.equals(ChatColor.RED + "Ресторан")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), 58, 61, -64));
                } else if (itemName.equals(ChatColor.RED + "Таинственный лес")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), 133, 59, -36));
                } else if (itemName.equals(ChatColor.RED + "Фестиваль")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), 220, 62, -59));
                } else if (itemName.equals(ChatColor.RED + "Сладкий мир")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), 292, 60, -570));
                } else if (itemName.equals(ChatColor.RED + "Рай")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), 360, 60, -104));
                } else if (itemName.equals(ChatColor.RED + "Спальня")) {
                    plugin.getZoneManager().teleportWithDelay(player, new Location(Bukkit.getWorld("PrisonServer"), 428, 63, -78));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                }
            }
        }
    }
}
