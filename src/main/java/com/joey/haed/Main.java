package com.joey.haed;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.BiConsumer;

public class Main extends JavaPlugin {
    Map<String, FallingBlock> blocks = new HashMap<>();

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
                Location spawnLocation = player.getEyeLocation();

                FallingBlock block = player.getWorld().spawnFallingBlock(spawnLocation, material, (byte)1);
                block.setGlowing(true);
                block.setGlowing(true);

                blocks.put(args[1], block);

                return true;
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (args[1].equalsIgnoreCase("all")) {
                    blocks.clear();

                    return true;
                }

                if (blocks.containsKey(args[1])) {
                    blocks.remove(args[1]);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Cannot find block [" + args[1] + "]");
                }
            }
            else if (args[0].equalsIgnoreCase("rotate")) {
                //todo
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
                    targets.addAll(blocks.keySet());

                    return targets;
                }
            }
        }

        return null;
    }
}
