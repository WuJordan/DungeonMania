package dungeonmania.mvp.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.exceptions.*;
import dungeonmania.mvp.TestUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceptreTest {
    @Test
    @Tag("18-1")
    @DisplayName("Test InvalidActionException is raised when the player "
            + "does not have sufficient items to build sceptre")
    public void buildInvalidActionException() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BuildablesTest_BuildInvalidArgumentException", "c_BuildablesTest_BuildInvalidArgumentException");
        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));
    }

    @Test
    @Tag("18-2")
    @DisplayName("Test building a sceptre with treasure")
    public void buildSceptreWithTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_build", "c_BuildablesTest_BuildBow");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // Pick up Wood
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up treasure
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Pick up sun stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build septre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Check correct materials are consumed
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("18-3")
    @DisplayName("Test building a sceptre with Key")
    public void buildSceptreWithKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_build", "c_BuildablesTest_BuildBow");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());

        res = dmc.tick(Direction.RIGHT);

        // Pick up Wood
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up key
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Pick up sun stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build septre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Check correct materials are consumed
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("18-4")
    @DisplayName("Test building a sceptre with sun stone substitue")
    public void buildSceptreWithSunStoneSubstitute() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_build", "c_BuildablesTest_BuildBow");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Pick up Wood
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up sun stone 1
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up sun stone 2
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // Build septre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Check correct materials are consumed. Sun Stone substituting key or treasure is not consumed.
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("18-5")
    @DisplayName("Test building a sceptre with arrows")
    public void buildSceptreWithArrows() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_build", "c_BuildablesTest_BuildBow");

        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Pick up 2 Arrows
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());

        // Pick up treasure
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Pick up sun stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build septre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Check correct materials are consumed. Sun Stone substituting key or treasure is not consumed.
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("18-6")
    @DisplayName("Test can mind control from range")
    public void testSceptreRange() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_mindControl", "c_sceptreTest_mindControl");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // Get materials to build sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // No Sceptre, bribe fails from range
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));

        // Build sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Mind control works from range
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
    }

    @Test
    @Tag("18-6")
    @DisplayName("Test mind controls before bribe")
    public void testSceptreAndBribe() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_mindControl", "c_sceptreTest_mindControl");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        //Get Treasure for bribe
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        // Get materials to build sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        //Get into bribe range
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // Build sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Check Mind control works
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // Check treasure isn't used and thus merc is mind controlled, not bribed:
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("18-7")
    @DisplayName("Test mind controlling lasts temporarily")
    public void testSceptreControlTemp() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_mindControl_battle", "c_sceptreTest_mindControl");
        // Mind Control lasts 5 ticks

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // Build sceptre
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Start Mind control, check allied
        // Tick 1
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // Overlap Mercenary, Shouldnt create battle
        // Tick 2
        res = dmc.tick(Direction.DOWN);
        // Tick 3
        res = dmc.tick(Direction.DOWN);
        assertEquals(0, res.getBattles().size());

        // move back and forth until mind control runs out
        // Tick 4
        res = dmc.tick(Direction.DOWN);
        // Tick 5
        res = dmc.tick(Direction.UP);

        assertEquals(0, res.getBattles().size());

        // Overlap to battle mercenary
        // Tick 6 (Mind Control has worn off)
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, res.getBattles().size());

    }
}
