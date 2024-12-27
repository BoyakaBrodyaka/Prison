package org.example.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.MainClass;

public class MainMenuManager implements Listener {
    private final MainClass plugin;

    public MainMenuManager(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void giveMenuItem(Player player) {
        ItemStack menuItem = new ItemStack(Material.PAPER);
        ItemMeta meta = menuItem.getItemMeta();
        meta.setDisplayName("§aМеню");
        menuItem.setItemMeta(meta);
        player.getInventory().setItem(8, menuItem); // Устанавливаем предмет меню в слот 9 (индекс 8)
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        giveMenuItem(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.PAPER && item.getItemMeta().getDisplayName().equals("§aМеню")) {
            openMainMenu(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Главное меню")) {
            event.setCancelled(true); // Предотвращаем взятие предметов из меню
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                String command = clickedItem.getItemMeta().getLocalizedName();
                if (command != null && !command.isEmpty()) {
                    player.closeInventory();
                    player.performCommand(command);
                }
            }
        }
    }

    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 45, ChatColor.GREEN + "Главное меню");

        menu.setItem(22, createMenuItem(Material.LIME_WOOL, ChatColor.GREEN + "Продать блоки", "sell"));
        menu.setItem(20, createMenuItem(Material.GOLDEN_APPLE, ChatColor.GOLD + "Достижения", "achievements"));
        menu.setItem(23, createMenuItem(Material.EXPERIENCE_BOTTLE, ChatColor.AQUA + "Уровень", "level"));
        menu.setItem(21, createMenuItem(Material.STONE_PICKAXE, ChatColor.RED + "Шахты", "mines"));
        menu.setItem(13, createMenuItem(Material.DIAMOND, ChatColor.GREEN + "Аукцион", "ah"));
        menu.setItem(31, createMenuItem(Material.EMERALD, ChatColor.YELLOW + "Магазин", "shop"));
        menu.setItem(24, createMenuItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED + "Боссы", "bosses"));
        menu.setItem(4, createMenuItem(Material.DIAMOND_BLOCK, ChatColor.AQUA + "Донат", "donate"));

        player.openInventory(menu);
    }

    private ItemStack createMenuItem(Material material, String name, String command) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLocalizedName(command); // Используем localizedName для хранения команды
        item.setItemMeta(meta);
        return item;
    }
}
