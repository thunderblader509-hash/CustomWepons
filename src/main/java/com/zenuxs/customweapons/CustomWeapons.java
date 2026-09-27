package com.zenuxs.customweapons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomWeapons extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getCommand("customweapons").setExecutor(this);
        getLogger().info("CustomWeapons has been enabled successfully!");
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("customweapons")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Usage: /customweapons <dragon|bow|frozen|emerald|wither|wings>");
                return true;
            }

            String type = args[0].toLowerCase();
            ItemStack item = null;

            switch (type) {
                case "dragon":
                    item = new ItemStack(Material.NETHERITE_SWORD);
                    ItemMeta m1 = item.getItemMeta();
                    m1.setDisplayName(ChatColor.DARK_PURPLE + "Dragon Blade");
                    item.setItemMeta(m1);
                    break;
                case "bow":
                    item = new ItemStack(Material.BOW);
                    ItemMeta m2 = item.getItemMeta();
                    m2.setDisplayName(ChatColor.AQUA + "Bow Shooter");
                    item.setItemMeta(m2);
                    break;
                case "frozen":
                    item = new ItemStack(Material.DIAMOND_SWORD);
                    ItemMeta m3 = item.getItemMeta();
                    m3.setDisplayName(ChatColor.BLUE + "Frozen Blade");
                    item.setItemMeta(m3);
                    break;
                case "emerald":
                    item = new ItemStack(Material.MACE);
                    ItemMeta m4 = item.getItemMeta();
                    m4.setDisplayName(ChatColor.GREEN + "Emerald Hammer");
                    item.setItemMeta(m4);
                    break;
                case "wither":
                    item = new ItemStack(Material.NETHERITE_AXE);
                    ItemMeta m5 = item.getItemMeta();
                    m5.setDisplayName(ChatColor.DARK_GRAY + "Wither Hammer");
                    item.setItemMeta(m5);
                    break;
                case "wings":
                    item = new ItemStack(Material.ELYTRA);
                    ItemMeta m6 = item.getItemMeta();
                    m6.setDisplayName(ChatColor.LIGHT_PURPLE + "Wings of Allays");
                    item.setItemMeta(m6);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Invalid weapon! Use: dragon, bow, frozen, emerald, wither, wings");
                    return true;
            }

            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.GREEN + "Custom weapon added to your inventory!");
            return true;
        }
        return false;
    }
}
