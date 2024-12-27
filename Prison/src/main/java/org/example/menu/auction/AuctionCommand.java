package org.example.menu.auction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.MainClass;
import org.example.stats.NumberFormatter;
import org.example.stats.PlayerStats;

import java.util.ArrayList;
import java.util.List;

public class AuctionCommand implements CommandExecutor, Listener {
    private final AuctionManager auctionManager;
    private final PlayerStats playerStats;

    public AuctionCommand(AuctionManager auctionManager, PlayerStats playerStats) {
        this.auctionManager = auctionManager;
        this.playerStats = playerStats;
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(MainClass.class));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду могут использовать только игроки.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            openAuctionHouse(player);
        } else if (args[0].equalsIgnoreCase("sell") && args.length == 2) {
            try {
                double startingPrice = NumberFormatter.parse(args[1]);
                sellItem(player, startingPrice);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Неправильный формат цены.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Использование: /ah [sell <цена>]");
        }
        return true;
    }

    private void openAuctionHouse(Player player) {
        Inventory auctionHouse = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Аукцион");
        int index = 0;
        for (AuctionItem auctionItem : auctionManager.getAuctionItems()) {
            ItemStack item = auctionItem.getItem().clone();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Продавец: " + auctionManager.getPlayerName(auctionItem.getSeller()));
            lore.add(ChatColor.GRAY + "Цена: " + NumberFormatter.format(auctionItem.getCurrentPrice()) + " монет");
            meta.setLore(lore);
            item.setItemMeta(meta);
            auctionHouse.setItem(index++, item);
        }
        player.openInventory(auctionHouse);
    }

    private void sellItem(Player player, double startingPrice) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "У вас нет предмета в руке.");
            return;
        }
        auctionManager.addAuctionItem(player, item, startingPrice);
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        player.sendMessage(ChatColor.GREEN + "Выставлено на аукцион за " + NumberFormatter.format(startingPrice) + " монет.");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        if (event.getView().getTitle().equals(ChatColor.GREEN + "Аукцион")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            AuctionItem auctionItem = auctionManager.getAuctionItemByIndex(slot);
            if (auctionItem != null && !auctionItem.getSeller().equals(player.getUniqueId())) {
                openConfirmInventory(player, auctionItem);
            } else {
                player.sendMessage(ChatColor.RED + "Вы не можете купить свой собственный товар.");
            }
        } else if (event.getView().getTitle().equals(ChatColor.GREEN + "Покупка предмета")) {
            event.setCancelled(true);
            if (event.getSlot() == 21) { // Зеленая панель - покупка
                Inventory confirmInventory = event.getInventory();
                ItemStack item = confirmInventory.getItem(13);
                AuctionItem auctionItem = getAuctionItemByItem(item);

                if (auctionItem != null) {
                    auctionManager.buyAuctionItem(player, auctionItem, playerStats);
                    player.closeInventory();
                }
            } else if (event.getSlot() == 23) { // Красная панель - отказ
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "Покупка отменена.");
            }
        }
    }

    private void openConfirmInventory(Player player, AuctionItem auctionItem) {
        Inventory confirmInventory = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Покупка предмета");
        confirmInventory.setItem(13, auctionItem.getItem());

        ItemStack greenPane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta greenPaneMeta = greenPane.getItemMeta();
        greenPaneMeta.setDisplayName(ChatColor.GREEN + "Подтвердить");
        greenPane.setItemMeta(greenPaneMeta);
        confirmInventory.setItem(21, greenPane);

        ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta redPaneMeta = redPane.getItemMeta();
        redPaneMeta.setDisplayName(ChatColor.RED + "Отмена");
        redPane.setItemMeta(redPaneMeta);
        confirmInventory.setItem(23, redPane);

        player.openInventory(confirmInventory);
    }

    private AuctionItem getAuctionItemByItem(ItemStack item) {
        for (AuctionItem auctionItem : auctionManager.getAuctionItems()) {
            if (auctionItem.getItem().isSimilar(item)) {
                return auctionItem;
            }
        }
        return null;
    }
}
