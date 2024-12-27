package org.example.donate.boosters;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.MainClass;
import org.example.donate.currency.CurrencyManager;

public class DonateEvent implements Listener {
    private final MainClass plugin;
    private final BoosterManager boosterManager;
    private final CurrencyManager currencyManager;

    public DonateEvent(MainClass plugin, BoosterManager boosterManager, CurrencyManager currencyManager) {
        this.plugin = plugin;
        this.boosterManager = boosterManager;
        this.currencyManager = currencyManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Донат меню")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            ItemMeta meta = clickedItem.getItemMeta();
            String displayName = meta.getDisplayName();

            if (displayName.equals(ChatColor.GREEN + "Локальный бустер киллов")) {
                if(!boosterManager.isLocalBoosterActive(player, "киллов")) {
                    if (currencyManager.getCurrency(player.getUniqueId()) >= 150) {
                        currencyManager.removeCurrency(player.getUniqueId(), 150);
                        boosterManager.activateLocalBooster(player, "киллов");
                        player.sendMessage("Вы купили локальный бустер киллов!");
                    } else {
                        player.sendMessage("У вас недостаточно монет.");
                    }
                }
            } else if (displayName.equals(ChatColor.GREEN + "Локальный бустер денег")) {
                if(!boosterManager.isLocalBoosterActive(player, "денег")) {
                    if (currencyManager.getCurrency(player.getUniqueId()) >= 150) {
                        currencyManager.removeCurrency(player.getUniqueId(), 150);
                        boosterManager.activateLocalBooster(player, "денег");
                        player.sendMessage("Вы купили локальный бустер денег!");
                    } else {
                        player.sendMessage("У вас недостаточно монет.");
                    }
                }
            } else if (displayName.equals(ChatColor.GREEN + "Локальный бустер блоков")) {
                if(!boosterManager.isLocalBoosterActive(player, "блоков")) {
                    if (currencyManager.getCurrency(player.getUniqueId()) >= 150) {
                        currencyManager.removeCurrency(player.getUniqueId(), 150);
                        boosterManager.activateLocalBooster(player, "блоков");
                        player.sendMessage("Вы купили локальный бустер блоков!");
                    } else {
                        player.sendMessage("У вас недостаточно монет.");
                    }
                }
            } else if (displayName.equals(ChatColor.RED + "Глобальный бустер киллов")) {
                if(!boosterManager.isGlobalBoosterActive("киллов")) {
                    if (currencyManager.getCurrency(player.getUniqueId()) >= 300) {
                        currencyManager.removeCurrency(player.getUniqueId(), 300);
                        boosterManager.activateGlobalBooster(player,"киллов");
                        player.sendMessage("Вы купили глобальный бустер киллов!");
                    } else {
                        player.sendMessage("У вас недостаточно монет.");
                    }
                }
            } else if (displayName.equals(ChatColor.RED + "Глобальный бустер денег")) {
                if(!boosterManager.isGlobalBoosterActive("денег")) {
                    if (currencyManager.getCurrency(player.getUniqueId()) >= 300) {
                        currencyManager.removeCurrency(player.getUniqueId(), 300);
                        boosterManager.activateGlobalBooster(player,"денег");
                        player.sendMessage("Вы купили глобальный бустер денег!");
                    } else {
                        player.sendMessage("У вас недостаточно монет.");
                    }
                }
            } else if (displayName.equals(ChatColor.RED + "Глобальный бустер блоков")) {
                if(!boosterManager.isGlobalBoosterActive("блоков")) {
                    if (currencyManager.getCurrency(player.getUniqueId()) >= 300) {
                        currencyManager.removeCurrency(player.getUniqueId(), 300);
                        boosterManager.activateGlobalBooster(player,"блоков");
                        player.sendMessage("Вы купили глобальный бустер блоков!");
                    } else {
                        player.sendMessage("У вас недостаточно монет.");
                    }
                }
            } else if (displayName.equals(ChatColor.RED + "Автопродажа")) {
                if(!player.hasPermission("autosell.use")) {
                    if (currencyManager.getCurrency(player.getUniqueId()) >= 799) {
                        currencyManager.removeCurrency(player.getUniqueId(), 799);
                        player.addAttachment(plugin, "autosell.use", true);
                        player.sendMessage("Вы купили автопродажу!");
                    } else {
                        player.sendMessage("У вас недостаточно монет.");
                    }
                } else {
                    player.sendMessage("Вы уже купили автопродажу");
                }
        } else if (displayName.equals(ChatColor.RED + "Отключение комиссии")) {
                if (currencyManager.getCurrency(player.getUniqueId()) >= 499) {
                    currencyManager.removeCurrency(player.getUniqueId(), 499);
                    boosterManager.disableCommission(player);
                    player.sendMessage("Вы отключили комиссию на 1 час!");
                } else {
                    player.sendMessage("У вас недостаточно монет.");
                }
            }
        }
    }
}
