package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
// import dungeonmania.entities.EntityFactory;
// import dungeonmania.entities.Player;
// import dungeonmania.entities.Entity;
// import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Shield extends Buildable implements BuildingItemInterface {
    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    public Shield() {
        super(null);
        this.durability = getDurability();
        this.defence = getDefence();
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
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, defence, 1, 1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public double getDefence() {
        return defence;
    }

    @Override
    public boolean canBuild(List<InventoryItem> items) {
        if (count(Wood.class, items) >= 2 && (count(Treasure.class, items) >= 1 || count(Key.class, items) >= 1
                || count(SunStone.class, items) >= 1)) {
            return true;
        }
        return false;
    }

    @Override
    public void removeMats(Inventory inventory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);

        inventory.remove(wood.get(0));
        inventory.remove(wood.get(1));
        if (treasure.size() >= 1) {
            inventory.remove(treasure.get(0));
        } else if (keys.size() >= 1) {
            inventory.remove(keys.get(0));
        } // else there is a sunstone. don't consume sunstone.
    }
}
