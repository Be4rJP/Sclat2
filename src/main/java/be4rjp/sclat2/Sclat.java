package be4rjp.sclat2;

import be4rjp.sclat2.listener.InkHitBlockListener;
import be4rjp.sclat2.listener.PlayerItemClickListener;
import be4rjp.sclat2.listener.PlayerJoinQuitListener;
import be4rjp.sclat2.weapon.WeaponManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Sclat extends JavaPlugin {
    
    private static Sclat plugin;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
    
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);
        pluginManager.registerEvents(new InkHitBlockListener(), this);
        pluginManager.registerEvents(new PlayerItemClickListener(), this);
    
        WeaponManager.loadAllWeapon();
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    
    public static Sclat getPlugin(){
        return plugin;
    }
}
