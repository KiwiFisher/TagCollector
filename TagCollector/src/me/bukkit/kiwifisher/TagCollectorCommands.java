package me.bukkit.kiwifisher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCollectorCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (command.getLabel().equalsIgnoreCase("tags") && commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.hasPermission("tagcollector.gui")) {
                TagCollectorGUI playerInventory = new TagCollectorGUI(player);
                if (playerInventory.getNumberOfTags() > 0) {
                    playerInventory.openInventory();
                } else {
                    player.sendMessage(ChatColor.RED + "Sorry, but your only tag is the one you already have");
                }

            } else {
                player.sendMessage(ChatColor.DARK_RED + "I'm sorry, you don't have permission for that command!");
            }
        }

        return false;
    }
}
