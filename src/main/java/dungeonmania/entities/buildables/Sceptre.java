package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.collectables.SunStone;

public class Sceptre extends Buildable implements BuildingItemInterface {
    public static final int DEFAULT_CONTROL_DURATION = 1;

    private int mindControlDuration;

    public Sceptre() {
        super(null);
    }

    public Sceptre(int duration) {
        super(null);
        this.mindControlDuration = duration;
    }

    @Override
    public void use(Game game) {
    }

    @Override
    public boolean canBuild(List<InventoryItem> items) {
        int woodCount = count(Wood.class, items);
        int arrowCount = count(Arrow.class, items);
        int keyCount = count(Key.class, items);
        int treasureCount = count(Treasure.class, items);
        int sunStoneCount = count(SunStone.class, items);
        if (woodCount >= 1 || arrowCount >= 2) {
            if (((keyCount >= 1 || treasureCount >= 1) && (sunStoneCount >= 1)) || (sunStoneCount >= 2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeMats(Inventory inventory) {

        List<SunStone> sunStones = inventory.getEntities(SunStone.class);
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);
        // Remove mandatory sunStone
        inventory.remove(sunStones.get(0));

        if (wood.size() >= 1) {
            inventory.remove(wood.get(0));
        } else {
            inventory.remove(arrows.get(0));
            inventory.remove(arrows.get(1));
        }

        // check if sunStone is being substituted for treasure / key. Always take treasure and keys first.
        if (treasure.size() >= 1) {
            inventory.remove(treasure.get(0));
        } else if (keys.size() >= 1) {
            inventory.remove(keys.get(0));
        } // else there is a sunstone. don't consume sunstone.
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1));
    }

    @Override
    public int getDurability() {
        // Durability of Sceptre is unlimited.
        return Integer.MAX_VALUE;
    }

    public int getMindControlDuration() {
        return mindControlDuration;
    }

}
