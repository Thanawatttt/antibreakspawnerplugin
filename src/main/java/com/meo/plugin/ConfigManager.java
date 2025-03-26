package com.meo.plugin;

import com.moandjiezana.toml.Toml;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;

public class ConfigManager {
    private final JavaPlugin plugin;
    private Toml config;
    private File configFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.toml");

        if (!configFile.exists()) {
            try (InputStream in = plugin.getResource("config.toml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create config file!");
                e.printStackTrace();
                return;
            }
        }

        try {
            config = new Toml().read(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Could not load config file!");
            e.printStackTrace();
        }
    }

    public boolean canBreakSpawner() {
        return config.getTable("settings").getBoolean("break_spawner", false);
    }

    public String getTitle() {
        return config.getTable("messages").getString("title", "§c§lไม่สามารถทุบได้");
    }

    public String getSubtitle() {
        return config.getTable("messages").getString("subtitle", "§7คุณไม่มีสิทธิ์ในการทำลาย Spawner");
    }

    public void reloadConfig() {
        loadConfig();
    }
}
