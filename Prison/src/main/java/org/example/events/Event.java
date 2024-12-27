package org.example.events;

import org.bukkit.entity.Player;

public interface Event {
    void apply(Player player); // Метод для применения события к игроку
}

