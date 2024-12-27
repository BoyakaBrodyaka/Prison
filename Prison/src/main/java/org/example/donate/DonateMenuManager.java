package org.example.donate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.MainClass;
import org.example.donate.boosters.BoosterManager;
import org.example.donate.currency.CurrencyManager;
import org.example.stats.NumberFormatter;

import java.util.Arrays;

public class DonateMenuManager {
    private final MainClass plugin;
    private final BoosterManager boosterManager;
    private final CurrencyManager currencyManager;

    public DonateMenuManager(MainClass plugin, BoosterManager boosterManager, CurrencyManager currencyManager) {
        this.plugin = plugin;
        this.boosterManager = boosterManager;
        this.currencyManager = currencyManager;
    }

    public void openDonateMenu(Player player) {
        Inventory donateMenu = Bukkit.createInventory(null, 45, ChatColor.GREEN + "Донат меню");

        donateMenu.setItem(21, createMenuItem(Material.DIAMOND, ChatColor.GREEN + "Локальный бустер киллов", "Активировать локальный бустер киллов", "Стоимость: 150 монет"));
        donateMenu.setItem(22, createMenuItem(Material.DIAMOND, ChatColor.GREEN + "Локальный бустер денег", "Активировать локальный бустер денег", "Стоимость: 150 монет"));
        donateMenu.setItem(23, createMenuItem(Material.DIAMOND, ChatColor.GREEN + "Локальный бустер блоков", "Активировать локальный бустер блоков", "Стоимость: 150 монет"));
        donateMenu.setItem(30, createMenuItem(Material.EMERALD, ChatColor.RED + "Глобальный бустер киллов", "Активировать глобальный бустер киллов", "Стоимость: 300 монет"));
        donateMenu.setItem(31, createMenuItem(Material.EMERALD, ChatColor.RED + "Глобальный бустер денег", "Активировать глобальный бустер денег", "Стоимость: 300 монет"));
        donateMenu.setItem(32, createMenuItem(Material.EMERALD, ChatColor.RED + "Глобальный бустер блоков", "Активировать глобальный бустер блоков", "Стоимость: 300 монет"));
        donateMenu.setItem(26, createMenuItem(Material.GOLD_INGOT, ChatColor.RED + "Автопродажа", "Автопродажа", "Стоимость: 799 монет"));
        donateMenu.setItem(18, createMenuItem(Material.IRON_INGOT, ChatColor.RED + "Отключение комиссии", "Комиссия - 0%", "Стоимость: 499 монет"));
        donateMenu.setItem(4, createMenuItem(Material.PAPER, ChatColor.YELLOW + "Валюта: " + NumberFormatter.format(currencyManager.getCurrency(player.getUniqueId())), "Ваше текущее количество валюты"));

        player.openInventory(donateMenu);
    }

    // Метод для создания предмета меню
    private ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}
