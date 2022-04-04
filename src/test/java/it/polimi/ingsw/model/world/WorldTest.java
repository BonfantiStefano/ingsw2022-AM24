package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.pawn.Tower;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.influence.AdditionalInfluence;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.world.influence.StandardInfluence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class WorldTest tests World class.
 *
 * @author Bonfanti Stefano
 * @see World
 */
class WorldTest {
    World world;

    /** Method setup creates a world that will be used by every test.*/
    @BeforeEach
    void setup() {
        ArrayList<ColorS> students = new ArrayList<>();
        Island mnLocation = new Island();
        ColorS student1 = ColorS.GREEN;
        students.add(student1);
        ColorS student2 = ColorS.RED;
        students.add(student2);
        ColorS student3 = ColorS.GREEN;
        students.add(student3);
        ColorS student4 = ColorS.YELLOW;
        students.add(student4);
        ColorS student5 = ColorS.YELLOW;
        students.add(student5);
        ColorS student6 = ColorS.PINK;
        students.add(student6);
        ColorS student7 = ColorS.BLUE;
        students.add(student7);
        ColorS student8 = ColorS.PINK;
        students.add(student8);
        ColorS student9 = ColorS.RED;
        students.add(student9);
        ColorS student10 = ColorS.BLUE;
        students.add(student10);
        world = new World(students);
    }

