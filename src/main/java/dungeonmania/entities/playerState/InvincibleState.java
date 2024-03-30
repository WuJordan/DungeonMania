package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class InvincibleState extends PlayerState {
    public InvincibleState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics addBuffToPlayer(BattleStatistics playerStats) {
        return BattleStatistics.applyBuff(playerStats, new BattleStatistics(0, 0, 0, 1, 1, true, true));
    }
}
