package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

import java.util.List;

import dungeonmania.entities.collectables.Arrow;
// import dungeonmania.entities.collectables.Key;
// import dungeonmania.entities.collectables.SunStone;
// import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Bow extends Buildable implements BuildingItemInterface {
    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
    }

    public Bow() {
        super(null);
        this.durability = getDurability();
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.removeItemFromPlayer(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 2, 1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public boolean canBuild(List<InventoryItem> items) {
        if (count(Wood.class, items) >= 1 && count(Arrow.class, items) >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public void removeMats(Inventory inventory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);

        inventory.remove(wood.get(0));
        inventory.remove(arrows.get(0));
        inventory.remove(arrows.get(1));
        inventory.remove(arrows.get(2));
    }
}
