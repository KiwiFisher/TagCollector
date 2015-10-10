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

public class TagCollectorGUI implements Listener{

    protected static Inventory inventory;
    private static ArrayList<String> userTags;
    private static Player player;
    private int numberOfTags = 0;

    public TagCollectorGUI() {

    }

    public TagCollectorGUI(Player player) {

        this.setPlayer(player);
        userTags = getTagRanks();

        for (String group : userTags) { this.numberOfTags++; }

        int rows = (this.numberOfTags / 6) + 1;

        try {
            inventory = Bukkit.createInventory(null, rows * 9, player.getName() + "'s Chat Tags");
        } catch (IllegalArgumentException e) {

            inventory = Bukkit.createInventory(null, rows * 9, "Your Chat Tags");
        }

        ItemStack defaultButton = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta defaultButtonMeta = defaultButton.getItemMeta();
        defaultButtonMeta.setDisplayName(ChatColor.GRAY + "Trainer");
        defaultButton.setItemMeta(defaultButtonMeta);

        inventory.setItem(8, defaultButton);

        setInventoryItems(userTags);
        setInventory(inventory);

    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        TagCollectorGUI.player = player;
    }

    public void setInventory(Inventory inventory) {
        TagCollectorGUI.inventory = inventory;
    }

    public void openInventory() {

        this.getPlayer().openInventory(this.getInventory());

    }

    public void setInventoryItems(ArrayList<String> tagsArray) {

        int position = 0;
        int endOfRow = 7;

        for (String group : tagsArray) {

            ItemStack item = new ItemStack(Material.NAME_TAG);
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

            if (TagCollector.permissions.playerInGroup(this.getPlayer(), group) && (!group.equalsIgnoreCase("default") && !group.equalsIgnoreCase("Trainer"))) {
                tagRanks.add(group);
            }

        }

        return tagRanks;

    }

    public int getNumberOfTags() {
        return this.numberOfTags;
    }

    public static ArrayList<String> getUserTags() {
        return userTags;
    }

    public static void setUserTags(ArrayList<String> userTags) {
        TagCollectorGUI.userTags = userTags;
    }

    public void setNumberOfTags(int numberOfTags) {
        this.numberOfTags = numberOfTags;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        boolean clickedValidItem;

        try {
            clickedValidItem = (getUserTags().contains(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))
                    || ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Trainer"));
        } catch (Exception e) { clickedValidItem = false; }

        if (getInventory().getName().equals(event.getInventory().getName())) {
            event.setCancelled(true);
        }

        if (getInventory().getName().equals(event.getInventory().getName()) && clickedValidItem) {

            try {

                String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                Player whoClicked = (Player) event.getWhoClicked();

                TagCollector.logger.info("pex user " + whoClicked.getName() + " group remove " + itemName);

                TagCollector.plugin.getServer().dispatchCommand(TagCollector.plugin.getServer().getConsoleSender(), "pex user " + whoClicked.getName() + " group remove " + itemName);


            /*
            The runable adds a 0.5s delay so that the system can execute the commands accurately.
             */
                TagCollector.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TagCollector.plugin, new Runnable() {
                    @Override
                    public void run() {
                        TagCollector.logger.info("pex user " + whoClicked.getName() + " group add " + itemName);
                        TagCollector.plugin.getServer().dispatchCommand( TagCollector.plugin.getServer().getConsoleSender(), "pex user " + whoClicked.getName() + " group add " + itemName);
                    }
                }, 10);

                whoClicked.sendMessage(ChatColor.GREEN + "Set your chat tag to " + ChatColor.BOLD + itemName + ChatColor.GREEN + "!");
                whoClicked.closeInventory();

                whoClicked.closeInventory();

            } catch (Exception e) {

                TagCollector.logger.info("Tag collector threw an error making the GUI");

            }

        }
    }

}
