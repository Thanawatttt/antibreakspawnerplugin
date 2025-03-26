package com.meo.plugin;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin implements Listener {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Initialize config
        this.configManager = new ConfigManager(this);
        
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        
        // Register commands
        this.getCommand("antibreakspawner").setExecutor(this);
        
        getLogger().info("AntiBreakSpawer has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiBreakSpawer has been disabled!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.SPAWNER) {
            Player player = event.getPlayer();
            
            // Check for admin permission first
            if (player.hasPermission("antibreakspawner.admin")) {
                return;
            }
            
            // Check for bypass permission
            if (player.hasPermission("antibreakspawner.bypass.break")) {
                return;
            }
            
            // Check if breaking spawners is allowed in config
            if (!configManager.canBreakSpawner()) {
                event.setCancelled(true);
                
                // Show title
                player.sendTitle(
                    configManager.getTitle(),
                    configManager.getSubtitle(),
                    10, // fade in time (ticks)
                    70, // stay time (ticks)
                    20  // fade out time (ticks)
                );
                
                // Play sound
                player.playSound(
                    player.getLocation(),
                    Sound.BLOCK_ANVIL_LAND,
                    1.0f, // volume
                    1.0f  // pitch
                );
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("antibreakspawner")) {
            if (!sender.hasPermission("antibreakspawner.admin")) {
                sender.sendMessage("§cคุณไม่มีสิทธิ์ใช้คำสั่งนี้");
                return true;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                configManager.reloadConfig();
                sender.sendMessage("§aรีโหลดการตั้งค่าเรียบร้อยแล้ว!");
                return true;
            }
        }
        return false;
    }
}
