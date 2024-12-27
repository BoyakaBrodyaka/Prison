package org.example.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatZoneManager implements Listener {
    private final List<ChatZone> zones = new ArrayList<>();
    private final StatusManager manager;
    private final FractionManager fractionManager;

    public ChatZoneManager(StatusManager manager, FractionManager fractionManager) {
        this.manager = manager;
        this.fractionManager = fractionManager;
        Bukkit.getPluginManager().registerEvents(this, manager.getPlugin()); // Регистрация событий
    }

    public void addZone(Location corner1, Location corner2) {
        zones.add(new ChatZone(corner1, corner2));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String fraction = fractionManager.getFraction(player);

        // Определение префикса фракции
        String fractionPrefix = "";
        if (fraction != null) {
            switch (fraction) {
                case "Азиаты":
                    fractionPrefix = ChatColor.YELLOW + "Азиаты " + ChatColor.RESET;
                    break;
                case "Белые":
                    fractionPrefix = ChatColor.WHITE + "Белые " + ChatColor.RESET;
                    break;
                case "Нигеры":
                    fractionPrefix = ChatColor.DARK_GRAY + "Нигеры " + ChatColor.RESET;
                    break;
            }
        }

        // Проверка на префиксы и изменение никнейма
        if (manager.hasLSPrefix(player)) {
            player.setDisplayName("§cLS " + player.getName() + " " + fractionPrefix + ChatColor.RESET);
        } else if (manager.hasDevPrefix(player)) {
            player.setDisplayName("§9Dev " + player.getName() + " " + fractionPrefix + ChatColor.RESET);
        } else if (manager.hasADMPrefix(player)) {
            player.setDisplayName("§cADM " + player.getName() + " " + fractionPrefix + ChatColor.RESET);
        } else if (manager.hasBuilderPrefix(player)) {
            player.setDisplayName("§3B " + player.getName() + " " + fractionPrefix + ChatColor.RESET);
        } else if (manager.hasCurbuilderPrefix(player)) {
            player.setDisplayName("§3Cur.B " + player.getName()  + " " + fractionPrefix + ChatColor.RESET);
        } else if (manager.hasModeratorPrefix(player)) {
            player.setDisplayName("§dM " + player.getName() + " " + fractionPrefix + ChatColor.RESET);
        } else {
            player.setDisplayName(player.getName() + " " +  fractionPrefix + ChatColor.RESET);
        }

        if (message.startsWith("!")) {
            // Global chat
            event.setFormat(ChatColor.GOLD + "[G] " + ChatColor.RESET + player.getDisplayName() + " » " + message.substring(1));
            return;
        }

        // Local chat
        ChatZone playerZone = getPlayerZone(player);
        if (playerZone != null) {
            event.setCancelled(true); // Prevent the default chat event
            for (Player p : playerZone.getPlayers()) {
                p.sendMessage(ChatColor.BLUE + "[L] " + ChatColor.RESET + player.getDisplayName() + " » " + message);
            }
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        for (ChatZone zone : zones) {
            if (zone.isWithinZone(location)) {
                if (!zone.getPlayers().contains(player)) {
                    zone.addPlayer(player);
                }
            } else {
                zone.removePlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (ChatZone zone : zones) {
            zone.removePlayer(player);
        }
    }

    public ChatZone getPlayerZone(Player player) {
        for (ChatZone zone : zones) {
            if (zone.isWithinZone(player.getLocation())) {
                return zone;
            }
        }
        return null;
    }

    public static class ChatZone {
        private final Location corner1;
        private final Location corner2;
        private final List<Player> players = new ArrayList<>();

        public ChatZone(Location corner1, Location corner2) {
            this.corner1 = corner1;
            this.corner2 = corner2;
        }

        public boolean isWithinZone(Location loc) {
            return loc.getX() >= Math.min(corner1.getX(), corner2.getX()) && loc.getX() <= Math.max(corner1.getX(), corner2.getX()) &&
                    loc.getY() >= Math.min(corner1.getY(), corner2.getY()) && loc.getY() <= Math.max(corner1.getY(), corner2.getY()) &&
                    loc.getZ() >= Math.min(corner1.getZ(), corner2.getZ()) && loc.getZ() <= Math.max(corner1.getZ(), corner2.getZ());
        }

        public void addPlayer(Player player) {
            players.add(player);
        }

        public void removePlayer(Player player) {
            players.remove(player);
        }

        public List<Player> getPlayers() {
            return new ArrayList<>(players);
        }
    }
}

