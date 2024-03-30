package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.inventory.Inventory;

public interface BuildingItemInterface {
    public boolean canBuild(List<InventoryItem> items);

    public void removeMats(Inventory inventory);

}
