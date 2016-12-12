package kz.khriz.uhcsun;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;

public class PreGameEvents implements Listener {

    UHC UHC;
    public PreGameEvents(UHC instance) {
        UHC = instance;
    }

    @EventHandler
    public void preGameMovement(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Location old = e.getFrom();
        if (UHC.Game.get("PREGAME") == "TRUE"){
            if (p.getFallDistance() > 0){

            } else {
                e.setCancelled(true);
                if (!(UHC.PlayerData.get(p.getName()) == "WAIT")){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oSorry, you can't move yet."));
                    UHC.PlayerData.put(p.getName(), "WAIT");
                    clearWait(p);
                    p.teleport(old);
                }
            }
        }
    }

    @EventHandler
    public void preGameJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (UHC.Game.get("PREGAME") == "TRUE"){
            p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&6&oSorry, currently in Pre-Game. Join back in 10 Seconds"));
        }
        if (UHC.Game.get("PREGAME") == "FALSE"){
            if (UHC.Game.get("STARTED") == "TRUE"){
                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&3&oYou joined in Spectator Mode."));
            }
        }

    }

    public void clearWait(Player p){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {
            @Override
            public void run() {
                UHC.PlayerData.remove(p.getName());
            }
        }, ((1)) * 20);
    }

}
