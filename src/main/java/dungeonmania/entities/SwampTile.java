package dungeonmania.entities;

import java.util.Random;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    public static final int DEFAULT_MOVEMENT = 1;
    private int movementFactor;

    public SwampTile(Position position, int numTicks) {
        super(position.asLayer(Entity.FLOOR_LAYER));
        this.movementFactor = numTicks;
    }

    public SwampTile(Position position) {
        super(position.asLayer(Entity.FLOOR_LAYER));
        setMovementFactor();
    }

    public void setMovementFactor(int movementFactor) {
        this.movementFactor = movementFactor;
    }

    public void setMovementFactor() {
        Random rand = new Random();
        this.movementFactor = rand.nextInt(5);
    }

    public int getMovementFactor() {
        return movementFactor;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}
