package kz.khriz.uhcsun;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class UHC extends JavaPlugin {

    //This is the API, You should'nt have to use this.
    APILoad API = new APILoad(this);
    //This is Utilities.java allowing for easy access between classes and use of methods in there.
    Utilities UTIL = new Utilities(this);
    //This is the FileRegistry to save and access files. "UHC.FILE.get("<INSERT NAME>");
    FileRegistry FILE = new FileRegistry(this);
    //Here is the UHC Prefix.
    public String PREFIX = ChatColor.translateAlternateColorCodes('&', "&c&lU&6&lH&e&lC &e&lS&6&lU&c&lN &f&o- ");

    @Override
    public void onEnable() {
        API.loadAPI();
        API.setupPermissions();
        loadUHC();
    }

    @Override
    public void onDisable() {
        FILE.ConcurrentGames.set("ALIVE", null);
        FILE.saveConcurrentGame();
        clearGame();
        PlayerData.clear();
        DamageMap.clear();
        DamageTook.clear();
    }

    public void loadUHC(){
        getCommand("UHC").setExecutor(new UHCCommand(this));
        getServer().getPluginManager().registerEvents(new LobbyEvents(this), this);
        getServer().getPluginManager().registerEvents(new PreGameEvents(this), this);
        getServer().getPluginManager().registerEvents(new GameEvents(this), this);

        UTIL.defaultConfig();
    }

    // Here are the Current Hash Maps.
    // Player Data is for the Amount of say coins and custom crafting recipies they used.
    // Game is all the Game data we use for knowing what events to register.
    // DamageMap for the amount of damage a player dealt to another player
    // DamageTook is the amount of damage a single player has took in total.
    HashMap<Object, Object> PlayerData = new HashMap<Object, Object>();
    HashMap<Object, Object> Game = new HashMap<Object, Object>();
    HashMap<Object, Double> DamageMap = new HashMap<Object, Double>();
    HashMap<Object, Double> DamageTook = new HashMap<Object, Double>();

    public void setGame(){
        Game.put("NAME", UTIL.getHour());
        Game.put("GAME ID", Config.getInt("GAMES") + 1);
        Game.put("PVP", "DISABLED");

        UTIL.startGame();
    }

    public Object getGame(String QUERY){

        Object Value = null;
        if (QUERY == "NAME"){
            Value = Game.get("NAME");
        }
        if (QUERY == "GAME ID"){
            Value = Game.get("GAME ID");
        }
        return Value;
    }

    public void clearGame(){
        Game.clear();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv delete UHC");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvconfirm");
    }

    final File ConfigYML = new File("plugins/UHC", "Config.yml");
    final FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigYML);
    public void SaveConfig(){
        try {
            Config.save(ConfigYML);
        } catch(IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }
    }

}
