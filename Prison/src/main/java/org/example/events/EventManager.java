package org.example.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.MainClass;

import java.util.*;
import java.util.stream.Collectors;

public class EventManager {
    private final MainClass plugin;
    private final List<Event> events = new ArrayList<>();
    private final Random random = new Random();

    public EventManager(MainClass plugin) {
        this.plugin = plugin;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void startEvents() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                int eventCount = Math.max(1, (int) (players.size() * 0.6)); // 60% игроков, но не менее 1 игрока

                for (int i = 0; i < eventCount; i++) {
                    if (!players.isEmpty()) {
                        Player player = players.remove(random.nextInt(players.size()));
                        Event event = events.get(random.nextInt(events.size()));
                        event.apply(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 12000); // Запуск каждую минуту
    }
}
