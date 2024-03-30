package dungeonmania.goals;

import dungeonmania.Game;

public class EnemyGoal implements GoalNode {
    private int target;

    //Goal is achieved after Destorying a certain number of enemies (or more) AND all spawners
    // Check how many spawners are still in the game.
    EnemyGoal(int target) {
        this.target = target;
    }

    public boolean achieved(Game game) {
        return (game.getEnemiesKilled() >= target && game.getNumSpawners() == 0);
    }

    public String toString(Game game) {
        return ":enemies";
    }
}
