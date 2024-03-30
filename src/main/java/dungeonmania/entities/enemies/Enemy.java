package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.entities.enemies.enemyMovement.MovementStratergy;

public abstract class Enemy extends Entity implements Battleable {
    private BattleStatistics battleStatistics;
    private MovementStratergy stratergy;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public double getBattleStatisticsHealth() {
        return battleStatistics.getHealth();
    }

    public void setBattleStatisticsHealth(double newHealth) {
        battleStatistics.setHealth(newHealth);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.gameBattle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    public abstract void move(Game game);

    // Added Stratergy:
    public void setMovementStratergy(MovementStratergy moveStrat) {
        stratergy = moveStrat;
    }

    public Position executeStratergy(Game game, GameMap map, Enemy entity) {
        return stratergy.executeMovment(game, map, entity);
    }
}
