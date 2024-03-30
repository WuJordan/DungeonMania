package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Entity {
    public static final int FLOOR_LAYER = 0;
    public static final int ITEM_LAYER = 1;
    public static final int DOOR_LAYER = 2;
    public static final int CHARACTER_LAYER = 3;

    private Position position;
    private Position previousPosition;
    private Position previousDistinctPosition;
    private Direction facing;
    private String entityId;

    private int stuckTicks = 0;
    private boolean amStuck = false;

    public Entity(Position position) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
        this.facing = null;
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }

    public void onOverlap(GameMap map, Entity entity) {
    };

    public void onMovedAway(GameMap map, Entity entity) {
    };

    public void onDestroy(GameMap gameMap) {
    };

    public Position getPosition() {
        return position;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    public String getId() {
        return entityId;
    }

    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public List<Position> getCardinallyAdjacentPositions() {
        int x = position.getX();
        int y = position.getY();
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x, y - 1));
        adjacentPositions.add(new Position(x + 1, y));
        adjacentPositions.add(new Position(x, y + 1));
        adjacentPositions.add(new Position(x - 1, y));
        return adjacentPositions;
    }

    public boolean stuckInSwamp(GameMap map) {
        if (stuckTicks > 0) {
            this.stuckTicks = this.stuckTicks - 1;
            return true;
        }
        if (!amStuck) {
            if (map.isSwampAtPosition(position)) {
                this.stuckTicks = map.getSwampAtPosition(position).getMovementFactor() - 1;
                if (this.stuckTicks >= 0) {
                    this.amStuck = true;
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            amStuck = false;
            this.stuckTicks = 0;
        }
        return false;
    }

}
