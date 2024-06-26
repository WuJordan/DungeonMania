package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class BaseState extends PlayerState {
    public BaseState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics addBuffToPlayer(BattleStatistics playerStats) {
        return playerStats;
    }
}
