package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
// import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem, BattleItem {
    public Buildable(Position position) {
        super(position);
    }

    public <T extends InventoryItem> int count(Class<T> itemType, List<InventoryItem> items) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }
}
