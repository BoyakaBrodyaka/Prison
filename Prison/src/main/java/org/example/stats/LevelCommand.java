package org.example.stats;

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
import org.example.MainClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LevelCommand implements CommandExecutor, Listener {
    private final MainClass plugin;
    private final Map<Integer, LevelRequirements> levelRequirementsMap = new HashMap<>();
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Boolean> hasLeveledUp = new HashMap<>();

    public LevelCommand(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        // Set level requirements
        levelRequirementsMap.put(2, new LevelRequirements(500, 100, 0));
        levelRequirementsMap.put(3, new LevelRequirements(2000, 250, 0));
        levelRequirementsMap.put(4, new LevelRequirements(5000, 400, 0));
        levelRequirementsMap.put(5, new LevelRequirements(10000, 1000, 10));
        levelRequirementsMap.put(6, new LevelRequirements(15000, 1500, 10));
        levelRequirementsMap.put(7, new LevelRequirements(25000, 2500, 20));
        levelRequirementsMap.put(8, new LevelRequirements(35000, 3500, 20));
        levelRequirementsMap.put(9, new LevelRequirements(45000, 4500, 40));
        levelRequirementsMap.put(10, new LevelRequirements(55000, 5500, 40));
        levelRequirementsMap.put(11, new LevelRequirements(65000, 6500, 60));
        levelRequirementsMap.put(12, new LevelRequirements(75000, 7500, 60));
        levelRequirementsMap.put(13, new LevelRequirements(85000, 8500, 70));
        levelRequirementsMap.put(14, new LevelRequirements(95000, 9500, 70));
        levelRequirementsMap.put(15, new LevelRequirements(105000, 10500, 80));
        levelRequirementsMap.put(16, new LevelRequirements(115000, 11500, 80));
        levelRequirementsMap.put(17, new LevelRequirements(125000, 12500, 90));
        levelRequirementsMap.put(18, new LevelRequirements(135000, 13500, 90));
        levelRequirementsMap.put(19, new LevelRequirements(145000, 14500, 100));
        levelRequirementsMap.put(20, new LevelRequirements(155000, 15500, 100));
        levelRequirementsMap.put(21, new LevelRequirements(165000, 16500, 110));
        levelRequirementsMap.put(22, new LevelRequirements(175000, 17500, 110));
        levelRequirementsMap.put(23, new LevelRequirements(185000, 18500, 120));
        levelRequirementsMap.put(24, new LevelRequirements(195000, 19500, 120));
        levelRequirementsMap.put(25, new LevelRequirements(205000, 20500, 130));
        levelRequirementsMap.put(26, new LevelRequirements(215000, 21500, 130));
        levelRequirementsMap.put(27, new LevelRequirements(225000, 22500, 140));
        levelRequirementsMap.put(28, new LevelRequirements(235000, 23500, 140));
        levelRequirementsMap.put(29, new LevelRequirements(245000, 24500, 150));
        levelRequirementsMap.put(30, new LevelRequirements(255000, 25500, 150));

        // Add more levels as needed
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
                openLevelMenu(player);
        }
        return true;
    }

    private void openLevelMenu(Player player) {
        Inventory levelMenu = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Уровень");

        int playerLevel = plugin.getPlayerStats().getLevel(player.getUniqueId());
        int nextLevel = playerLevel + 1;

        ItemStack levelInfo = new ItemStack(Material.PAPER);
        ItemMeta meta = levelInfo.getItemMeta();

        if (playerLevel >= 30) {
            meta.setDisplayName("§eМаксимальный уровень");
            meta.setLore(Arrays.asList(
                    ChatColor.GOLD + "Вы достигли максимального уровня!"
            ));
        } else {
            LevelRequirements requirements = levelRequirementsMap.getOrDefault(nextLevel, new LevelRequirements(0, 0, 0));
            double currentBlocks = plugin.getPlayerStats().getBlocksMined(player.getUniqueId());
            double currentMoney = plugin.getPlayerStats().getMoney(player.getUniqueId());
            int currentKills = plugin.getPlayerStats().getKills(player.getUniqueId());

            meta.setDisplayName("§e" + playerLevel + " -> " + nextLevel);
            meta.setLore(Arrays.asList(
                    ChatColor.GOLD + "Требования:",
                    ChatColor.YELLOW + "- Блоки: " + NumberFormatter.format(currentBlocks) + "/" + requirements.getBlocks(),
                    ChatColor.YELLOW + "- Монет: " + NumberFormatter.format(currentMoney) + "/" + requirements.getMoney(),
                    ChatColor.YELLOW + "- Киллы: " + currentKills + "/" + requirements.getKills()
            ));
        }
        levelInfo.setItemMeta(meta);

        levelMenu.setItem(13, levelInfo); // Place the item in the center

        player.openInventory(levelMenu);
        hasLeveledUp.put(player.getUniqueId(), false); // Reset leveling flag
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Уровень")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            UUID playerUUID = player.getUniqueId();

            // Check cooldown
            if (cooldowns.containsKey(playerUUID)) {
                long lastClickTime = cooldowns.get(playerUUID);
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < 1000) { // 1 second cooldown
                    return;
                }
            }
            if (hasLeveledUp.getOrDefault(playerUUID, false)) { // Check if already leveled up
                return;
            }

            cooldowns.put(playerUUID, System.currentTimeMillis());
            hasLeveledUp.put(playerUUID, true); // Mark as leveled up to prevent multiple executions

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PAPER) {
                player.closeInventory(); // Close inventory before updating level

                int playerLevel = plugin.getPlayerStats().getLevel(player.getUniqueId());
                if (playerLevel >= 30) {
                    player.sendMessage(ChatColor.RED + "Вы уже достигли максимального уровня.");
                    return;
                }

                int nextLevel = playerLevel + 1;
                LevelRequirements requirements = levelRequirementsMap.getOrDefault(nextLevel, new LevelRequirements(0, 0, 0));
                double currentBlocks = plugin.getPlayerStats().getBlocksMined(player.getUniqueId());
                double currentMoney = plugin.getPlayerStats().getMoney(player.getUniqueId());
                int currentKills = plugin.getPlayerStats().getKills(player.getUniqueId());

                if (currentBlocks >= requirements.getBlocks() && currentMoney >= requirements.getMoney() &&
                        currentKills >= requirements.getKills()) {
                    plugin.getPlayerStats().setLevel(player.getUniqueId(), nextLevel);
                    player.sendMessage(ChatColor.GREEN + "Поздравляю! Вы достигли уровня " + nextLevel + "!");
                } else {
                    player.sendMessage(ChatColor.RED + "У вас недостаточно ресурсов для повышения уровня.");
                }
            }
        }
    }

    private static class LevelRequirements {
        private final int blocks;
        private final int money;
        private final int kills;

        public LevelRequirements(int blocks, int money, int kills) {
            this.blocks = blocks;
            this.money = money;
            this.kills = kills;
        }

        public int getBlocks() {
            return blocks;
        }

        public int getMoney() {
            return money;
        }

        public int getKills() {
            return kills;
        }
    }
}
