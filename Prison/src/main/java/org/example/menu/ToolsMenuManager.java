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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsMenuManager implements Listener {
    private final MainClass plugin;
    private final Map<String, Double> itemPrices = new HashMap<>();

    public ToolsMenuManager(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        itemPrices.put(ChatColor.RED + "Сломанный топор", 200.0);
        itemPrices.put(ChatColor.RED + "Сломанная кирка", 100.0);
        itemPrices.put(ChatColor.RED + "Сломанная лопата", 10.0);
        itemPrices.put(ChatColor.RED + "Сломанные ножницы", 300.0);
        itemPrices.put(ChatColor.RED + "Сломанный меч", 150.0);
        itemPrices.put(ChatColor.GREEN + "Кожаный шлем", 50.0);
        itemPrices.put(ChatColor.GREEN + "Кожаный нагрудник", 100.0);
        itemPrices.put(ChatColor.GREEN + "Кожаные поножи", 75.0);
        itemPrices.put(ChatColor.GREEN + "Кожаные ботинки", 50.0);
    }

    public void openToolsMenu(Player player) {
        Inventory toolsMenu = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Инструменты");

        toolsMenu.setItem(2, createToolItem(Material.WOODEN_AXE, ChatColor.RED + "Сломанный топор", "Цена: 200.0"));
        toolsMenu.setItem(4, createToolItem(Material.WOODEN_PICKAXE, ChatColor.RED + "Сломанная кирка", "Цена: 100.0"));
        toolsMenu.setItem(6, createToolItem(Material.WOODEN_SHOVEL, ChatColor.RED + "Сломанная лопата", "Цена: 10.0"));
        toolsMenu.setItem(8, createToolItem(Material.SHEARS, ChatColor.RED + "Сломанные ножницы", "Цена: 300.0"));

        player.openInventory(toolsMenu);
    }

    private ItemStack createToolItem(Material material, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of("§7Начальный инструмент", "", "§7" + lore));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public void purchaseItem(Player player, ItemStack clickedItem) {
        String itemName = clickedItem.getItemMeta().getDisplayName();
        if (itemPrices.containsKey(itemName)) {
            double price = itemPrices.get(itemName);
            double playerMoney = plugin.getPlayerStats().getMoney(player.getUniqueId());
            if (playerMoney >= price) {
                plugin.getPlayerStats().setMoney(player.getUniqueId(), playerMoney - price);

                ItemStack purchasedItem = new ItemStack(clickedItem.getType());
                ItemMeta purchasedItemMeta = purchasedItem.getItemMeta();
                purchasedItemMeta.setDisplayName(itemName);
                purchasedItemMeta.setLore(clickedItem.getItemMeta().getLore());
                purchasedItemMeta.setUnbreakable(true);
                purchasedItem.setItemMeta(purchasedItemMeta);

                player.getInventory().addItem(purchasedItem);
                player.sendMessage(ChatColor.GREEN + "Вы купили " + itemName + ChatColor.GREEN + " за " + price + " монет!");
            } else {
                player.sendMessage(ChatColor.RED + "Недостаточно денег!");
            }
            plugin.getScoreboardManager().updateScoreboard(player); // Обновляем табло после покупки
        }
    }
}
