package com.joey.haed;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    Map<String, ArmorStand> stands = new HashMap<>();

    @Override
    public void onEnable() {
        System.out.println("Haed is activated");
    }

    @Override
    public void onDisable() {
        System.out.println("Haed is deactivated");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("fe")) {
            if (args[0].equalsIgnoreCase("spawn")) {
                Material material = player.getInventory().getItemInMainHand().getType();
                Location location = player.getEyeLocation();
                ItemStack stack = new ItemStack(material);

                ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                stand.setGravity(false);
                //stand.setGlowing(true);
                stand.setVisible(false);
                stand.setHelmet(stack);

                stands.put(args[1], stand);

                player.sendMessage(ChatColor.GREEN + "stand [" + args[1] + "] is spawned");

                return true;
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (args[1].equalsIgnoreCase("all")) {
                    for (ArmorStand i : stands.values()) {
                        i.remove();
                    }
                    stands.clear();

                    return true;
                }

                if (stands.containsKey(args[1])) {
                    stands.get(args[1]).remove();
                    stands.remove(args[1]);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                }
            }
            else if (args[0].equalsIgnoreCase("rotate")) {
                if (args[1].equalsIgnoreCase("all")) {
                    for (ArmorStand i : stands.values()) {
                        i.setRotation(45,45);
                    }

                    return true;
                }

                if (stands.containsKey(args[1])) {
                    stands.get(args[1]).setRotation(Integer.parseInt(args[2]), 45);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("fe")) {
            if (args.length == 1) {
                List<String> modes = new ArrayList<>();

                modes.add("spawn");
                modes.add("remove");
                modes.add("rotate");

                return modes;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rotate")) {
                    List<String> targets = new ArrayList<>();

                    targets.add("all");
                    targets.addAll(stands.keySet());

                    return targets;
                }
            }
        }

        return null;
    }
}
