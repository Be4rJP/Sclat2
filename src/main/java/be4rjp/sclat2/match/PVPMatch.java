package be4rjp.sclat2.match;

import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.team.SclatTeam;

public class PVPMatch extends Match{
    public PVPMatch(SclatMap sclatMap) {
        super(sclatMap);
    }
    
    @Override
    public MatchType getType() {
        return MatchType.PVP_2_TEAM;
    }
    
    @Override
    public boolean checkWin() {
        return false;
    }
    
    @Override
    public SclatTeam getWinner() {
        return null;
    }
}
