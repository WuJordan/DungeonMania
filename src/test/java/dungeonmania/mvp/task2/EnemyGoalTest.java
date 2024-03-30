package dungeonmania.mvp.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EnemyGoalTest {
    @Test
    @Tag("16-1")
    @DisplayName("Test achieveing a single enemy goal")
    public void singleEnemy() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_singleEnemy", "c_EnemyGoalTest_singleEnemy");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        // move player to right to battle the zombie. Battling occurs all in one tick.
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-2")
    @DisplayName("Test achieveing a multiple enemy goal")
    public void multipleEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_multipleEnemies", "c_EnemyGoalTest_multipleEnemies");

        // assert goal not met -> Goal: Kill 3
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to right to battle the zombie. Battling occurs all in one tick.
        res = dmc.tick(Direction.RIGHT); // kill 1
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.RIGHT); // kill 2
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.RIGHT); // kill 3

        //assert goal met
        assertEquals("", TestUtils.getGoals(res));

    }

    @Test
    @Tag("16-3")
    @DisplayName("Test achieveing a single enemy goal through bomb")
    public void bombEnemy() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_bombEnemy", "c_EnemyGoalTest_bombEnemy");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player down to move boulder onto switch. Switch is now active
        res = dmc.tick(Direction.DOWN);

        // Pick up bomb
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());

        // Place bomb cardinally adjacent to active switch. Bomb should blow up.
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

        //assert goal met
        assertEquals("", TestUtils.getGoals(res));

    }

    @Test
    @Tag("16-4")
    @DisplayName("Testing achieving Enemy Goal with enemy and spawners")
    public void enemyAndSpawner() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_enemyAndSpawner", "c_EnemyGoalTest_singleEnemy");
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // cardinally adjacent: true, has sword: false. Try to destory but with no weapon. Should not be destroyed.
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true. Not Cardinally Adjacent, try to destory. Should not be destroyed
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move down to fight the mercenary. Merc is killed.
        res = dmc.tick(Direction.DOWN);

        // assert goal stll not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        // assert goal stll not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // cardinally adjacent: true, has sword: true
        // Destory Spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        //assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-5")
    @DisplayName("Test achieveing no enemy goal")
    public void noEnemyGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_singleEnemy", "c_EnemyGoalTest_noEnemies");

        // assert goal not met -> Goal: Kill 0 enemies
        assertEquals("", TestUtils.getGoals(res));

        // move player to right to battle the zombie. Battling occurs all in one tick.
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-6")
    @DisplayName("Testing achieving with just spawner goal")
    public void onlySpawner() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_onlySpawner", "c_EnemyGoalTest_noEnemies");
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // cardinally adjacent: true, has sword: false. Try to destory but with no weapon. Should not be destroyed.
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true. Not Cardinally Adjacent, try to destory. Should not be destroyed
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // assert goal stll not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true
        // Destory Spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        //assert goal met
        assertEquals("", TestUtils.getGoals(res));

    }

    @Test
    @Tag("16-7")
    @DisplayName("Testing achieving Enemy Goal with enemy and multiple spawners")
    public void enemyAndMultipleSpawner() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_enemyAndMultipleSpawners", "c_EnemyGoalTest_singleEnemy");
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId1 = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawnerId2 = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // cardinally adjacent: true, has sword: false. Try to destory but with no weapon. Should not be destroyed.
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId1));
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true. Not Cardinally Adjacent, try to destory. Should not be destroyed
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId1));
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move down to fight the mercenary. Merc is killed.
        res = dmc.tick(Direction.DOWN);

        // assert goal stll not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        // assert goal stll not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // cardinally adjacent: true, has sword: true
        // Destory Spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId1));
        assertEquals(1, TestUtils.countType(res, "zombie_toast_spawner"));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // assert goal stll not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId2));

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-8")
    @DisplayName("Test achieveing enemy goal through bombing spawner and enemy")
    public void bombSpawner() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_bombSpawner", "c_EnemyGoalTest_bombEnemy");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player down to move boulder onto switch. Switch is now active
        res = dmc.tick(Direction.DOWN);

        // Pick up bomb
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());

        // Place bomb cardinally adjacent to active switch. Bomb should blow up.
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

        //assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-9")
    @DisplayName("Testing a map with all 4 goals")
    public void andAllComplex() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_andAll", "c_EnemyGoalTest_andAll");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));

        // pickup treasure
        res = dmc.tick(Direction.DOWN);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));

        // move to exit
        res = dmc.tick(Direction.DOWN);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-10")
    @DisplayName("Testing that the exit goal must be achieved last in EXIT and ENEMIS")
    public void exitAndEnemiesOrder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_ExitAndEnemies", "c_EnemyGoalTest_andAll");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player onto exit
        res = dmc.tick(Direction.RIGHT);

        // don't check state of exit goal in string
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to battle mercenary
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // assert treasure goal met, but goal string is not empty
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertNotEquals("", TestUtils.getGoals(res));

        // move player back onto exit
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-11")
    @DisplayName("Testing enemy goal OR exit goal")
    public void exitOrEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTest_enemyORexit", "c_EnemyGoalTest_andAll");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player battle mercenary
        res = dmc.tick(Direction.DOWN);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }
}
