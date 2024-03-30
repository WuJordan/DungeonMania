package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals;
        switch (jsonGoal.getString("goal")) {
        case "AND":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new Goal(new AndGoal(createGoal(subgoals.getJSONObject(0), config),
                    createGoal(subgoals.getJSONObject(1), config)));
        case "OR":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new Goal(new OrGoal(createGoal(subgoals.getJSONObject(0), config),
                    createGoal(subgoals.getJSONObject(1), config)));
        case "exit":
            return new Goal(new ExitGoal());
        case "boulders":
            return new Goal(new BoulderGoal());
        case "treasure":
            int treasureGoal = config.optInt("treasure_goal", 1);
            return new Goal(new TreasureGoal(treasureGoal));
        case "enemies":
            int enemyGoal = config.optInt("enemy_goal", 1);
            return new Goal(new EnemyGoal(enemyGoal));
        default:
            return null;
        }
    }
}
