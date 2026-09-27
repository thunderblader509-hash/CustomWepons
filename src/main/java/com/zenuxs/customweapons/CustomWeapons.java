package com.zenuxs.customweapons;

import org.bukkit.plugin.java.JavaPlugin;

public class CustomWeapons extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new WeaponListeners(), this);
        getLogger().info("CustomWeapons Plugin Successfully Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomWeapons Plugin Disabled!");
    }
}
