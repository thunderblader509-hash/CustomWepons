package com.zenuxs.customweapons;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class CustomWeapons extends JavaPlugin implements CommandExecutor, Listener {

    private final HashMap<UUID, String> emeraldHammerModes = new HashMap<>();

    @Override
    public void onEnable() {
        if (getCommand("customweapons") != null) {
            getCommand("customweapons").setExecutor(this);
        }
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("CustomWeapons (6 Weapons) Loaded Successfully for 1.21.11!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Use: /customweapons <dragon|bow|frozen|emerald|wither|wings>");
            return true;
        }

        String weaponType = args[0].toLowerCase();
        ItemStack item = null;

        switch (weaponType) {
            case "dragon":
                item = new ItemStack(Material.NETHERITE_SWORD);
                setItemMeta(item, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "🐉 Dragon Blade", 
                    List.of(ChatColor.GRAY + "Shift + Right Click: Dragon Breath & Teleport"));
                break;
            case "bow":
                item = new ItemStack(Material.BOW);
                setItemMeta(item, ChatColor.GOLD + "" + ChatColor.BOLD + "🏹 Bow Shooter", 
                    List.of(ChatColor.GRAY + "Shift + Right Click: Rapid Instant Damage Arrows"));
                break;
            case "frozen":
                item = new ItemStack(Material.DIAMOND_SWORD);
                setItemMeta(item, ChatColor.AQUA + "" + ChatColor.BOLD + "❄️ Frozen Blade", 
                    List.of(ChatColor.GRAY + "Shift + Right Click: Freeze Enemies"));
                break;
            case "emerald":
                item = new ItemStack(Material.IRON_AXE);
                setItemMeta(item, ChatColor.GREEN + "" + ChatColor.BOLD + "💚 Emerald Hammer", 
                    List.of(ChatColor.GRAY + "Right Click: Dash", ChatColor.GRAY + "Shift + Right Click: Launch", ChatColor.GRAY + "Shift + Left Click: Switch Breach/Density"));
                break;
            case "wither":
                item = new ItemStack(Material.NETHERITE_AXE);
                setItemMeta(item, ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "💀 Wither Hammer", 
                    List.of(ChatColor.GRAY + "Shift + Right Click: Wither Effect", ChatColor.GRAY + "Shift + Left Click: Strength II"));
                break;
            case "wings":
                item = new ItemStack(Material.ELYTRA);
                setItemMeta(item, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "🪽 Wings of Allays", 
                    List.of(ChatColor.GRAY + "Shift + Right Click while flying: Flight Boost"));
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown weapon name! Use: dragon, bow, frozen, emerald, wither, wings");
                return true;
        }

        if (item != null) {
            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.GREEN + "Given " + weaponType + " weapon!");
        }
        return true;
    }

    private void setItemMeta(ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        String name = item.getItemMeta().getDisplayName();

        boolean isShift = player.isSneaking();
        Action action = event.getAction();

        // 1. Dragon Blade
        if (name.contains("Dragon Blade") && isShift && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            player.getWorld().spawn(player.getLocation(), DragonFireball.class);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1f, 1f);
            
            Location targetLoc = player.getTargetBlock(null, 30).getLocation();
            targetLoc.setPitch(player.getLocation().getPitch());
            targetLoc.setYaw(player.getLocation().getYaw());
            player.teleport(targetLoc);
            player.getWorld().spawnParticle(Particle.PORTAL, targetLoc, 30, 0.5, 1, 0.5, 0.1);
            player.getWorld().playSound(targetLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        }

        // 2. Bow Shooter
        else if (name.contains("Bow Shooter") && isShift && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            Arrow arrow = player.launchProjectile(Arrow.class);
            arrow.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 1, 1), true);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1f, 1.5f);
        }

        // 3. Frozen Blade
        else if (name.contains("Frozen Blade") && isShift && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity) entity;
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 255, false, false));
                    target.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation(), 40, 0.5, 1, 0.5, 0.1);
                    target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 0.5f);
                }
            }
            player.sendMessage(ChatColor.AQUA + "Nearby enemies frozen!");
        }

        // 4. Emerald Hammer (Dash, Launch, and Shift + Left Click Breach/Density Mode Switch)
        else if (name.contains("Emerald Hammer")) {
            if (!isShift && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
                event.setCancelled(true);
                Vector dash = player.getLocation().getDirection().multiply(1.5).setY(0.2);
                player.setVelocity(dash);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5f, 2f);
            } else if (isShift && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
                event.setCancelled(true);
                for (Entity entity : player.getNearbyEntities(4, 4, 4)) {
                    if (entity instanceof LivingEntity) {
                        entity.setVelocity(new Vector(0, 1.5, 0));
                        entity.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, entity.getLocation(), 20, 0.5, 1, 0.5);
                        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 1f);
                    }
                }
            } else if (isShift && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
                event.setCancelled(true);
                UUID uuid = player.getUniqueId();
                String currentMode = emeraldHammerModes.getOrDefault(uuid, "DENSITY");

                if (currentMode.equals("DENSITY")) {
                    emeraldHammerModes.put(uuid, "BREACH");
                    player.sendActionBar(ChatColor.RED + "⚔ EMERALD BREACH MODE ACTIVATED");
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1f, 1.5f);
                } else {
                    emeraldHammerModes.put(uuid, "DENSITY");
                    player.sendActionBar(ChatColor.GOLD + "💥 EMERALD DENSITY MODE ACTIVATED");
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1f, 0.5f);
                }
            }
        }

        // 5. Wither Hammer (Strength II on Shift + Left click)
        else if (name.contains("Wither Hammer") && isShift && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 1, false, true));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.5f);
            player.sendMessage(ChatColor.DARK_GRAY + "Strength II Activated for 20 seconds!");
        }

        // 6. Wings of Allays (Flight Boost)
        else if (name.contains("Wings of Allays") && player.isGliding() && isShift && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            Vector boost = player.getLocation().getDirection().multiply(1.2).setY(0.6);
            player.setVelocity(boost);
            player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1f, 1f);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();
            
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String name = item.getItemMeta().getDisplayName();
                
                if (name.contains("Wither Hammer") && player.isSneaking() && event.getEntity() instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity) event.getEntity();
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 140, 1));
                    target.getWorld().spawnParticle(Particle.SMOKE, target.getLocation(), 30, 0.5, 1, 0.5, 0.05);
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_HURT, 1f, 1f);
                }
            }
        }
    }
                                        }
