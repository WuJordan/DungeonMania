package dungeonmania.entities.enemies;

// import java.util.List;
// import java.util.Random;
// import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.enemyMovement.InvincibilityMovement;
import dungeonmania.entities.enemies.enemyMovement.InvisibilityMovement;
import dungeonmania.entities.enemies.enemyMovement.MercenaryDefaultMovement;
import dungeonmania.map.GameMap;
// import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean bribed = false;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;

    private int mindControlTime = 0;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
        bribed = true;
    }

    @Override
    public void interact(Player player, Game game) {
        // CanBeBribed Checking already been done. Can be interacted if you have a sceptre or enough money.
        allied = true;
        // When a merc can be bribed and mind controlled at the same time, don't need to worry ab what happens after.
        //Just make them allied.
        if (player.hasSceptre()) {
            //mind control for a specific duration of time.
            mindControlTime = player.getSceptre().getMindControlDuration();
        } else {
            bribe(player);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
                isAdjacentToPlayer = true;
        }
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = game.getPlayer();

        // if the merc is mind controlled.
        if (!bribed && allied) {
            updateMindControl();
        }

        if (allied) {
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                    : map.dijkstraPathFind(getPosition(), player.getPosition(), this);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                isAdjacentToPlayer = true;
        } else if (map.getPlayer().getEffectivePotion() instanceof InvisibilityPotion) {
            setMovementStratergy(new InvisibilityMovement());
            nextPos = executeStratergy(game, map, this);

        } else if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            setMovementStratergy(new InvincibilityMovement());
            nextPos = executeStratergy(game, map, this);
        } else {
            setMovementStratergy(new MercenaryDefaultMovement());
            nextPos = executeStratergy(game, map, this);
        }

        map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && (canBeBribed(player) || player.hasSceptre());
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    //Added getter and setter
    public boolean getIsAdjacentToPlayer() {
        return isAdjacentToPlayer;
    }

    public void setIsAdjacentToPlayer(boolean adjacent) {
        this.isAdjacentToPlayer = adjacent;
    }

    public void updateMindControl() {
        mindControlTime -= 1;
        if (mindControlTime <= 0) {
            allied = false;
        }
    }
}
