package dungeonmania.entities.enemies.enemyMovement;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MercenaryDefaultMovement implements MovementStratergy {
    public Position executeMovment(Game game, GameMap map, Enemy entity) {
        Player player = game.getPlayer();
        // Follow hostile
        return map.dijkstraPathFind(entity.getPosition(), player.getPosition(), entity);
    }
}
