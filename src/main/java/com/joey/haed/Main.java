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
                        if (args.length == 4) {
                            if (args[2].equalsIgnoreCase("with")) {
                                World world = i.getWorld();
                                Location location = i.getLocation().add(0, i.getEyeHeight(), 0);

                                world.spawnParticle(Particle.valueOf(args[3]), location, 50);
                            }
                        }
                        i.remove();
                    }
                    stands.clear();

                    return true;
                }

                if (stands.containsKey(args[1])) {
                    if (args.length == 4) {
                        if (args[2].equalsIgnoreCase("with")) {
                            World world = stands.get(args[1]).getWorld();
                            Location location = stands.get(args[1]).getLocation().add(0, stands.get(args[1]).getEyeHeight(), 0);

                            world.spawnParticle(Particle.valueOf(args[3]), location, 50);
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
                        final int[] degree = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (ArmorStand i : stands.values()) {
                                    i.setRotation(degree[0], 45);
                                    degree[0] += Integer.parseInt(args[3]);
                                    if (degree[0] >= 360) {
                                        degree[0] = max360(degree[0], Integer.parseInt(args[3]));
                                    }
                                }
                            }
                        }.runTaskTimer(this, 0, 1);

                        return true;
                    }

                    if (stands.containsKey(args[2])) {
                        final int[] term = {0};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (stands.containsKey(args[2])) {
                                    stands.get(args[2]).setRotation(term[0], 45);
                                    term[0] += Integer.parseInt(args[3]);
                                    if (term[0] >= 360) {
                                        term[0] = max360(term[0], Integer.parseInt(args[3]));
                                    }
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
                else if (args[1].equalsIgnoreCase("stop")) {
                    //todo
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
                            return true;
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
                            return true;
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
                            return true;
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
                            return true;
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
                Player target = Bukkit.getPlayer(args[1]);

                sword.setVisible(false);
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
                            stands.get("sword").teleport(target.getLocation());
                        }
                        else {
                            cancel();
                        }
                    }
                }.runTaskTimer(this, 0, 1);
            }

            //큰 블럭 만들기
            else if (args[0].equalsIgnoreCase("hugeBlock")) {
                Material material = player.getInventory().getItemInMainHand().getType();
                Location location = player.getEyeLocation();
                ItemStack stack = new ItemStack(material);

                int num = 1;
                int mx = Integer.parseInt(args[1]);
                int my = Integer.parseInt(args[2]);
                int mz = Integer.parseInt(args[3]);
                for (double xc = 0; xc < mx; xc+=1) {
                    for (double yc = 0; yc < my; yc+=1) {
                        for (double zc = 0; zc < mz; zc+=1) {
                            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(xc * 0.6, yc * 0.6, zc * 0.6), EntityType.ARMOR_STAND);

                            stand.setHelmet(stack);
                            stand.setGravity(false);
                            stand.setVisible(false);
                            stand.setRotation(0, 45);

                            stands.put("block" + num, stand);

                            num += 1;
                        }
                    }
                }
            }

            //명령어 도우미
            else if (args[0].equalsIgnoreCase("help")) {
                if (args[1].equalsIgnoreCase("spawn")) {
                    player.sendMessage(ChatColor.YELLOW + "usage: " + ChatColor.GREEN + "/fe spawn <name:string>");
                }
                else if (args[1].equalsIgnoreCase("remove")) {
                    player.sendMessage(ChatColor.YELLOW + "usage: " + ChatColor.GREEN + "/fe remove <name:string>");
                }
                else if (args[1].equalsIgnoreCase("rotate")) {
                    player.sendMessage(ChatColor.YELLOW + "usage: " + ChatColor.GREEN + "/fe rotate <degree/forever> <target:target> <radius:int>");
                }
                else if (args[1].equalsIgnoreCase("setting")) {
                    player.sendMessage(ChatColor.YELLOW + "usage: " + ChatColor.GREEN + "/fe setting <type:settingType> <enable:boolean>");
                }
                else if (args[1].equalsIgnoreCase("goblin")) {
                    player.sendMessage(ChatColor.YELLOW + "usage: " + ChatColor.GREEN + "/fe goblin");
                }
                else if (args[1].equalsIgnoreCase("hugeBlock")) {
                    player.sendMessage(ChatColor.YELLOW + "usage: " + ChatColor.GREEN + "/fe hugeBlock");
                }
                else if (args[1].equalsIgnoreCase("help")) {

                }
            }

            //파티클 생성
            else if (args[0].equalsIgnoreCase("particle")) {
                if (args[1].equalsIgnoreCase("all")) {
                    for (ArmorStand i : stands.values()) {
                        World world = i.getWorld();
                        Location location = i.getLocation().add(0, i.getEyeHeight(), 0);

                        world.spawnParticle(Particle.valueOf(args[2]), location, Integer.parseInt(args[3]));
                    }
                }

                if (stands.containsKey(args[1])) {
                    World world = stands.get(args[1]).getWorld();
                    Location location = stands.get(args[1]).getLocation().add(0, stands.get(args[1]).getEyeHeight(), 0);;

                    world.spawnParticle(Particle.valueOf(args[2]), location, Integer.parseInt(args[3]));
                }
            }

            //플레이어한테 attach
            else if (args[0].equalsIgnoreCase("attach")) {
                if (args[1].equalsIgnoreCase("")) {
                    //todo
                }
            }
        }

        return false;
    }

    int max360(int degree, int degree2) {
        int num = 0;
        for (int i = 0; i < degree2; i+=1) {
            num += 1;
            if (degree + num == 360) {
                return degree2 - num;
            }
        }

        return 0;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("fe")) {
            if (args.length == 1) {
                List<String> subs = new ArrayList<>();

                subs.add("spawn");
                subs.add("remove");
                subs.add("rotate");
                subs.add("setting");
                subs.add("goblin");
                subs.add("hugeBlock");
                subs.add("help");
                subs.add("particle");

                return subs;
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
                else if (args[0].equalsIgnoreCase("particle")) {
                    List<String> targets = new ArrayList<>();

                    targets.add("all");
                    targets.addAll(stands.keySet());

                    return targets;
                }
                else if (args[0].equalsIgnoreCase("help")) {
                    List<String> subs = new ArrayList<>();

                    subs.add("spawn");
                    subs.add("remove");
                    subs.add("rotate");
                    subs.add("setting");
                    subs.add("goblin");
                    subs.add("hugeBlock");
                    subs.add("help");
                    subs.add("particle");

                    return subs;
                }
                else if (args[0].equalsIgnoreCase("goblin")) {
                    List<String> targets = new ArrayList<>();

                    targets.add("@all");
                    targets.add("@local");
                    targets.add("@random");

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
                else if (args[0].equalsIgnoreCase("particle")) {
                    List<String> types = new ArrayList<>();

                    for (Particle i : Particle.values()) {
                        types.add(i.toString());
                    }

                    return types;
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

                    for (Particle i : Particle.values()) {
                        types.add(i.toString());
                    }

                    return types;
                }
            }
        }

        return null;
    }
}
