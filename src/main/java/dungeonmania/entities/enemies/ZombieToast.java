package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.enemyMovement.InvincibilityMovement;
import dungeonmania.entities.enemies.enemyMovement.ZombieToastDefaultMovement;
import dungeonmania.map.GameMap;
// import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    // private Random randGen = new Random(); moved into default zombietoastmovement

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            setMovementStratergy(new InvincibilityMovement());
        } else {
            setMovementStratergy(new ZombieToastDefaultMovement());
        }
        nextPos = executeStratergy(game, map, this);
        game.getMap().moveTo(this, nextPos);

    }

}
