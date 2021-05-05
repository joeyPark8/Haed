package com.joey.haed;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
            //스탠드 소환
            if (args[0].equalsIgnoreCase("spawn")) {
                Material material = player.getInventory().getItemInMainHand().getType();
                Location location = player.getEyeLocation();
                ItemStack stack = new ItemStack(material);

                ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                stand.setGravity(false);
                //stand.setGlowing(true);
                stand.setVisible(false);
                stand.setHelmet(stack);
                stand.setRotation(0, 45);

                stands.put(args[1], stand);

                player.sendMessage(ChatColor.GREEN + "stand [" + args[1] + "] is spawned");

                return true;
            }

            //스탠드 제거
            else if (args[0].equalsIgnoreCase("remove")) {
                if (args[1].equalsIgnoreCase("all")) {
                    for (ArmorStand i : stands.values()) {
                        i.remove();
                    }
                    stands.clear();

                    return true;
                }

                if (stands.containsKey(args[1])) {
                    if (args.length == 4) {
                        if (args[2].equalsIgnoreCase("with")) {
                            if (args[3].equalsIgnoreCase("rocket")) {
                                World world = stands.get(args[1]).getWorld();
                                Location location = stands.get(args[1]).getLocation().add(0, stands.get(args[1]).getEyeHeight(), 0);

                                world.spawnParticle(Particle.FIREWORKS_SPARK, location, 50);
                            }
                        }
                    }
                    stands.get(args[1]).remove();
                    stands.remove(args[1]);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                }
            }

            //회전
            else if (args[0].equalsIgnoreCase("rotate")) {
                if (args[1].equalsIgnoreCase("forever")) {
                    if  (args[2].equalsIgnoreCase("all")) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                //todo
                            }
                        }.runTaskTimer(this, 0, 2);
                    }

                    if (stands.containsKey(args[2])) {
                        final int[] term = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                stands.get(args[2]).setRotation(term[0], 45);
                                term[0] += Integer.parseInt(args[3]);
                                if (term[0] >= 360) {
                                    term[0] = 0;
                                }
                            }
                        }.runTaskTimer(this, 0, 1);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[2] + "]");
                    }
                }
                else if (args[1].equalsIgnoreCase("degree")) {
                    if (args[2].equalsIgnoreCase("all")) {
                        for (ArmorStand i : stands.values()) {
                            i.setRotation(Integer.parseInt(args[3]),45);
                        }

                        return true;
                    }

                    if (stands.containsKey(args[2])) {
                        stands.get(args[2]).setRotation(Integer.parseInt(args[3]), 45);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                    }
                }
            }

            //설정
            else if (args[0].equalsIgnoreCase("setting")) {
                if (args[2].equalsIgnoreCase("glowing")) {
                    if (args[3].equalsIgnoreCase("true")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            for (ArmorStand i : stands.values()) {
                                i.setGlowing(true);
                            }
                        }

                        if (stands.containsKey(args[1])) {
                            stands.get(args[1]).setGlowing(true);
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                        }
                    }
                    else if (args[3].equalsIgnoreCase("false")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            for (ArmorStand i : stands.values()) {
                                i.setGlowing(false);
                            }
                        }

                        if (stands.containsKey(args[1])) {
                            stands.get(args[1]).setGlowing(false);
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                        }
                    }
                }
                else if (args[2].equalsIgnoreCase("visible")) {
                    if (args[3].equalsIgnoreCase("true")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            for (ArmorStand i : stands.values()) {
                                i.setVisible(true);
                            }
                        }

                        if (stands.containsKey(args[1])) {
                            stands.get(args[1]).setVisible(true);
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                        }
                    }
                    else if (args[3].equalsIgnoreCase("false")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            for (ArmorStand i : stands.values()) {
                                i.setVisible(false);
                            }
                        }

                        if (stands.containsKey(args[1])) {
                            stands.get(args[1]).setVisible(false);
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "Cannot find stand [" + args[1] + "]");
                        }
                    }
                }
            }

            //도깨비
            else if (args[0].equalsIgnoreCase("goblin")) {
                ArmorStand sword = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);

                sword.setVisible(true);
                sword.setGravity(false);
                sword.setItemInHand(stack);
                sword.getLocation().setDirection(player.getLocation().getDirection());

                stands.put("sword", sword);

                player.sendMessage("날이 좋아서.\n" +
                        "날이 좋지 않아서.\n" +
                        "날이 적당해서.\n" +
                        "모든 날이 좋았다.\n");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (stands.containsKey("sword")) {
                            sword.getLocation().setDirection(player.getLocation().getDirection());
                        }
                        else {
                            cancel();
                        }
                    }
                }.runTaskTimer(this, 0, 2);
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
                modes.add("setting");
                modes.add("goblin");

                return modes;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove")) {
                    List<String> targets = new ArrayList<>();

                    targets.add("all");
                    targets.addAll(stands.keySet());

                    return targets;
                }
                else if (args[0].equalsIgnoreCase("rotate")) {
                    List<String> types = new ArrayList<>();

                    types.add("forever");
                    types.add("degree");

                    return types;
                }
                else if (args[0].equalsIgnoreCase("setting")) {
                    List<String> targets = new ArrayList<>();

                    targets.add("all");
                    targets.addAll(stands.keySet());

                    return targets;
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("rotate")) {
                    List<String> targets = new ArrayList<>();

                    targets.add("all");
                    targets.addAll(stands.keySet());

                    return targets;
                }
                else if (args[0].equalsIgnoreCase("setting")) {
                    List<String> types = new ArrayList<>();

                    types.add("glowing");
                    types.add("visible");

                    return types;
                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    List<String> extra = new ArrayList<>();

                    extra.add("with");

                    return extra;
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("setting")) {
                    List<String> bool = new ArrayList<>();

                    bool.add("true");
                    bool.add("false");

                    return bool;
                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    List<String> types = new ArrayList<>();

                    types.add("rocket");

                    return types;
                }
            }
        }

        return null;
    }
}
