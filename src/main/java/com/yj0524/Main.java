package com.yj0524;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class Main extends JavaPlugin {

    public FileConfiguration config;
    public List<String> data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin Enabled");

        // Config.yml 파일 생성
        loadConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Disabled");
    }

    public void loadConfig() {
        // Load config
        FileConfiguration config = getConfig();
        // default : Arrays.asList("wow", "wonderful", "god")
        data = config.getStringList("datum");
        if (data == null || data.isEmpty()) {
            // datum 값이 없을 경우 기본값으로 설정
            data = Arrays.asList("wow", "wonderful", "god");
            config.set("datum", data);
            saveConfig();
        }
        // Save config
        config.set("datum", data);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("configreload")) {
            if (sender.isOp()) {
                reloadConfig();
                config = getConfig();
                data = config.getStringList("datum");
                sender.sendMessage(ChatColor.GREEN + "Config reloaded.");
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("roulette")) {
            if (sender.isOp()) {
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count >= 40) {
                            this.cancel();

                            String selectedData = data.get(new Random().nextInt(data.size()));

                            for (Player player : getServer().getOnlinePlayers()) {
                                player.sendTitle(ChatColor.GREEN + "뽑기 결과", ChatColor.YELLOW + selectedData, 0, 60, 20);
                                player.playSound(player.getLocation(), "entity.player.levelup", 1, 1);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.setTitleTimes(10, 70, 20);
                                    }
                                }.runTaskLater(Main.this, 80L);
                            }

                            return;
                        }

                        String dataValue = "";
                        String oldDataValue = "";

                        while(true) {
                            dataValue = data.get(new Random().nextInt(data.size()));
                            if (!dataValue.equals(oldDataValue)) {
                                oldDataValue = dataValue;
                                break;
                            }
                        }

                        for (Player player : getServer().getOnlinePlayers()) {
                            player.sendTitle(ChatColor.GREEN + "뽑는 중...", ChatColor.YELLOW + dataValue, 0, 3, 0);
                            player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1, 1);
                        }

                        count++;
                    }
                }.runTaskTimer(this, 0L, 1L);
            }
        }
        return false;
    }
}
