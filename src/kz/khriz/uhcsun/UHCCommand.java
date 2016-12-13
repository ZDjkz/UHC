package kz.khriz.uhcsun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class UHCCommand implements CommandExecutor {

    UHC UHC;
    public UHCCommand(UHC instance) {
        UHC = instance;
    }

    @Override
    public boolean onCommand(CommandSender SENDER, Command CMD, String LABEL, String[] args) {
        if (CMD.getName().equalsIgnoreCase("UHC")) {
            if (!(SENDER instanceof Player)) return true;

            Player p = (Player) SENDER;
            if (args.length == 0){
                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&c&oThanks for Testing."));
            }

            if (args.length == 1){
                if (args[0].equalsIgnoreCase("Start")){
                    if (!UHC.Game.containsKey("NAME")){
                        if (UHC.Config.getBoolean("Lobby.Active")){
                            int PlayerCount = Bukkit.getOnlinePlayers().size();
                            if (PlayerCount >= 1){
                                UHC.Config.set("GAMES", UHC.Config.getInt("GAMES") + 1);
                                UHC.SaveConfig();
                                UHC.setGame();

                                Object id = UHC.getGame("GAME ID");
                                Object name = UHC.getGame("NAME");
                                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&e&oStarted new Game Chache."));
                                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oGame ID > " + id));
                                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oGame NAME > " + name));

                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(kz.khriz.uhcsun.UHC.getPlugin(UHC.class), new Runnable() {

                                    @Override
                                    public void run() {
                                        UHC.Game.put("PREGAME", "TRUE");
                                    }
                                }, (long) 1.5 * 20);

                            } else {
                                p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&c&oSorry there's not enough players online to Start a Game"));
                            }
                        } else {
                            p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&9&oPlease set the Waiting Lobby.."));
                        }
                    } else {
                        p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&c&oCan't continue, there's a game running."));

                    }
                }
                if (args[0].equalsIgnoreCase("Get")){

                    if (!UHC.Game.containsKey("NAME")){
                        p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&c&oCan't continue, there isn't a game running."));
                    } else {
                        Object id = UHC.getGame("GAME ID");
                        Object name = UHC.getGame("NAME");

                        p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oGame ID > " + id));
                        p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&6&oGame NAME > " + name));
                    }
                }
                if (args[0].equalsIgnoreCase("Clear")){
                    if (!UHC.Game.containsKey("NAME")){
                        p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&c&oCan't continue, there isn't a game running."));
                    } else {
                        p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&c&oGame Chache Cleared."));
                        UHC.clearGame();
                    }
                }
            }

            if (args.length == 2){
                if (args[0].equalsIgnoreCase("Debug")){
                    if (args[1].equalsIgnoreCase("rl")){
                        Location Spawn = UHC.rl();
                        p.teleport(Spawn);
                    }
                    if (args[1].equalsIgnoreCase("sb")){
                        UHC.SCOREBOARD.startUHCGameBoard(p);
                    }
                }
                if (args[0].equalsIgnoreCase("Lobby")){
                    if (args[1].equalsIgnoreCase("Set")){
                        p.sendMessage(UHC.PREFIX + ChatColor.translateAlternateColorCodes('&', "&9&oSet UHC Lobby to Your Location"));

                        double X = p.getLocation().getX();
                        double Y = p.getLocation().getY();
                        double Z = p.getLocation().getZ();

                        String W = p.getLocation().getWorld().getName();

                        float PI = p.getLocation().getPitch();
                        float YA = p.getLocation().getYaw();

                        UHC.Config.set("Lobby.Active", true);
                        UHC.Config.set("Lobby.X", X);
                        UHC.Config.set("Lobby.Y", Y);
                        UHC.Config.set("Lobby.Z", Z);
                        UHC.Config.set("Lobby.W", W);
                        UHC.Config.set("Lobby.PI", PI);
                        UHC.Config.set("Lobby.YA", YA);
                        UHC.SaveConfig();
                    }
                    if (args[1].equalsIgnoreCase("Clear")){
                        UHC.Config.set("Lobby", null);
                        UHC.SaveConfig();
                    }
                }
            }
        }
        return true;
    }

}