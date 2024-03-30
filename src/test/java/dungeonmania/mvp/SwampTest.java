package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwampTest {
    @Test
    @Tag("18-1")
    @DisplayName("Testing player & swamp movement")
    public void playerMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampPlayerTest", "c_simpleSwampTest");
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());

        assertEquals(6, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(26, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
    }

    @Test
    @Tag("18-2")
    @DisplayName("Testing spider & swamp movement")
    public void spiderMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampSpiderTest", "c_simpleSwampTest");
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 2), TestUtils.getEntities(res, "spider").get(0).getPosition());

        assertEquals(6, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(26, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "spider").size());

        // Tick 1: Move onto the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());
        // Tick 2: Stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());
        // Tick 3: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());
        // Tick 4: Move off the swamp tile.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertNotEquals(new Position(9, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), TestUtils.getPlayer(res).get().getPosition());
        assertNotEquals(new Position(9, 1), TestUtils.getEntities(res, "spider").get(0).getPosition());
    }

    @Test
    @Tag("18-3")
    @DisplayName("Testing zombie & swamp movement")
    public void zombieMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampZombieTest", "c_simpleSwampTest");
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 2), TestUtils.getEntities(res, "zombie_toast").get(0).getPosition());

        assertEquals(6, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(26, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast").size());

        // Tick 1: Move onto the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        Position prevPosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(prevPosition, TestUtils.getEntities(res, "zombie_toast").get(0).getPosition());
        // Tick 2: Stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(prevPosition, TestUtils.getEntities(res, "zombie_toast").get(0).getPosition());
        // Tick 3: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(prevPosition, TestUtils.getEntities(res, "zombie_toast").get(0).getPosition());
        // Tick 4: Move off the swamp tile.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertNotEquals(prevPosition, TestUtils.getEntities(res, "zombie_toast").get(0).getPosition());
    }

    @Test
    @Tag("18-4")
    @DisplayName("Testing mercenary & swamp movement")
    public void mercenaryMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampMercTest", "c_simpleSwampTest");
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 2), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(6, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(26, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        // Tick 1: Move onto the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 2: Stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 3: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 4: Move off the swamp tile.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    @Tag("18-5")
    @DisplayName("Testing mercenary bribe & swamp movement")
    public void mercenaryBribeMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampMercBribeTest", "c_simpleSwampTest");
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 2), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(9, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(26, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        // Tick 1: Move onto the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 2: Stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 3: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 4: Move off the swamp tile.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // attempt bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(6, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(7, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(6, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(5, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Mercenary should not get stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(3, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(2, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    @Tag("18-6")
    @DisplayName("Testing mercenary & swamp, no bribe movement")
    public void mercenarySlowMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampMercBribeTest", "c_simpleSwampTest");
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 2), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(9, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(26, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        // Tick 1: Move onto the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 2: Stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 3: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 4: Move off the swamp tile.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(6, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(7, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(6, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Mercenary should get stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(5, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(5, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(5, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    @Tag("18-7")
    @DisplayName("Testing mercenary & swamp longer movement factor")
    public void mercenaryLongerMovementFactor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampMoreStuck", "c_simpleSwampTest");
        assertEquals(new Position(1, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 2), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(9, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(26, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        // Tick 1: Move onto the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 2: Stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 3: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 4: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 5: Still stuck on the swamp tile;
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(9, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
        // Tick 6: Move off the swamp tile.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), TestUtils.getPlayer(res).get().getPosition());
        assertEquals(new Position(8, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());
    }
}
