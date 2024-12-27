package org.example.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.MainClass;

import java.util.List;

public class ShopMenuManager implements Listener {
    private final MainClass plugin;
    private final ToolsMenuManager toolsMenuManager;

    public ShopMenuManager(MainClass plugin, ToolsMenuManager toolsMenuManager) {
        this.plugin = plugin;
        this.toolsMenuManager = toolsMenuManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openShopMenu(Player player) {
        Inventory shopMenu = Bukkit.createInventory(null, 45, ChatColor.GREEN + "Магазин");

        shopMenu.setItem(28, createShopItem(Material.WOODEN_SHOVEL, ChatColor.RED + "Сломанная лопата", "Цена: 10.0"));
        shopMenu.setItem(30, createShopItem(Material.WOODEN_PICKAXE, ChatColor.RED + "Сломанная кирка", "Цена: 100.0"));
        shopMenu.setItem(32, createShopItem(Material.WOODEN_AXE, ChatColor.RED + "Сломанный топор", "Цена: 200.0"));
        shopMenu.setItem(34, createShopItem(Material.SHEARS, ChatColor.RED + "Сломанные ножницы", "Цена: 300.0"));

        shopMenu.setItem(13, createShopItem(Material.WOODEN_SWORD, ChatColor.RED + "Сломанный меч", "Цена: 150.0"));
        shopMenu.setItem(10, createShopItem(Material.LEATHER_HELMET, ChatColor.GREEN + "Кожаный шлем", "Цена: 50.0"));
        shopMenu.setItem(12, createShopItem(Material.LEATHER_CHESTPLATE, ChatColor.GREEN + "Кожаный нагрудник", "Цена: 100.0"));
        shopMenu.setItem(14, createShopItem(Material.LEATHER_LEGGINGS, ChatColor.GREEN + "Кожаные поножи", "Цена: 75.0"));
        shopMenu.setItem(16, createShopItem(Material.LEATHER_BOOTS, ChatColor.GREEN + "Кожаные ботинки", "Цена: 50.0"));

        player.openInventory(shopMenu);
    }

    private ItemStack createShopItem(Material material, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of("§7" + lore));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Магазин")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                if (itemName != null) {
                    toolsMenuManager.purchaseItem(player, clickedItem);
                }
            }
        }
    }
}
