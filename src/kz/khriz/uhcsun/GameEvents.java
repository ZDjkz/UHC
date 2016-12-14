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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameEvents implements Listener {

    UHC UHC;
    public GameEvents(UHC instance) {
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
        if (UHC.Game.get("STARTED") == "FINISHED"){
            return;
        }
        if (UHC.Game.get("PREGAME") == "TRUE"){
            return;
        }
        final File ConcurrentGamesFile = new File("plugins/UHC/Games/", UHC.Game.get("GAME ID") + ".yml");
        final FileConfiguration ConcurrentGames = YamlConfiguration.loadConfiguration(ConcurrentGamesFile);

        ArrayList<String> UsersAlive = (ArrayList<String>) ConcurrentGames.getStringList("ALIVE");
        Player p = (Player) e.getEntity();

        UsersAlive.remove(p.getName());
        ConcurrentGames.set("ALIVE", UsersAlive);
        try {
            ConcurrentGames.save(ConcurrentGamesFile);
        } catch (IOException ex) {
            Bukkit.getServer().getConsoleSender().sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }

        ConcurrentGames.set(p.getName() + ".ALIVE", false);
        try {
            ConcurrentGames.save(ConcurrentGamesFile);
        } catch (IOException exx) {
            Bukkit.getServer().getConsoleSender().sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&c&lWe're having Major Errors with UHC, screen shot the Error"
                    + " and send it to Khriz."));
        }

        boolean CnclMsg = false;

        ArrayList<String> AliveUsers = (ArrayList<String>) ConcurrentGames.getStringList("ALIVE");
        for (String alive : (ArrayList<String>) ConcurrentGames.getStringList("ALIVE")){
            if (AliveUsers.size() == 1){
                CnclMsg = true;
                UHC.Game.put("STARTED", "FINISHED");
                for (Player online : Bukkit.getOnlinePlayers()){
                    if (!(online.getName().equals(p.getKiller().getName()))){
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o" + p.getKiller().getName() + " &c&okilled &e&o" + p.getName()));
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
                if (online.getName() == p.getKiller().getName()){
                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou killed &e&o" + p.getName() + "&6&l +&e&o25 &c&oStars"));
                } else {
                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o" + p.getKiller() + " &c&okilled &e&o" + p.getName()));
                }
            }
            e.setDeathMessage("");
        } else {
            e.setDeathMessage("");
        }

        p.spigot().respawn();
    }

    @EventHandler
    public void DamageEvent(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;
        if (UHC.Game.get("PVP") == "DISABLED") return;
        if (UHC.Game.get("STARTED") == "FINISHED"){
            e.setCancelled(true);
            return;
        }
        if (UHC.Game.get("PREGAME") == "TRUE"){
            e.setCancelled(true);
            return;
        }
        if (UHC.Game.containsKey("NAME")){
            Player p = (Player) e.getEntity();
            Player a = (Player) e.getDamager();
            Double amount = e.getDamage();
            p.sendMessage(amount.toString());
            Double current = (double) 0;
            if (UHC.DamageMap.containsKey(a.getName() + " " + p.getName())){
                current = UHC.DamageMap.get(a.getName() + " " + p.getName());
            }
            p.sendMessage(current.toString());
            UHC.DamageMap.put(a.getName() + " " + p.getName(), amount + current);
            Double currentT = (double) 0;
            if (UHC.DamageMap.containsKey(a.getName() + " " + p.getName())){
                currentT = UHC.DamageTook.get(p.getName());
            }
            p.sendMessage(currentT.toString());
            UHC.DamageTook.put(p.getName(), amount + currentT);
        }
    }
}
