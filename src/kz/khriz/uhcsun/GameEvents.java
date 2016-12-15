package kz.khriz.uhcsun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.bukkit.scheduler.BukkitRunnable;

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

        Double totalDamage = 0.0;
        Double damageDid = 0.0;
        Double percentByYou = 0.0;
        Double Stars = 25.0;

        int TrueStars = 0;
        for (String existingPlayer : ConcurrentGames.getStringList("EXISTING")){
            if (UHC.PlayerTAttackerDamage.containsKey(p.getName() + ":" + existingPlayer.toString())){
                totalDamage = UHC.TotalDamageTook.get(p.getName());
                damageDid = UHC.PlayerTAttackerDamage.get(p.getName() + ":" + existingPlayer.toString());
                Double totalToP = damageDid / 10.0;
                if (damageDid >= totalDamage){
                    percentByYou = 100.0;
                } else {
                    percentByYou = totalDamage * totalToP;
                }

                Stars = Stars % UHC.UTIL.round(percentByYou);

                TrueStars = UHC.UTIL.round(Stars);
                Player attacker = Bukkit.getPlayer(existingPlayer);
                if (attacker != null){
                    if (attacker.getName() != k.getName()){
                        int CurrentStars = 0;
                        if (UHC.PlayerData.containsKey(attacker.getName() + ":Stars")){
                            CurrentStars = Integer.parseInt( UHC.PlayerData.get(attacker.getName() + ":Stars") + "");
                        }
                        UHC.PlayerData.put(attacker.getName() + ":Stars", CurrentStars + TrueStars);
                        attacker.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou helped kill &e&o" + p.getName() + "&6&l +&e&o" + TrueStars + " &c&oStars.   &6&l" + UHC.UTIL.round(percentByYou) + "&7&o%"));
                    } else {
                        int CurrentStars = 0;
                        if (UHC.PlayerData.containsKey(attacker.getName() + ":Stars")){
                            CurrentStars = Integer.parseInt( UHC.PlayerData.get(attacker.getName() + ":Stars") + "");
                        }
                        UHC.PlayerData.put(attacker.getName() + ":Stars", CurrentStars + TrueStars);

                        int Kills = 0;
                        if (UHC.PlayerData.containsKey(attacker.getName() + ":Kills")){
                            Kills = Integer.parseInt( UHC.PlayerData.get(attacker.getName() + ":Kills") + "");
                        }
                        UHC.PlayerData.put(attacker.getName() + ":Kills", Kills + 1);
                    }
                } else {

                }
            }
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
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou killed &e&o" + p.getName() + "&6&l +&e&o" + TrueStars + " &c&oStars.   &6&l" + UHC.UTIL.round(percentByYou) + "&7&o%"));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "              &9&lYou won!"));
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                    }
                }
                new BukkitRunnable() {

                    @Override
                    public  void run(){
                        UHC.clearGame();
                    }

                }.runTaskLater(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), (1 * 60) * 20);
            }

        }

        if (!CnclMsg){
            for (Player online : Bukkit.getOnlinePlayers()){
                if (online.getName() == p.getKiller().getName()){
                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou killed &e&o" + p.getName() + "&6&l +&e&o" + TrueStars + " &c&oStars.   &6&l" + UHC.UTIL.round(percentByYou) + "&7&o%"));
                } else {
                    online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o" + p.getKiller().getName() + " &c&okilled &e&o" + p.getName()));
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
        if (UHC.Game.containsKey("GAME ID")){

            Player p = (Player) e.getEntity();
            Player a = (Player) e.getDamager();

            Double amountDealt = e.getDamage();
            Double currenDamageTotal = 0.0;

            if (UHC.PlayerTAttackerDamage.containsKey(p.getName() + ":" + a.getName())){
                currenDamageTotal = UHC.PlayerTAttackerDamage.get(p.getName() + ":" + a.getName());
            }
            UHC.PlayerTAttackerDamage.put(p.getName() + ":" + a.getName(), currenDamageTotal + amountDealt);

            Double beforeTotalTook = 0.0;
            if (UHC.TotalDamageTook.containsKey(p.getName())){
                beforeTotalTook = UHC.TotalDamageTook.get(p.getName());
            }
            UHC.TotalDamageTook.put(p.getName(), beforeTotalTook + amountDealt);

        }
    }
}
