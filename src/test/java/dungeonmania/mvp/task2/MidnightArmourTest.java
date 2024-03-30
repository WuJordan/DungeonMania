package dungeonmania.mvp.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class MidnightArmourTest {
    @Test
    @Tag("19-1")
    @DisplayName("Test build armour without zombies")
    public void testCraftMidnightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_build", "c_midnightArmourTest_attributes");

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());
        buildables.add("midnight_armour");
        // Pick up sword and sun stone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Armour should now be added to game buildables list
        assertEquals(buildables, res.getBuildables());
        assertTrue(buildables.containsAll(res.getBuildables()));

        // Build armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Armour disappears from buildables list
        assertEquals(0, res.getBuildables().size());
    }

    @Test
    @Tag("19-2")
    @DisplayName("Test creating armour with zombie")
    public void testBuildArmourWithZombie() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombie", "c_midnightArmourTest_attributes");

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());

        // Pick up sword and sun stone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // buildable list should be empty since there are zombies.
        assertEquals(buildables, res.getBuildables());
        assertEquals(buildables.size(), res.getBuildables().size());

        // Fails to build armour
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @Tag("19-3")
    @DisplayName("Test creating armour with zombieSpawner no Spawns")
    public void testBuildArmourWithZombieSpawnerNoSpawns() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombieSpawner", "c_DoorsKeysTest_pickUpKey");

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());
        buildables.add("midnight_armour");

        // Pick up sword and sun stone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Armour should now be added to game buildables list
        assertEquals(buildables, res.getBuildables());
        assertTrue(buildables.containsAll(res.getBuildables()));

        // Build armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Armour disappears from buildables list
        assertEquals(0, res.getBuildables().size());
    }

    @Test
    @Tag("19-4")
    @DisplayName("Test creating armour with zombie spawned in")
    public void testBuildArmourWithZombieSpawned() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombie", "c_midnightArmourTest_spawnZombies");

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());

        // Pick up sword and sun stone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // buildable list should be empty since there are zombies spawned in.
        assertEquals(buildables, res.getBuildables());
        assertEquals(buildables.size(), res.getBuildables().size());

        // Fails to build armour
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @Tag("19-5")
    @DisplayName("Test the player gets the correct attack and defence buffs from config")
    public void testMidnightArmourBuffs() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        String config = "c_midnightArmourTest_attributes";
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_checkBuffs", config);

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());

        // Pick up sword and sun stone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        res = dmc.tick(Direction.RIGHT);
        List<BattleResponse> battles = res.getBattles();
        BattleResponse battle = battles.get(0);

        // Assert battle is occuring
        assertEquals(1, battles.size());

        // This is the attack without the armour attack buff
        double playerBaseAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("player_attack", config));
        double armourAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("midnight_armour_attack", config));

        RoundResponse firstRound = battle.getRounds().get(0);
        assertEquals((playerBaseAttack + armourAttack) / 5, -firstRound.getDeltaEnemyHealth(), 0.001);

        // Assumption: armour defence effect calculation to reduce damage makes enemyAttack =
        // enemyAttack - armour effect
        double enemyAttack = Integer.parseInt(TestUtils.getValueFromConfigFile("mercenary_attack", config));
        double armourDefence = Integer.parseInt(TestUtils.getValueFromConfigFile("midnight_armour_defence", config));
        double expectedDamage = (enemyAttack - armourDefence) / 10;
        // Delta health is negative so take negative here
        assertEquals(expectedDamage, -firstRound.getDeltaCharacterHealth(), 0.001);
    }

}
