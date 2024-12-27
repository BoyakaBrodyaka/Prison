package org.example.donate.currency;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.stats.NumberFormatter;

public class RemoveCurrencyCommand implements CommandExecutor {
    private final CurrencyManager currencyManager;

    public RemoveCurrencyCommand(CurrencyManager currencyManager) {
        this.currencyManager = currencyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) return false;
        Player target = sender.getServer().getPlayer(args[0]);
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (target != null) {
            currencyManager.removeCurrency(target.getUniqueId(), amount);
            sender.sendMessage("Вы убрали " + NumberFormatter.format(amount) + " валюты у " + target.getName());
            target.sendMessage("У вас забрали " + NumberFormatter.format(amount) + " валюты.");
            return true;
        }
        return false;
    }
}
