package org.example.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.example.managers.FractionManager;
import org.example.managers.PvPManager;
import org.example.managers.PvPModeManager;

public class PlayerEntityDamageEvent implements Listener {
    private final PvPManager pvpManager;
    private final FractionManager fractionManager;
    private final PvPModeManager pvpmodeManager;

    public PlayerEntityDamageEvent(PvPManager pvpManager, FractionManager fractionManager, PvPModeManager pvpmodeManager) {
        this.pvpManager = pvpManager;
        this.fractionManager = fractionManager;
        this.pvpmodeManager = pvpmodeManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player target = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if (!pvpManager.canPlayerPvP(damager)) {
                event.setCancelled(true);
            }

            String damagerFraction = fractionManager.getFraction(damager);
            String targetFraction = fractionManager.getFraction(target);

            if (damagerFraction != null && targetFraction != null && damagerFraction.equals(targetFraction)) {
                event.setCancelled(true);
                pvpmodeManager.deactivatePvPMode(target);
                pvpmodeManager.deactivatePvPMode(damager);
            }
        }
    }


}
