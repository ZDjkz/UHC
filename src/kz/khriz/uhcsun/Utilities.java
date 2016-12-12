package kz.khriz.uhcsun;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import net.minecraft.server.v1_8_R1.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Utilities {

    UHC UHC;
    public Utilities(UHC instance) {
        UHC = instance;
    }

    FileRegistry FileRegistry;
    public Utilities(FileRegistry instance) {
        FileRegistry = instance;
    }

    public String getDate(){
        String Format = "MM/dd/yyyy - HH:mm:ss";
        DateFormat DateFormat = new SimpleDateFormat(Format);
        String Date = DateFormat.format(new Date());
        return Date.toString();
    }

    public String getHour(){
        String Format = "MM-dd-yyyy - HH";
        DateFormat DateFormat = new SimpleDateFormat(Format);
        String Date = DateFormat.format(new Date());
        return Date.toString();
    }

    public String getDay(){
        String Format = "MM-dd-yyyy";
        DateFormat DateFormat = new SimpleDateFormat(Format);
        String Date = DateFormat.format(new Date());
        return Date.toString();
    }

    public void defaultConfig(){
        FileConfiguration Config = UHC.Config;

        Config.set("Settings.Lobby.World", "world");
        UHC.SaveConfig();
    }

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new UHCGenerator(UHC);
    }

    public void startGame(){
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv create UHC normal -g UHC");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv load UHC");

        UHC.FILE.Border.set("worlds.UHC.radiusX", 1000);
        UHC.FILE.Border.set("worlds.UHC.radiusZ", 1000);
        UHC.FILE.Border.set("worlds.UHC.x", 0);
        UHC.FILE.Border.set("worlds.UHC.z", 0);
        UHC.FILE.Border.set("worlds.UHC.wrapping", false);
        UHC.FILE.saveBorder();
        setupGame();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            @Override
            public void run() {
                UHC.Game.put("PVP", "ENABLED");
                Bukkit.getServer().broadcastMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&',"&c&lPVP Has Been Enabled."));
            }

        }, (((15 * 2)) * 20));

    }

    public void shrinkBorder(int Amount){
        int CurrentX = UHC.FILE.Border.getInt("worlds.UHC.radiusX");
        int CurrentZ = UHC.FILE.Border.getInt("worlds.UHC.radiusZ");

        UHC.FILE.Border.set("worlds.UHC.radiusX", CurrentX - Amount);
        UHC.FILE.Border.set("worlds.UHC.radiusZ", CurrentZ - Amount);
        UHC.FILE.saveBorder();
    }

    public void chatTimer(Player p){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            @Override
            public void run() {
                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oThe game will begin in &a&l" + 5 + "&6&o seconds."));
            }
        }, ((4)) * 20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            @Override
            public void run() {
                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oThe game will begin in &e&l" + 3 + "&6&o seconds."));
            }
        }, ((7)) * 20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            @Override
            public void run() {
                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oThe game will begin in &6&l" + 2 + "&6&o seconds."));
            }
        }, ((8)) * 20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            @Override
            public void run() {
                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oThe game will begin in &c&l" + 1 + "&6&o second."));
            }
        }, ((9)) * 20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            @Override
            public void run() {
                Object id = UHC.getGame("GAME ID");
                Object name = UHC.getGame("NAME");

                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&c&lThe Game has Begun."));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&oINFO &9&l- &f&oGame ID; " + id + "   &f&oDate; " + name));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&oYou have 7.5 Minutes to prepare then PvP is Enabled"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&oTIP &9&l- &a&lDon't take fall damage. You can't regen."));
                p.sendMessage("");
            }
        }, ((10)) * 20);
    }

    public void setupGame(){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            ArrayList<String> UsersAlive = new ArrayList<String>();

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                for (Player online : Bukkit.getOnlinePlayers()){
                    Location Spawn = newRandomLoc("UHC", 1000, 100, 1000, 100, 156, 60);
                    online.teleport(Spawn);

                    online.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oThe game will begin in &2&l10&6&o seconds."));
                    chatTimer(online);
                    UsersAlive.add(online.getName());

                    UHC.FILE.ConcurrentGames.set(online.getName() + ".ALIVE", true);
                    UHC.FILE.saveConcurrentGame();

                    online.setMaxHealth(40);
                    online.setHealth(40);
                }
                UHC.FILE.ConcurrentGames.set("ALIVE", UsersAlive);
                UHC.FILE.saveConcurrentGame();
            }
        }, 1 * 20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

            @Override
            public void run() {
                UHC.Game.put("PREGAME", "FALSE");
                UHC.PlayerData.clear();
                UHC.Game.put("STARTED", "TRUE");
            }
        }, 11 * 20);
    }

    //This get's a new random location in the world between the stated parameters.
    @SuppressWarnings("deprecation")
    public Location newRandomLoc(String world, int paraXMax, int paraXMin, int paraZMax, int paraZMin, int maxY, int minY){
        Random random = new Random();
        Location teleportLoc = null;
        Location teleportLoca1 = null;
        Location teleportLoca2 = null;

        World World = Bukkit.getWorld(world);
        int x = random.nextInt((paraXMax - paraXMin) + 1) + paraXMin;
        int z = random.nextInt((paraZMax - paraZMin) + 1) + paraZMin;
        int y = maxY;
        boolean land = false;
        while (land == false){
            teleportLoc = new Location(World, x + 0.5, y + 1, z + 0.5);
            teleportLoca1 = new Location(World, x + 0.5, y + 2, z + 0.5);
            teleportLoca2 = new Location(World, x + 0.5, y + 3, z + 0.5);

            if (teleportLoc.getBlock().getType() != org.bukkit.Material.AIR
                    && teleportLoca1.getBlock().getType() == org.bukkit.Material.AIR
                    && teleportLoca2.getBlock().getType() == org.bukkit.Material.AIR ){
                land = true;
                for (Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a&lFound location."));
                }
            } else {
                if (y <= minY){
                    y = maxY;
                    x = random.nextInt((paraXMax - paraXMin) + 1) + paraXMin;
                    z = random.nextInt((paraZMax - paraZMin) + 1) + paraZMin;
                    for (Player p : Bukkit.getOnlinePlayers()){
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c&oNo Safe Land - Rolling Dice."));
                    }
                } else {
                    y--;
                }
            }
        }

        Location Spawn = new Location(World, x + 0.5, y + 2, z + 0.5 );

        return Spawn;
    }

}