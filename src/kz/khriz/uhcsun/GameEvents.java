package kz.khriz.uhcsun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameEvents implements Listener {

    UHC UHC;
    public GameEvents(UHC instance) {
        UHC = instance;
    }

    @EventHandler
    public void foodDeprecator(FoodLevelChangeEvent e){
        if (e.isCancelled()) return;
        if (e.getEntityType() != EntityType.PLAYER) return;
        Player p = (Player) e.getEntity();
        if (p.isSprinting()){
            e.setFoodLevel(e.getFoodLevel() - 1);
        } else {
            e.setFoodLevel(e.getFoodLevel() + 1);
        }
    }

    @EventHandler
    public void Regen(EntityRegainHealthEvent e){
        if (!(e.getEntityType() == EntityType.PLAYER)) return;
        final File ConcurrentGamesFile = new File("plugins/UHC/Games/", UHC.Game.get("GAME ID") + ".yml");
        final FileConfiguration ConcurrentGames = YamlConfiguration.loadConfiguration(ConcurrentGamesFile);
        ArrayList<String> UsersAlive = (ArrayList<String>) ConcurrentGames.getStringList("ALIVE");
        Player p = (Player) e.getEntity();

        if (UsersAlive.contains(p.getName())){
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
        Player k = p.getKiller();

        Double DMGT = 0.0;
        Double dmDid = 0.0;
        Double PercentageDoneByYou = 0.0;
        Double Stars = 0.0;
        for (String dm : ConcurrentGames.getStringList("EXISTING")){
            DMGT = UHC.DamageTook.get(p.getName());
            dmDid = UHC.DamageMap.get(p.getName() + " " + dm);

            PercentageDoneByYou = DMGT / dmDid;

            Stars = 25 / PercentageDoneByYou;
        }

        // ADD METHOD TO MESSAGE ALL PLAYERS THAT WERE PART OF THE KILLING

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
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou killed &e&o" + p.getName() + "&6&l +&e&o" + Stars + " &c&oStars.   &6&l" + PercentageDoneByYou + "&7&o%"));
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
                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou killed &e&o" + p.getName() + "&6&l +&e&o" + Stars + " &c&oStars.   &6&l" + PercentageDoneByYou + "&7&o%"));
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
        if (!(e.getEntityType() == EntityType.PLAYER)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (UHC.Game.get("PVP") == "DISABLED") {
            e.setCancelled(true);
            return;
        }
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
            Double current = (double) 0;

            if (UHC.DamageMap.containsKey(p.getName() + " " + a.getName())){
                current = UHC.DamageMap.get(p.getName() + " " + a.getName());
            }

            UHC.DamageMap.put(p.getName() + " " + a.getName(), amount + current);

            Double currentT = (double) 0;

            if (UHC.DamageMap.containsKey(p.getName() + " " + a.getName())){
                currentT = UHC.DamageTook.get(p.getName());
            }

            UHC.DamageTook.put(p.getName(), amount + currentT);
        }
    }
}
