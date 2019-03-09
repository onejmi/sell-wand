package io.github.scarger.sellwand.command;

import io.github.scarger.sellwand.SellWand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellWandCmd implements CommandExecutor {

    private SellWand plugin;

    public SellWandCmd(SellWand plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length > 2) {
            if (commandSender.hasPermission("sellwand.give") || commandSender.hasPermission("*") || commandSender.isOp()) {
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if(targetPlayer == null) {
                    commandSender.sendMessage(ChatColor.RED + "That player is not online!");
                }
                else {
                    try {
                        int amount = Integer.valueOf(args[2]);
                        targetPlayer.getInventory().addItem(plugin.getSellWand(amount));
                        commandSender
                                .sendMessage(ChatColor.GREEN + "Successfully gave " + amount + " sell wands to " + targetPlayer.getName());
                    }
                    catch (NumberFormatException e) {
                        commandSender.sendMessage(ChatColor.RED + "Please specify a valid number for the amount!");
                        return false;
                    }
                }
            }
            else commandSender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
        }
        else commandSender.sendMessage(ChatColor.YELLOW + "Usage: /sellwand give [player] <amount>");
        return true;
    }
}
