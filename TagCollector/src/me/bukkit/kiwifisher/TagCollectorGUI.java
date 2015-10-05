package me.bukkit.kiwifisher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

        try {
            inventory = Bukkit.createInventory(null, 9, player.getDisplayName() + "'s Chat Tags");
        } catch (IllegalArgumentException e) {

            inventory = Bukkit.createInventory(null, 9, " Your Chat Tags");
        }

        ItemStack offButton = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta offButtonMeta = offButton.getItemMeta();
        offButtonMeta.setDisplayName(ChatColor.GRAY + "Trainer Tag");
        offButton.setItemMeta(offButtonMeta);

        inventory.setItem(8, offButton);

        setInventoryItems(getTagRanks());
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

        for (String group : tagsArray) {

            ItemStack item = new ItemStack(Material.APPLE, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + group);
            item.setItemMeta(itemMeta);

            this.getInventory().setItem(position, item);

            position += 1;


        }

    }

    public ArrayList<String> getTagRanks() {

        ArrayList<String> tagRanks = new ArrayList<String>();

        for (String group : TagCollector.permissions.getGroups()) {

            if (TagCollector.permissions.playerInGroup(this.getPlayer(), group) && !group.equalsIgnoreCase("default")) {
                tagRanks.add(group);
            }

        }

        return tagRanks;

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        event.setCancelled(true);

        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
        Player player = (Player) event.getWhoClicked();

        Bukkit.broadcastMessage(ChatColor.stripColor("Set" + player.getDisplayName() + " to " + itemName));

        TagCollector.permissions.playerRemoveGroup(this.getPlayer(), itemName);
        TagCollector.permissions.playerAddGroup(this.getPlayer(), itemName);

    }

}
