package org.example.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.example.MainClass;
import org.example.managers.ScoreboardManager;

import java.util.UUID;

public class PlayerMoveEventListener implements Listener {
    private final MainClass plugin;

    public PlayerMoveEventListener(MainClass plugin) {
        this.plugin = plugin;
    }


}
