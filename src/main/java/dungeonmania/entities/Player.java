package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.playerState.BaseState;
import dungeonmania.entities.playerState.InvincibleState;
import dungeonmania.entities.playerState.InvisibleState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Game;
import dungeonmania.entities.buildables.Sceptre;

public class Player extends Entity implements Battleable {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    private Queue<Potion> queue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;

    private int collectedTreasureCount = 0;
    private int enemiesKilled = 0;

    private PlayerState state;
    private Game game;

    public Player(Position position, double health, double attack) {
        super(position);
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
        inventory = new Inventory();
        state = new BaseState(this);
    }

    public int getCollectedTreasureCount() {
        return collectedTreasureCount;
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public BattleItem getWeapon() {
        return inventory.getWeapon();
    }

    public List<String> getBuildables() {
        return inventory.getBuildables(game);
    }

    public boolean build(String entity, EntityFactory factory) {
        InventoryItem item = inventory.checkBuildCriteria(this, entity, factory, game);
        if (item == null)
            return false;
        return inventory.add(item);
    }

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Enemy) {
            if (entity instanceof Mercenary) {
                if (((Mercenary) entity).isAllied())
                    return;
            }
            map.getGame().battle(this, (Enemy) entity);
        }
    }

    //Pick Up Function: -> Moving Pick up from Entity to Player
    public void onPickup(GameMap map, Entity item) {
        if (item instanceof InventoryItem) {
            if (item instanceof Bomb) {
                Bomb bomb = (Bomb) item;
                List<Switch> subs = bomb.getSubsList();
                if (bomb.getState() != Bomb.State.SPAWNED)
                    return;
                if (!pickUp(item))
                    return;
                subs.stream().forEach(s -> s.unsubscribe(bomb));
                map.destroyEntity(item);
                bomb.setState(Bomb.State.INVENTORY);
            } else if (!pickUp(item)) {
                return;
            }
            map.destroyEntity(item);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public BattleStatistics applyItemBuffs(BattleStatistics playerBuff) {
        for (BattleItem item : inventory.getEntities(BattleItem.class)) {
            if (item instanceof Potion)
                continue;
            playerBuff = item.applyBuff(playerBuff);
        }
        return playerBuff;
    }

    public List<BattleItem> getBattleItemEntities() {
        return inventory.getEntities(BattleItem.class);
    }

    public boolean pickUp(Entity item) {
        if (item instanceof Treasure || item instanceof SunStone)
            collectedTreasureCount++;
        if (item instanceof Key && inventory.count(Key.class) >= 1) {
            return false;
        }
        return inventory.add((InventoryItem) item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Key getFirstKey() {
        return inventory.getFirst(Key.class);
    }

    public void removeKeyFromInventory(Key key) {
        inventory.remove(key);
    }

    public Potion getEffectivePotion() {
        return inEffective;
    }

    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null)
            inventory.remove(item);
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    public void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffective = null;
            state = new BaseState(this);
            return;
        }
        inEffective = queue.remove();
        if (inEffective instanceof InvincibilityPotion) {
            state = new InvincibleState(this);
        } else {
            state = new InvisibleState(this);
        }
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public void changeState(PlayerState playerState) {
        state = playerState;
    }

    public void use(Potion potion, int tick) {
        inventory.remove(potion);
        queue.add(potion);
        if (inEffective == null) {
            triggerNext(tick);
        }
    }

    public void onTick(int tick) {
        if (inEffective == null || tick == nextTrigger) {
            triggerNext(tick);
        }
    }

    public void remove(InventoryItem item) {
        inventory.remove(item);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public double getBattleStatisticsHealth() {
        return battleStatistics.getHealth();
    }

    public void setBattleStatisticsHealth(double newHealth) {
        battleStatistics.setHealth(newHealth);
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return state.addBuffToPlayer(origin);
    }

    public void interactWeapon(Game game) {
        inventory.useWeapon(game);
    }

    //Methods for Task-2-a
    public int getNumEnemiesKilled() {
        return enemiesKilled;
    }

    public void setNumEnemiesKilled(int enemiesKilled) {
        this.enemiesKilled = enemiesKilled;
    }

    //Task-2-D
    public SunStone getSunStone() {
        return inventory.getFirst(SunStone.class);
    }

    public Sceptre getSceptre() {
        return inventory.getFirst(Sceptre.class);
    }

    public boolean hasSceptre() {
        return (getSceptre() != null);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