    /** Method getInfluenceIsland tests the calculation of the Influence for every single Player on a single Island.*/
    @Test
    @DisplayName("World's join method test")
    void getInfluenceIsland() {
        int counter;
        world.setStrategy(new StandardInfluence());
        Island island = new Island();
        ArrayList<Player> players = new ArrayList<>();
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9,6);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9,6);
        players.add(lisa);
        players.add(bob);
        players.add(alice);
        Map<ColorS, Player> profs = new HashMap<>();
        profs.put(ColorS.GREEN, lisa);
        profs.put(ColorS.BLUE, bob);
        profs.put(ColorS.YELLOW, alice);
        for(counter = 0; counter < 3; counter++) {
            island.add(ColorS.GREEN);
        }
        for(counter = 0; counter < 4; counter++) {
            island.add(ColorS.YELLOW);
        }
        island.add(ColorS.BLUE);
        island.add(ColorS.BLUE);
        island.add(ColorS.RED);
        island.add(ColorT.BLACK);
        Map<Player, Integer> mapInfluence = world.getInfluenceIsland(island, profs, players);
        assertEquals(4, mapInfluence.get(lisa));
        assertEquals(4, mapInfluence.get(alice));
        assertEquals(2, mapInfluence.get(bob));
    }

    /** Method join tests the merging of two islands.*/
    @Test
    @DisplayName("World's join method test")
    void join() {
        Island i1 = world.getIslandByIndex(1);
        Island i2 = world.getIslandByIndex(2);
        for(int counter = 0; counter < 3; counter++) {
            ColorS s1 = ColorS.GREEN;
            i1.add(s1);
            ColorS s2 = ColorS.RED;
            i1.add(s2);
            ColorS s3 = ColorS.GREEN;
            i2.add(s3);
        }
        ColorS s4 = ColorS.YELLOW;
        i1.add(s4);
        ColorS s5 = ColorS.RED;
        i2.add(s5);
        ColorT t1 = ColorT.WHITE;
        i1.add(t1);
        ColorT t2 = ColorT.WHITE;
        i2.add(t2);
        i2.setNumNoEntry(1);
        i1.setNumNoEntry(1);
        Island i3 = world.join(i1, i2);
        assertEquals(i3, world.getIslandByIndex(1));
        for(ColorS c : ColorS.values()) {
            assertEquals(i1.getNumStudentByColor(c) + i2.getNumStudentByColor(c), i3.getNumStudentByColor(c));
        }
        assertEquals(2, i3.getNumSubIsland());
        assertEquals(2, i3.getNumNoEntry());
        assertEquals(Optional.of(ColorT.WHITE), i3.getTowerColor());
        assertEquals(11, world.getSize());
    }

    /** Method checkJoin tests if two Island have to be merged, in this case the Islands are merged and iterate.*/
    @Test
    @DisplayName("World's checkJoin method test")
    void checkJoin() {
        //merge the first and the last Island.
        Island island1 = world.getIslandByIndex(0);
        Island island2 = world.getIslandByIndex(11);
        island1.add(ColorT.WHITE);
        island2.add(ColorT.WHITE);
        world.checkJoin(island1);
        assertEquals(11, world.getSize());
        //nothing needs to be merged.
        Island island3 = world.getIslandByIndex(5);
        island3.add(ColorT.WHITE);
        world.checkJoin(island3);
        assertEquals(11, world.getSize());
        //merge of consecutive island.
        Island island4 = world.getIslandByIndex(6);
        island4.add(ColorT.WHITE);
        world.checkJoin(island4);
        assertEquals(10, world.getSize());
        //consecutive island with different Tower color, nothing to do.
        Island island5 = world.getIslandByIndex(4);
        island5.add(ColorT.BLACK);
        world.checkJoin(island5);
        assertEquals(10, world.getSize());
        //three consecutive island need to bo merged.
        Island island6 = world.getIslandByIndex(7);
        island6.add(ColorT.GREY);
        Island island7 = world.getIslandByIndex(8);
        island7.add(ColorT.GREY);
        Island island8 = world.getIslandByIndex(9);
        island8.add(ColorT.GREY);
        world.checkJoin(island7);
        assertEquals(8, world.getSize());
    }

    /** Method getSize tests the size of the World's getter, means as number of distinct Island.*/
    @Test
    @DisplayName("World's size getter test")
    void getSize() {
        assertEquals(12, world.getSize());
        //other tests are inside checkJoin().
    }

    /** Method moveMN tests the move of Mother Nature.*/
    @Test
    @DisplayName("World's moveMN test")
    void moveMN() {
        int index = world.getMNPosition();
        Island islandMN = world.moveMN(5);
        assertEquals(index + 5 >= world.getSize() ? world.getIslandByIndex(index + 5 - world.getSize()) : world.getIslandByIndex(index + 5)
                , islandMN);
    }

    /** Method influence Strategy tests the World's influence strategy setter, getter and reset.*/
    @Test
    @DisplayName("Set influence strategy test")
    void influenceStrategy() {
        assertTrue(world.getStrategy() instanceof StandardInfluence);
        world.setStrategy(new AdditionalInfluence());
        assertTrue(world.getStrategy() instanceof AdditionalInfluence);
        world.resetStrategy();
        assertTrue(world.getStrategy() instanceof StandardInfluence);
    }

    /** Method checkEntry tests if an Island has noEntryTiles.*/
    @Test
    @DisplayName("World's checkEntry test")
    void checkEntry() {
        Island island = world.getIslandByIndex(world.getMNPosition());
        island.setNumNoEntry(2);
        assertEquals(false, world.checkEntry());
        assertEquals(1, island.getNumNoEntry());
        island.setNumNoEntry(-1);
        assertEquals(true, world.checkEntry());
        assertEquals(0, island.getNumNoEntry());
    }

    /** Method checkConquest tests if an island changes the owner.     */
    @Test
    @DisplayName("World's checkConquest test")
    void checkConquest() {
        ArrayList<Player> players = new ArrayList<>();
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9,6);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9,6);
        players.add(lisa);
        players.add(bob);
        players.add(alice);
        Map<Player, Integer> mapInfluence = new HashMap<>();
        Island islandMN = world.getIslandByIndex(world.getMNPosition());
        islandMN.add(ColorT.WHITE);
        //Case when there is an owner that has to change.
        mapInfluence.put(lisa, 5);
        mapInfluence.put(bob, 4);
        mapInfluence.put(alice, 6);
        assertEquals(Optional.of(alice), world.checkConquest(mapInfluence, players));
        //Limit case when there is an owner that has the lowest influence and the other players have the same influence,
        // I choose to non to modify anything.
        mapInfluence.put(alice, 5);
        assertEquals(Optional.empty(), world.checkConquest(mapInfluence, players));
        //Case when there is an owner that hasn't to change, because he has the highest influence.
        mapInfluence.put(bob, 7);
        assertEquals(Optional.empty(), world.checkConquest(mapInfluence, players));
        //Case when there is an owner that hasn't to change, because he has the same influence of another player.
        mapInfluence.put(alice, 7);
        assertEquals(Optional.empty(), world.checkConquest(mapInfluence, players));
        //Case when there isn't an owner and one player conquest that Island.
        islandMN.remove(ColorT.WHITE);
        mapInfluence.put(lisa, 5);
        mapInfluence.put(bob, 4);
        mapInfluence.put(alice, 6);
        assertEquals(Optional.of(alice), world.checkConquest(mapInfluence, players));
        //Case when there isn't an owner and no one conquest that Island.
        mapInfluence.put(bob, 6);
        assertEquals(Optional.empty(), world.checkConquest(mapInfluence, players));
    }

    /** Method setBannedColorS tests the change of the banned ColorS */
    @Test
    @DisplayName("World's setBannedColorS test")
    void setBannedColorS() {
        assertEquals(Optional.empty(), world.getBannedColorS());
        world.setBannedColorS(ColorS.GREEN);
        assertEquals(Optional.of(ColorS.GREEN), world.getBannedColorS());
    }
}