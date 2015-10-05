package me.bukkit.kiwifisher;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Julian on 22/09/2015.
 */
public class TagCollector extends JavaPlugin {

    public static Plugin plugin;
    public static Logger logger;

    public static Permission permissions = null;

    @Override
    public void onEnable() {

        plugin = this;
        logger = plugin.getLogger();
        setupPermissions();

        logger.info("Loading TagCollector");

        this.getCommand("tags").setExecutor(new TagCollectorCommands());
        this.getServer().getPluginManager().registerEvents(new TagCollectorGUI(), this);



    }

    @Override
    public void onDisable() {

    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

}
