package dungeonmania.goals;

import dungeonmania.Game;

public class Goal {
    private GoalNode type;

    public Goal(GoalNode type) {
        this.type = type;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return type.achieved(game);
    }

    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return type.toString(game);
    }

}
