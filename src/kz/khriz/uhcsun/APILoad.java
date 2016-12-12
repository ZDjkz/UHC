package kz.khriz.uhcsun;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;

public class APILoad {

    UHC UHC;
    public APILoad(UHC instance) {
        UHC = instance;
    }

    public Permission PERM;

    public void loadAPI(){
        org.bukkit.plugin.PluginManager manager = Bukkit.getServer().getPluginManager();

        final Plugin vault = manager.getPlugin("Vault");
        if(vault != null && !vault.isEnabled()) {
            UHC.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oSorry, we couldn't locate &6&l'&f&oVault&6&l' &c&oPlease be sure you have &6&l'&f&oVault&6&l' &c&oInstalled."));
            manager.disablePlugin(UHC);
            return;
        }
    }

    public boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = UHC.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            PERM = permissionProvider.getProvider();
        }
        return (PERM != null);
    }

}
