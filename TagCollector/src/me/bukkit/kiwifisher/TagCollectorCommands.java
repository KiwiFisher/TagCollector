package me.bukkit.kiwifisher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Julian on 22/09/2015.
 */

public class TagCollectorCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (command.getLabel().equalsIgnoreCase("tags") && commandSender instanceof Player) {
            Player player = (Player) commandSender;

            player.sendMessage(ChatColor.GREEN + "Opening the tags GUI.");
            TagCollectorGUI playerInventory = new TagCollectorGUI().createInventory(player);
            playerInventory.openInventory();
            player.sendMessage(playerInventory.getTagRanks().toString());
        }

        return false;
    }
}
