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
    public void startUHCGameBoard(Player p){
        Scoreboard Board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = Board.registerNewObjective("UHC", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&cUHC &6Sun &f- &e" + UHC.Game.get("GAME ID")));

        Score name = obj.getScore(p.getName());
        name.setScore(0);



        Team timeleft = Board.registerNewTeam("TimeLeft");
        timeleft.addEntry(ChatColor.RED.toString());
        timeleft.setPrefix(ChatColor.GOLD + "PvP > ");
        timeleft.setSuffix(ChatColor.RED + "0:30");
        obj.getScore(ChatColor.RED.toString()).setScore(5);

        Team date = Board.registerNewTeam("Date");
        date.addEntry(ChatColor.GOLD.toString());
        date.setPrefix(ChatColor.DARK_GRAY + UHC.UTIL.getDay());
        date.setSuffix(ChatColor.RED + "0:30");
        obj.getScore(ChatColor.RED.toString()).setScore(6);

        p.setScoreboard(Board);

        new BukkitRunnable() {

            int TLeftS = 30;
            int TLeftM = 0;

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

    }

}
