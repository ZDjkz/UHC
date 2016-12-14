package kz.khriz.uhcsun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyEvents implements Listener {

    UHC UHC;
    public LobbyEvents(UHC instance) {
        UHC = instance;
    }

    @EventHandler
    public void joinDatLog(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.setMaxHealth(20);
        p.setHealth(20);
        p.setFoodLevel(20);
        if (UHC.Game.containsKey("NAME")){
            if (UHC.Game.get("PREGAME") == "TRUE"){
                return;
            }
            if (UHC.Game.get("STARTED") == "TRUE"){
                return;
            }
        }

        if (UHC.Config.getBoolean("Lobby.Active")){
            double X = UHC.Config.getDouble("Lobby.X");
            double Y = UHC.Config.getDouble("Lobby.Y");
            double Z = UHC.Config.getDouble("Lobby.Z");

            String WC = UHC.Config.getString("Lobby.W");
            World W = Bukkit.getWorld(WC);

            float PI = (float) UHC.Config.getDouble("Lobby.PI");
            float YA = (float) UHC.Config.getDouble("Lobby.YA");

            Location SPAWN = new Location(W, X, Y, Z);
            SPAWN.setPitch(PI);
            SPAWN.setYaw(YA);
            p.teleport(SPAWN);

            p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&9&oSent to Waiting Lobby..."));
        } else {
            if (!p.isOp()){
                p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&6&oSorry, the Lobby is not Set."));
            } else {
                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&9&oPlease set the Waiting Lobby.."));
            }
        }
    }

    @EventHandler
    public void foodDeprecator(FoodLevelChangeEvent e){
        if (e.getEntityType() != EntityType.PLAYER) return;
        Player p = (Player) e.getEntity();
        if (UHC.Game.containsKey("NAME")){
            if (UHC.Game.get("STARTED") == "TRUE"){
                return;
            }
            if (UHC.Game.get("PREGAME") == "TRUE"){
                e.setCancelled(true);
            }
        }
        e.setCancelled(true);
    }

}
