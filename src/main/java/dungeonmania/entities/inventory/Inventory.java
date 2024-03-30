package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Sword;

import dungeonmania.Game;
import dungeonmania.entities.buildables.Sceptre;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables(Game game) {
        List<String> result = new ArrayList<>();
        Bow newBow = new Bow();
        Shield newShield = new Shield();
        if (newBow.canBuild(this.items)) {
            result.add("bow");
        }
        if (newShield.canBuild(this.items)) {
            result.add("shield");
        }
        if (new Sceptre().canBuild(this.items)) {
            result.add("sceptre");
        }
        if (new MidnightArmour().canBuildArmour(this.items, game)) {
            result.add("midnight_armour");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, String item, EntityFactory factory, Game game) {
        switch (item) {
        case "shield":
            Shield newShield = new Shield();
            if (newShield.canBuild(this.items)) {
                newShield.removeMats(this);
                return factory.buildShield();
            }
            break;
        case "bow":
            Bow newBow = new Bow();
            if (newBow.canBuild(this.items)) {
                newBow.removeMats(this);
                return factory.buildBow();
            }
            break;
        case "sceptre":
            Sceptre newSceptre = new Sceptre();
            if (newSceptre.canBuild(this.items)) {
                newSceptre.removeMats(this);
                return factory.buildSceptre();
            }
            break;
        case "midnight_armour":
            MidnightArmour newArmour = new MidnightArmour();
            if (newArmour.canBuildArmour(items, game)) {
                newArmour.removeMats(this);
                return factory.buildMidnightArmour();
            }
            break;
        default:
            break;
        }
        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public void useWeapon(Game game) {
        getWeapon().use(game);
    }
}
