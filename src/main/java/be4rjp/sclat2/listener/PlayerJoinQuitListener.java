package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.NawabariMatch;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.runnable.MatchWaitRunnable;
import be4rjp.sclat2.match.team.SclatColor;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.packet.PacketHandler;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.player.passive.Gear;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.SclatWeapon;
import be4rjp.sclat2.weapon.WeaponClass;
import be4rjp.sclat2.weapon.sub.SubWeapon;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {
    
    private static int i = 0;
    private static Match match;
    private static SclatTeam blue;
    private static SclatTeam orange;
    private static MatchWaitRunnable waitRunnable;
    
    static {
        match = new NawabariMatch(SclatMap.getSclatMap("shionome"));
        blue = new SclatTeam(match, SclatColor.BLUE);
        orange = new SclatTeam(match, SclatColor.ORANGE);
        match.initialize();
        waitRunnable = new MatchWaitRunnable(match);
        waitRunnable.start();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        sclatPlayer.updateBukkitPlayer();
        sclatPlayer.sendSkinRequest();
        sclatPlayer.setLang(Lang.ja_JP);

        if(i % 2 == 0){
            orange.join(sclatPlayer);
        }else{
            blue.join(sclatPlayer);
        }
        
        Lang lang = sclatPlayer.getLang();
        player.getInventory().clear();
        for(MainWeapon mainWeapon : MainWeapon.getMainWeaponList()){
            player.getInventory().addItem(mainWeapon.getItemStack(lang));
        }
    
        SubWeapon splash_bomb = (SubWeapon) SclatWeapon.getSclatWeapon("SPLASH_BOMB");
        player.getInventory().addItem(splash_bomb.getItemStack(sclatPlayer.getSclatTeam(), lang));
    
        WeaponClass wakaba = WeaponClass.getWeaponClass("wakaba");
        sclatPlayer.setWeaponClass(wakaba);
        
        sclatPlayer.getGearList().add(Gear.IKA_SPEED_UP);
        sclatPlayer.createPassiveInfluence();
        
        i++;
    }
    
    
    @EventHandler
    public void onjoin(PlayerJoinEvent event){
        //Inject packet handler
        Player player = event.getPlayer();
        
        PacketHandler packetHandler = new PacketHandler(player);
        
        try {
            ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
            pipeline.addBefore("packet_handler", Sclat.getPlugin().getName() + "PacketInjector:" + player.getName(), packetHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @EventHandler
    public void onleave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        
        try {
            Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
            
            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(Sclat.getPlugin().getName() + "PacketInjector:" + player.getName());
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
