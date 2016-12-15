package kz.khriz.uhcsun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class UHCScoreboard implements Listener {

    UHC UHC;
    public UHCScoreboard(UHC instance) {
        UHC = instance;
    }

    @EventHandler
    public void startUHCGameBoard(Player p, boolean idDebug){
        Scoreboard Board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = Board.registerNewObjective("UHC", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Object GameID = UHC.Game.get("GAME ID");
        if (idDebug){
            GameID = UHC.UTIL.createNewID();
        } else {
            if (GameID == null) GameID = "DEBUG";
        }
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&cUHC &6Sun &f- &e" + GameID));

        Score name = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&a&l" + p.getName()));
        name.setScore(0);

        Team emptyLine = Board.registerNewTeam("eLineOne");
        emptyLine.addEntry(ChatColor.RED.toString());
        obj.getScore(ChatColor.RED.toString()).setScore(1);

        Team kills = Board.registerNewTeam("kills");
        kills.addEntry(ChatColor.GREEN.toString());
        kills.setPrefix(ChatColor.translateAlternateColorCodes('&', "&6&oKills &7> "));
        kills.setSuffix(ChatColor.RED + "0");
        obj.getScore(ChatColor.GREEN.toString()).setScore(2);

        Team stars = Board.registerNewTeam("stars");
        stars.addEntry(ChatColor.LIGHT_PURPLE.toString());
        stars.setPrefix(ChatColor.translateAlternateColorCodes('&', "&6&oStars &7> "));
        stars.setSuffix(ChatColor.RED + "0");
        obj.getScore(ChatColor.LIGHT_PURPLE.toString()).setScore(3);

        Team emptyLine2 = Board.registerNewTeam("eLineTwo");
        emptyLine2.addEntry(ChatColor.BLUE.toString());
        obj.getScore(ChatColor.BLUE.toString()).setScore(4);

        Team timeleft = Board.registerNewTeam("TimeLeft");
        timeleft.addEntry(ChatColor.GOLD.toString());
        timeleft.setPrefix(ChatColor.translateAlternateColorCodes('&', "&6&lPVP &8> "));
        timeleft.setSuffix(ChatColor.RED + "" + UHC.Mins + ":" + UHC.Seconds);
        obj.getScore(ChatColor.GOLD.toString()).setScore(5);

        Team date = Board.registerNewTeam("Date");
        date.addEntry(ChatColor.YELLOW.toString());
        date.setPrefix(ChatColor.DARK_GRAY + UHC.UTIL.getDay());
        date.setSuffix(ChatColor.RED + "");
        obj.getScore(ChatColor.YELLOW.toString()).setScore(6);

        p.setScoreboard(Board);

        new BukkitRunnable() {

            int TLeftM = UHC.Mins;
            int TLeftS = UHC.Seconds;

            @Override
            public  void run(){
                if (TLeftS >= 1){
                    if (TLeftM == 0 && TLeftS == 1){
                        Board.getTeam("TimeLeft").setSuffix(ChatColor.RED + "NOW");
                    } else {
                        TLeftS = TLeftS - 1;
                        String PatchS;
                        if (TLeftS <= 9) {
                            PatchS = 0 + "" + TLeftS;
                        } else {
                            PatchS = TLeftS + "";
                        }
                        String asTime = TLeftM + ":" + PatchS;
                        Board.getTeam("TimeLeft").setSuffix(ChatColor.RED + asTime);
                    }
                } else {
                    if (TLeftM == 0 && TLeftS == 1){
                        Board.getTeam("TimeLeft").setSuffix(ChatColor.RED + "NOW");
                    } else {
                        TLeftM = TLeftM - 1;
                        TLeftS = 59;
                        String asTime = TLeftM + ":" + TLeftS;
                        Board.getTeam("TimeLeft").setSuffix(ChatColor.RED + asTime);
                    }
                }
            }

        }.runTaskTimer(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), 20 + 20*10, 20);

        new BukkitRunnable() {

            Object Kills;
            Object Stars;

            @Override
            public  void run(){
                if (UHC.PlayerData.containsKey(p.getName() + ":Kills")){
                    Kills = UHC.PlayerData.get(p.getName() + ":Kills");
                }
                if (UHC.PlayerData.containsKey(p.getName() + ":Stars")){
                    Stars = UHC.PlayerData.get(p.getName() + ":Stars");
                }

                if (Kills == null){
                    Kills = 0;
                }
                if (Stars == null){
                    Stars = 0;
                }

                kills.setSuffix(ChatColor.RED + Kills.toString());
                stars.setSuffix(ChatColor.RED + Stars.toString());
            }

        }.runTaskTimer(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), 20 + 20*10 + (UHC.Seconds * 20) + ((60 * UHC.Mins) * 20) - 20, 20);

    }

}
