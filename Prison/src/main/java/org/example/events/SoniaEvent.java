package org.example.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.example.MainClass;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.util.Vector;

public class SoniaEvent implements Event, Listener {
    private final MainClass plugin;
    private final Set<Vector> bedLocations = new HashSet<>();

    public SoniaEvent(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeBedLocations();
    }

    private void initializeBedLocations() {
        bedLocations.add(new Vector(21,65,17));
        bedLocations.add(new Vector(20, 65, 17));
        // Добавляйте свои координаты здесь
    }

    @Override
    public void apply(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 3));
            player.sendMessage(ChatColor.YELLOW + "Вы устали...");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null) {

            if (block.getType() == Material.RED_BED || block.getType() == Material.WHITE_BED) {
                Vector location = block.getLocation().toVector();
                if (bedLocations.contains(location)) {
                    if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        player.sendMessage(ChatColor.GREEN + "Вы избавились от Соня!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Block bed = event.getBed();
        Vector location = bed.getLocation().toVector();

        if (bedLocations.contains(location)) {
            event.setCancelled(true);
        }
    }
}
