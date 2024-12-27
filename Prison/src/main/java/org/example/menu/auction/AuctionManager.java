package org.example.menu.auction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.MainClass;
import org.example.stats.NumberFormatter;
import org.example.stats.PlayerStats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuctionManager {
    private final MainClass plugin;
    private final List<AuctionItem> auctionItems = new ArrayList<>();
    private final File auctionFile;
    private final FileConfiguration auctionConfig;

    public AuctionManager(MainClass plugin) {
        this.plugin = plugin;
        this.auctionFile = new File(plugin.getDataFolder(), "auction.yml");
        this.auctionConfig = YamlConfiguration.loadConfiguration(auctionFile);
        loadAuctionItems();
    }

    public void addAuctionItem(Player seller, ItemStack item, double startingPrice) {
        AuctionItem auctionItem = new AuctionItem(item, seller.getUniqueId(), startingPrice);
        auctionItems.add(auctionItem);
        saveAuctionItems();
    }

    public List<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public AuctionItem getAuctionItemByIndex(int index) {
        if (index < 0 || index >= auctionItems.size()) {
            return null;
        }
        return auctionItems.get(index);
    }

    public void removeAuctionItem(AuctionItem auctionItem) {
        auctionItems.remove(auctionItem);
        saveAuctionItems();
    }

    public void buyAuctionItem(Player buyer, AuctionItem auctionItem, PlayerStats playerStats) {
        double price = auctionItem.getCurrentPrice();
        if (playerStats.getMoney(buyer.getUniqueId()) < price) {
            buyer.sendMessage(ChatColor.RED + "У вас недостаточно денег для покупки этого предмета.");
            return;
        }

        UUID sellerId = auctionItem.getSeller();
        Player seller = Bukkit.getPlayer(sellerId);

        if (seller == null) {
            playerStats.addOfflineMoney(sellerId, price); // Добавляем деньги продавцу, если он оффлайн
        } else {
            playerStats.addMoney(seller.getUniqueId(), price);
            seller.sendMessage(ChatColor.GREEN + "Ваш предмет был продан за " + NumberFormatter.format(price) + " монет.");
        }

        playerStats.removeMoney(buyer.getUniqueId(), price);
        buyer.getInventory().addItem(auctionItem.getItem());
        removeAuctionItem(auctionItem);

        buyer.sendMessage(ChatColor.GREEN + "Вы успешно купили предмет за " + NumberFormatter.format(price) + " монет.");
    }

    public void saveAuctionItems() {
        auctionConfig.set("items", null);
        for (int i = 0; i < auctionItems.size(); i++) {
            AuctionItem auctionItem = auctionItems.get(i);
            auctionConfig.set("items." + i + ".item", auctionItem.getItem());
            auctionConfig.set("items." + i + ".seller", auctionItem.getSeller().toString());
            auctionConfig.set("items." + i + ".startingPrice", auctionItem.getStartingPrice());
            auctionConfig.set("items." + i + ".currentPrice", auctionItem.getCurrentPrice());
        }
        try {
            auctionConfig.save(auctionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAuctionItems() {
        if (auctionConfig.contains("items")) {
            for (String key : auctionConfig.getConfigurationSection("items").getKeys(false)) {
                ItemStack item = auctionConfig.getItemStack("items." + key + ".item");
                UUID seller = UUID.fromString(auctionConfig.getString("items." + key + ".seller"));
                double startingPrice = auctionConfig.getDouble("items." + key + ".startingPrice");
                double currentPrice = auctionConfig.getDouble("items." + key + ".currentPrice");
                AuctionItem auctionItem = new AuctionItem(item, seller, startingPrice);
                auctionItem.setCurrentPrice(currentPrice);
                auctionItems.add(auctionItem);
            }
        }
    }

    public String getPlayerName(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        return offlinePlayer.getName() != null ? offlinePlayer.getName() : "Неизвестный игрок";
    }
}
