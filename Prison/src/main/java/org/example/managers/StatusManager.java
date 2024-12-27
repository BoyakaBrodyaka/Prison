package org.example.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.MainClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatusManager implements CommandExecutor {

    private final Set<String> lsPlayers = new HashSet<>();
    private final Set<String> devPlayers = new HashSet<>();
    private final Set<String> admPlayers = new HashSet<>();
    private final Set<String> builderPlayers = new HashSet<>();
    private final Set<String> curbuilderPlayers = new HashSet<>();
    private final Set<String> moderatorPlayers = new HashSet<>();
    private MainClass plugin;

    public StatusManager(MainClass plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("ls").setExecutor(this);
        this.plugin.getCommand("developer").setExecutor(this);
        this.plugin.getCommand("administrator").setExecutor(this);
        this.plugin.getCommand("builder").setExecutor(this);
        this.plugin.getCommand("curbuilder").setExecutor(this);
        this.plugin.getCommand("moderator").setExecutor(this);
        loadPlayers();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + command.getName() + " <add/remove> <player>");
            return true;
        }

        String subCommand = args[0];
        String playerName = args[1];
        Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден");
            return true;
        }
        if(!sender.isOp()) {
            sender.sendMessage("У вас нет прав");
            return true;
        }

        if (command.getName().equalsIgnoreCase("ls")) {
            handleCommand(lsPlayers, sender, subCommand, target, "LS");
        } else if (command.getName().equalsIgnoreCase("developer")) {
            handleCommand(devPlayers, sender, subCommand, target, "Dev");
        } else if (command.getName().equalsIgnoreCase("administrator")) {
            handleCommand(admPlayers, sender, subCommand, target, "ADM");
        } else if (command.getName().equalsIgnoreCase("builder")) {
            handleCommand(builderPlayers, sender, subCommand, target, "B");
        } else if (command.getName().equalsIgnoreCase("curbuilder")) {
            handleCommand(curbuilderPlayers, sender, subCommand, target, "Cur.B");
        } else if (command.getName().equalsIgnoreCase("moderator")) {
            handleCommand(moderatorPlayers, sender, subCommand, target, "M");
        }

        savePlayers();
        return true;
    }

    private void handleCommand(Set<String> playerSet, CommandSender sender, String subCommand, Player target, String prefix) {
        if (subCommand.equalsIgnoreCase("add")) {
            playerSet.add(target.getName());
            sender.sendMessage(ChatColor.GREEN + "Игроку " + target.getName() + " добавлен " + prefix + " в список");
            target.sendMessage(ChatColor.RED + prefix + " твое новое призвание");
        } else if (subCommand.equalsIgnoreCase("remove")) {
            playerSet.remove(target.getName());
            sender.sendMessage(ChatColor.GREEN + "Игроку " + target.getName() + " удален " + prefix + " из списка");
            target.sendMessage(ChatColor.RED + prefix + " - призвание удалено");
        } else {
            sender.sendMessage(ChatColor.RED + "Используй: /" + prefix.toLowerCase() + " <add/remove> <player>");
        }
    }

    public boolean hasLSPrefix(Player player) {
        return lsPlayers.contains(player.getName());
    }

    public boolean hasDevPrefix(Player player) {
        return devPlayers.contains(player.getName());
    }

    public boolean hasADMPrefix(Player player) {
        return admPlayers.contains(player.getName());
    }

    public boolean hasBuilderPrefix(Player player) {
        return builderPlayers.contains(player.getName());
    }

    public boolean hasModeratorPrefix(Player player) {
        return moderatorPlayers.contains(player.getName());
    }

    public boolean hasCurbuilderPrefix(Player player) {
        return curbuilderPlayers.contains(player.getName());
    }

    public String getPrefix(Player player) {
        if (hasLSPrefix(player)) {
            return "LS";
        } else if (hasDevPrefix(player)) {
            return "Dev";
        } else if (hasADMPrefix(player)) {
            return "ADM";
        } else if (hasBuilderPrefix(player)) {
            return "B";
        } else if (hasCurbuilderPrefix(player)) {
            return "Cur.B";
        } else if (hasModeratorPrefix(player)) {
            return "M";
        } else {
            return ""; // No prefix
        }
    }

    public ChatColor getPrefixColor(Player player) {
        if (hasLSPrefix(player)) {
            return ChatColor.RED; // Example: Blue for LS
        } else if (hasDevPrefix(player)) {
            return ChatColor.BLUE; // Example: Red for Dev
        } else if (hasADMPrefix(player)) {
            return ChatColor.RED; // Example: Dark Red for ADM
        } else if (hasBuilderPrefix(player)) {
            return ChatColor.DARK_AQUA; // Example: Green for Builder
        } else if (hasCurbuilderPrefix(player)) {
            return ChatColor.DARK_AQUA; // Example: Dark Green for Cur.B
        } else if (hasModeratorPrefix(player)) {
            return ChatColor.LIGHT_PURPLE; // Example: Yellow for Moderator
        } else {
            return ChatColor.WHITE; // Default color
        }
    }

    public int getPrefixPriority(Player player) {
        if (hasADMPrefix(player)) {
            return 1; // ADM
        } else if (hasDevPrefix(player)) {
            return 2; // Dev
        } else if (hasModeratorPrefix(player)) {
            return 3; // M
        } else if (hasCurbuilderPrefix(player)) {
            return 4; // Cur.B
        } else if (hasBuilderPrefix(player)) {
            return 5; // B
        } else {
            return 6; // Default/No prefix
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    private void savePlayers() {
        FileConfiguration config = plugin.getConfig();
        config.set("lsPlayers", new ArrayList<>(lsPlayers));
        config.set("devPlayers", new ArrayList<>(devPlayers));
        config.set("admPlayers", new ArrayList<>(admPlayers));
        config.set("builderPlayers", new ArrayList<>(builderPlayers));
        config.set("curbuilderPlayers", new ArrayList<>(curbuilderPlayers));
        config.set("moderatorPlayers", new ArrayList<>(moderatorPlayers));
        plugin.saveConfig();
    }

    private void loadPlayers() {
        FileConfiguration config = plugin.getConfig();
        List<String> loadedLSPlayers = config.getStringList("lsPlayers");
        List<String> loadedDevPlayers = config.getStringList("devPlayers");
        List<String> loadedADMPlayers = config.getStringList("admPlayers");
        List<String> loadedBuilderPlayers = config.getStringList("builderPlayers");
        List<String> loadedCurbuilderPlayers = config.getStringList("curbuilderPlayers");
        List<String> loadedModeratorPlayers = config.getStringList("moderatorPlayers");
        lsPlayers.addAll(loadedLSPlayers);
        devPlayers.addAll(loadedDevPlayers);
        admPlayers.addAll(loadedADMPlayers);
        builderPlayers.addAll(loadedBuilderPlayers);
        curbuilderPlayers.addAll(loadedCurbuilderPlayers);
        moderatorPlayers.addAll(loadedModeratorPlayers);
    }
}
