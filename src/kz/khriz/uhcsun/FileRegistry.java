package kz.khriz.uhcsun;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class FileRegistry {

    UHC UHC;
    public FileRegistry(UHC instance) {
        UHC = instance;
    }

    Utilities UTIL = new Utilities(this);

    final File ConcurrentGamesFile = new File("plugins/UHC/Games/", UTIL.getHour() + ".yml");
    final FileConfiguration ConcurrentGames = YamlConfiguration.loadConfiguration(ConcurrentGamesFile);
    public void saveConcurrentGame(){
        try {
            ConcurrentGames.save(ConcurrentGamesFile);
        } catch(IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }
    }

    final File StorageFile = new File("plugins/UHC/Data/", "Storage.yml");
    final FileConfiguration Storage = YamlConfiguration.loadConfiguration(ConcurrentGamesFile);
    public void saveStorage(){
        try {
            Storage.save(ConcurrentGamesFile);
        } catch(IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }
    }

    final File BorderFile = new File("plugins/WorldBorder", "config.yml");
    final FileConfiguration Border = YamlConfiguration.loadConfiguration(BorderFile);
    public void saveBorder(){
        try {
            Border.save(BorderFile);
        } catch(IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }
    }

}
