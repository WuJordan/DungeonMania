package dungeonmania.entities.enemies.enemyMovement;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class InvisibilityMovement implements MovementStratergy {
    public Position executeMovment(Game game, GameMap map, Enemy entity) {
        Position nextPos;
        // Move random
        Random randGen = new Random();
        List<Position> pos = entity.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(entity, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = entity.getPosition();
            map.moveTo(entity, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            map.moveTo(entity, nextPos);
        }
        return nextPos;
    }
}
