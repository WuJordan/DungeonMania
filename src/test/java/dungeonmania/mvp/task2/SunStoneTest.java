package dungeonmania.mvp.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
// import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

// import java.util.ArrayList;
// import java.util.List;

public class SunStoneTest {
    @Test
    @Tag("17-1")
    @DisplayName("Test player can pick up sun stone and add to inventory")
    public void pickUpSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_basic", "c_DoorsKeysTest_pickUpKey");

        assertEquals(1, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sun stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());

    }

    @Test
    @Tag("17-2")
    @DisplayName("Test player can use a sun stone to open and walk through a door")
    public void useStoneToWalkThroughOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_basic", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up sun stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Walk up to the door (one square away)
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // Attempt to walk through the door
        res = dmc.tick(Direction.LEFT);

        // Check that we still have the sunstone, and the player has moved onto the door tile.
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @Tag("17-3")
    @DisplayName("Test if player has sun stone and required key in inventory, key is not consumed.")
    public void checkKeyNotConsumed() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_basic", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up key stone
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        res = dmc.tick(Direction.DOWN);

        // pick up sun stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Walk up to the door (one square away)
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // walk through door and check we still have sun stone and key
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @Tag("17-4")
    @DisplayName("Test treasure goal is met using sun stone")
    public void checkTreasureGoalwithSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_treasure", "c_sunStoneTest_treasure");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up normal treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up sun_stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("17-5")
    @DisplayName("Test player can't use sun stone to bribe mercs")
    public void checkCantBribeMercenaryWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_bribeMercenary", "c_mercenaryTest_allyMovement");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // pick up treasure and attempt to bribe.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertDoesNotThrow(() -> dmc.interact(mercId));
    }

    @Test
    @Tag("17-6")
    @DisplayName("Test sun stone substitues key OR treasure in shield build")
    public void checkSunStoneBuildSubstitute() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_buildShield", "c_mercenaryTest_allyMovement");

        // pick up building items (2 wood, 1 sun stone)
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // Check shield is buildable
        List<String> buildables = new ArrayList<>();
        buildables.add("shield");
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Check correct materials are used and sun stone isn't used
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("17-7")
    @DisplayName("Test sun stone not used first in treasure shield build")
    public void checkSunStoneNotUsedTreasureBuild() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_buildShield", "c_mercenaryTest_allyMovement");

        // pick up building items (2 wood, 1 sun stone, 1 treasure)
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Check shield is buildable
        List<String> buildables = new ArrayList<>();
        buildables.add("shield");
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Check correct materials are used
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("17-8")
    @DisplayName("Test sun stone can be used for both shield builds")
    public void checkSunStoneNotUsedDoubleBuild() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_buildDoubleShield", "c_mercenaryTest_allyMovement");

        // pick up building items (4 wood, 1 sun stone)
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(4, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // Check shield is buildable
        List<String> buildables = new ArrayList<>();
        buildables.add("shield");
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build shield 1
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Check correct materials are used
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Build shield 2
        assertTrue(res.getBuildables().containsAll(buildables));
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(2, TestUtils.getInventory(res, "shield").size());

        // Check correct materials are used
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
    }
}
