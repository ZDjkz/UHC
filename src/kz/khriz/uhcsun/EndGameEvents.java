package kz.khriz.uhcsun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EndGameEvents implements Listener {

    UHC UHC;
    public EndGameEvents(UHC instance) {
        UHC = instance;
    }

    @EventHandler
    public void Regen(EntityRegainHealthEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        final File ConcurrentGamesFile = new File("plugins/UHC/Games/", UHC.Game.get("GAME ID") + ".yml");
        final FileConfiguration ConcurrentGames = YamlConfiguration.loadConfiguration(ConcurrentGamesFile);
        ArrayList<String> UsersAlive = (ArrayList<String>) ConcurrentGames.getStringList("ALIVE");
        Player p = (Player) e.getEntity();

        if (UsersAlive.contains(p.getUniqueId())){
            e.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void Death(PlayerDeathEvent e){
        final File ConcurrentGamesFile = new File("plugins/UHC/Games/", UHC.Game.get("GAME ID") + ".yml");
        final FileConfiguration ConcurrentGames = YamlConfiguration.loadConfiguration(ConcurrentGamesFile);

        ArrayList<String> UsersAlive = (ArrayList<String>) ConcurrentGames.getStringList("ALIVE");
        Player p = (Player) e.getEntity();

        UsersAlive.remove(p.getName());
        ConcurrentGames.set("ALIVE", UsersAlive);
        try {
            ConcurrentGames.save(ConcurrentGamesFile);
        } catch (IOException exx) {
            Bukkit.getServer().getConsoleSender().sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }

        ConcurrentGames.set(p.getName() + ".ALIVE", false);
        try {
            ConcurrentGames.save(ConcurrentGamesFile);
        } catch (IOException exxx) {
            Bukkit.getServer().getConsoleSender().sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }

        boolean CnclMsg = false;

        ArrayList<String> AliveUsers = (ArrayList<String>) ConcurrentGames.getStringList("ALIVE");
        for (String alive : (ArrayList<String>) ConcurrentGames.getStringList("ALIVE")){
            if (AliveUsers.size() == 1){
                CnclMsg = true;
                for (Player online : Bukkit.getOnlinePlayers()){
                    if (!(online.getName().equals(p.getKiller().getName()))){
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o" + p.getKiller() + " &c&okilled &e&o" + p.getName()));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "              &9&lThe Winner is &6&o" + alive));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                    } else {
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou killed &e&o" + p.getName() + "&6&l +&e&o25 &c&oStars"));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "              &9&lYou won!"));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                    }
                }
            }

        }

        if (!CnclMsg){
            for (Player online : Bukkit.getOnlinePlayers()){
                if (!(online.getName().equals(p.getKiller().getName()))){
                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o" + p.getKiller() + " &c&okilled &e&o" + p.getName()));
                } else {
                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou killed &e&o" + p.getName() + "&6&l +&e&o25 &c&oStars"));
                }
            }
            e.setDeathMessage("");
        } else {
            e.setDeathMessage("");
        }

    }
}