package be4rjp.sclat2.weapon.main.runnable;

import be4rjp.sclat2.entity.ink.InkBullet;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.main.Shooter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShooterRunnable extends MainWeaponRunnable {
    
    private static final SclatSound NO_INK_SOUND = new SclatSound(Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, 0.8F, 1.2F);
    
    private final Shooter shooter;
    
    public ShooterRunnable(Shooter shooter, SclatPlayer sclatPlayer){
        super(shooter, sclatPlayer);
        this.shooter = shooter;
    }
    
    private int taskTick = 0;
    private int clickTick = 0;
    private int noClickTick = 0;
    
    @Override
    public void run() {
        boolean using = playerTick <= 6;
        
        if(taskTick % shooter.getShootTick() == 0){
            if(using) {
                Player player = sclatPlayer.getBukkitPlayer();
                if (player == null) return;
                //インク消費
                if(sclatPlayer.consumeInk(shooter.getNeedInk())) {
                    //射撃
                    Vector direction = player.getEyeLocation().getDirection();
                    Location origin = player.getEyeLocation();
    
                    InkBullet inkBullet = new InkBullet(sclatPlayer.getSclatTeam(), origin, shooter);
                    double range = shooter.getRecoil().getShootRandomRange(clickTick);
                    Vector randomVector = new Vector(Math.random() * range - range / 2, 0, Math.random() * range - range / 2);
                    inkBullet.shootInitialize(sclatPlayer, direction.multiply(shooter.getShootSpeed()).add(randomVector), shooter.getFallTick());
                    inkBullet.spawn();
                    sclatPlayer.playSound(shooter.getShootSound());
    
                    clickTick += shooter.getShootTick();
                }else{
                    sclatPlayer.playSound(NO_INK_SOUND);
                }
            }else{
                noClickTick += shooter.getShootTick();
                if(noClickTick >= shooter.getRecoil().getResetTick()) {
                    noClickTick = 0;
                    clickTick = 0;
                }
            }
        }
        
        
        playerTick++;
        taskTick++;
    }
}
