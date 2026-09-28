package com.zenuxs.customweapons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class CustomWeapons extends JavaPlugin implements CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        getCommand("customweapons").setExecutor(this);
        getCommand("customweapons").setTabCompleter(this);
        
        registerCustomRecipes();
        getLogger().info("CustomWeapons enabled with Auto-Complete!");
    }

    @Override
    public void onDisable() {
    }

    private void registerCustomRecipes() {
        ItemStack dragonBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta m1 = dragonBlade.getItemMeta();
        m1.setDisplayName(ChatColor.DARK_PURPLE + "Dragon Blade");
        dragonBlade.setItemMeta(m1);

        NamespacedKey key1 = new NamespacedKey(this, "dragon_blade");
        ShapedRecipe recipe1 = new ShapedRecipe(key1, dragonBlade);
        recipe1.shape(" D ", " D ", " S ");
        recipe1.setIngredient('D', Material.DRAGON_BREATH);
        recipe1.setIngredient('S', Material.NETHERITE_SWORD);
        Bukkit.addRecipe(recipe1);

        ItemStack emeraldHammer = new ItemStack(Material.MACE);
        ItemMeta m4 = emeraldHammer.getItemMeta();
        m4.setDisplayName(ChatColor.GREEN + "Emerald Hammer");
        emeraldHammer.setItemMeta(m4);

        NamespacedKey key2 = new NamespacedKey(this, "emerald_hammer");
        ShapedRecipe recipe2 = new ShapedRecipe(key2, emeraldHammer);
        recipe2.shape("EEE", " E ", " S ");
        recipe2.setIngredient('E', Material.EMERALD_BLOCK);
        recipe2.setIngredient('S', Material.MACE);
        Bukkit.addRecipe(recipe2);
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
                    ItemMeta dmeta = item.getItemMeta();
                    dmeta.setDisplayName(ChatColor.DARK_PURPLE + "Dragon Blade");
                    item.setItemMeta(dmeta);
                    break;
                case "bow":
                    item = new ItemStack(Material.BOW);
                    ItemMeta bmeta = item.getItemMeta();
                    bmeta.setDisplayName(ChatColor.AQUA + "Bow Shooter");
                    item.setItemMeta(bmeta);
                    break;
                case "frozen":
                    item = new ItemStack(Material.DIAMOND_SWORD);
                    ItemMeta fmeta = item.getItemMeta();
                    fmeta.setDisplayName(ChatColor.BLUE + "Frozen Blade");
                    item.setItemMeta(fmeta);
                    break;
                case "emerald":
                    item = new ItemStack(Material.MACE);
                    ItemMeta emeta = item.getItemMeta();
                    emeta.setDisplayName(ChatColor.GREEN + "Emerald Hammer");
                    item.setItemMeta(emeta);
                    break;
                case "wither":
                    item = new ItemStack(Material.NETHERITE_AXE);
                    ItemMeta wmeta = item.getItemMeta();
                    wmeta.setDisplayName(ChatColor.DARK_GRAY + "Wither Hammer");
                    item.setItemMeta(wmeta);
                    break;
                case "wings":
                    item = new ItemStack(Material.ELYTRA);
                    ItemMeta lmeta = item.getItemMeta();
                    lmeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Wings of Allays");
                    item.setItemMeta(lmeta);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Invalid weapon!");
                    return true;
            }

            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.GREEN + "Custom weapon added!");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("customweapons")) {
            if (args.length == 1) {
                completions.add("dragon");
                completions.add("bow");
                completions.add("frozen");
                completions.add("emerald");
                completions.add("wither");
                completions.add("wings");
            }
        }
        return completions;
    }
}
