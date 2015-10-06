package me.bukkit.kiwifisher;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Julian on 22/09/2015.
 */
public class TagCollectorGUI implements Listener{

    private Inventory inventory;
    private Player player;

    public TagCollectorGUI createInventory(Player player) {

        this.setPlayer(player);
        ArrayList<String> userTags = getTagRanks();

        int size = 0;

        for (String group : userTags) { size++; }

        int rows = (size / 6) + 1;

        try {
            inventory = Bukkit.createInventory(null, rows * 9, player.getDisplayName() + "'s Chat Tags");
        } catch (IllegalArgumentException e) {

            inventory = Bukkit.createInventory(null, rows * 9, " Your Chat Tags");
        }

        ItemStack offButton = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta offButtonMeta = offButton.getItemMeta();
        offButtonMeta.setDisplayName(ChatColor.GRAY + "Trainer");
        offButton.setItemMeta(offButtonMeta);

        inventory.setItem(8, offButton);



        setInventoryItems(userTags);
        setInventory(inventory);

        return this;

    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void openInventory() {

        this.getPlayer().openInventory(this.getInventory());

    }

    public void setInventoryItems(ArrayList<String> tagsArray) {

        int position = 0;
        int endOfRow = 7;

        for (String group : tagsArray) {

            ItemStack item = new ItemStack(Material.STAINED_GLASS, 1, (byte) 5);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + group);
            item.setItemMeta(itemMeta);

            this.getInventory().setItem(position, item);

            position += 1;

            if (position == endOfRow) {
                position += 2;
                endOfRow += 9;
            }

        }

    }

    public ArrayList<String> getTagRanks() {

        ArrayList<String> tagRanks = new ArrayList<>();

        for (String group : TagCollector.permissions.getGroups()) {

            if (TagCollector.permissions.playerInGroup(this.getPlayer(), group) && (!group.equalsIgnoreCase("default") || !group.equalsIgnoreCase("Trainer"))) {
                tagRanks.add(group);
            }

        }

        return tagRanks;

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        event.setCancelled(true);

        try {

            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            Player whoClicked = (Player) event.getWhoClicked();

            TagCollector.plugin.getServer().dispatchCommand(TagCollector.plugin.getServer().getConsoleSender(), "pex user " + whoClicked.getDisplayName() + " group remove " + itemName);

            TagCollector.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TagCollector.plugin, new Runnable() {
                @Override
                public void run() {
                    TagCollector.plugin.getServer().dispatchCommand( TagCollector.plugin.getServer().getConsoleSender(), "pex user " + whoClicked.getDisplayName() + " group add " + itemName);
                }
            }, 10);

            whoClicked.sendMessage(ChatColor.GREEN + "Set your chat tag to " + ChatColor.BOLD + itemName + ChatColor.GREEN + "!");
            whoClicked.closeInventory();

        } catch (Exception e) { }

        finally {

            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

        }



    }

}
