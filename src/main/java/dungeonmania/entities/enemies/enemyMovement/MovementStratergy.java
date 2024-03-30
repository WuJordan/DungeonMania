package dungeonmania.entities.enemies.enemyMovement;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;

public interface MovementStratergy {
    Position executeMovment(Game game, GameMap map, Enemy entity);
}
