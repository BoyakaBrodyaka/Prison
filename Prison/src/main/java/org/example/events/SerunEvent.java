package org.example.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.example.MainClass;

import java.util.*;

public class SerunEvent implements Event, Listener {
    private final MainClass plugin;
    private final Set<Vector> buttonLocations = new HashSet<>();

    public SerunEvent(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeButtonLocations();
    }

    private void initializeButtonLocations() {
        buttonLocations.add(new Vector(21, 67, 19));
        // Добавляйте свои координаты здесь
    }

    @Override
    public void apply(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3));
            player.sendMessage(ChatColor.YELLOW + "Вы захотели в туалет...");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null) {
            if (block.getType() == Material.STONE_BUTTON) {
                Vector location = block.getLocation().toVector();
                if (buttonLocations.contains(location)) {
                    if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                        player.removePotionEffect(PotionEffectType.SLOW);
                        player.sendMessage(ChatColor.GREEN + "Вы избавились от Серуна!");
                    }
                }
            }
        }
    }
}
