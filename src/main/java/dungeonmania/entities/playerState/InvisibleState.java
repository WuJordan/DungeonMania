package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class InvisibleState extends PlayerState {
    public InvisibleState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics addBuffToPlayer(BattleStatistics playerStats) {
        return BattleStatistics.applyBuff(playerStats, new BattleStatistics(0, 0, 0, 1, 1, false, false));
    }
}
