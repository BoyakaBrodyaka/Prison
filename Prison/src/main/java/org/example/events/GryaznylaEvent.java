package org.example.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.MainClass;

import java.util.UUID;

public class GryaznylaEvent implements Event, Listener {
    private final MainClass plugin;

    public GryaznylaEvent(MainClass plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void apply(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.CONFUSION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 3));
            player.sendMessage(ChatColor.YELLOW + "Пора помыться...");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (player.hasPotionEffect(PotionEffectType.CONFUSION) && player.isInWater()) {
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.sendMessage(ChatColor.GREEN + "Вы избавились от Грязнули!");
        }
    }
}
