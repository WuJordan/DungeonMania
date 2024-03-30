package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class MidnightArmour extends Buildable implements BuildingItemInterface {
    public static final int DEFAULT_ATTACK = 1;
    public static final int DEFAULT_DEFENCE = 1;

    private double attack;
    private double defence;

    public MidnightArmour() {
        super(null);
    }

    public MidnightArmour(double attack, double defence) {
        super(null);
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public void use(Game game) {
    }

    @Override
    public boolean canBuild(List<InventoryItem> items) {
        int swords = count(Sword.class, items);
        int sunStoneCount = count(SunStone.class, items);

        if (swords >= 1 && sunStoneCount >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public void removeMats(Inventory inventory) {
        List<SunStone> sunStones = inventory.getEntities(SunStone.class);
        List<Sword> swords = inventory.getEntities(Sword.class);

        // Remove mandatory sunStone
        inventory.remove(sunStones.get(0));
        inventory.remove(swords.get(0));

    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, attack, defence, 1, 1));
    }

    @Override
    public int getDurability() {
        // Durability of Midnight Armour is unlimited.
        return Integer.MAX_VALUE;
    }

    public boolean canBuildArmour(List<InventoryItem> items, Game game) {
        return (canBuild(items) && game.getNumZombies() == 0);
    }
}
