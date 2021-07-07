package be4rjp.sclat2.match.runnable;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.NawabariMatch;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatScoreboard;

import java.util.ArrayList;
import java.util.List;

public class NawabariMatchRunnable extends MatchRunnable{
    
    /**
     * 試合のスケジューラーを作成します。
     *
     * @param match     試合のインスタンス
     * @param timeLimit 試合の最大時間
     */
    public NawabariMatchRunnable(NawabariMatch match, int timeLimit) {
        super(match, timeLimit);
        
    }
    
    @Override
    public void run() {
    
        String min = String.format("%02d", timeLeft%60);
        //スコアボード
        SclatScoreboard scoreboard = match.getScoreboard();
        for(SclatPlayer sclatPlayer : match.getPlayers()) {
            Lang lang = sclatPlayer.getLang();
            List<String> lines = new ArrayList<>();
            lines.add("");
            lines.add("§a" + MessageManager.getText(lang, "match-map") + " » §r§l" + match.getSclatMap().getDisplayName(lang));
            lines.add(" ");
            lines.add("§a" + MessageManager.getText(lang, "match-mode") + " » §6§l " + match.getType().getDisplayName(lang));
            lines.add("   ");
            lines.add("§bTime left » §r§l" + timeLeft/60 + ":" + min);
            scoreboard.setSidebarLine(sclatPlayer, lines);
        }
        scoreboard.updateSidebar(match.getPlayers());
        
        if(timeLeft == 0){
            SclatTeam winTeam = match.getWinner();
            if(winTeam != null){
                //勝利処理
                this.cancel();
            }
        }
        timeLeft--;
    }
    
    
    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
    }
}
