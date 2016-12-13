package kz.khriz.uhcsun;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UHCScoreboard implements Listener {

    UHC UHC;
    public UHCScoreboard(UHC instance) {
        UHC = instance;
    }

    @EventHandler
    public void scoreBoardInit(PlayerJoinEvent e){

    }


}
