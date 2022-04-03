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
        ArrayList<Student> students = new ArrayList<>();
        Island mnLocation = new Island();
        Student student1 = new Student(ColorS.GREEN);
        students.add(student1);
        Student student2 = new Student(ColorS.RED);
        students.add(student2);
        Student student3 = new Student(ColorS.GREEN);
        students.add(student3);
        Student student4 = new Student(ColorS.YELLOW);
        students.add(student4);
        Student student5 = new Student(ColorS.YELLOW);
        students.add(student5);
        Student student6 = new Student(ColorS.PINK);
        students.add(student6);
        Student student7 = new Student(ColorS.BLUE);
        students.add(student7);
        Student student8 = new Student(ColorS.PINK);
        students.add(student8);
        Student student9 = new Student(ColorS.RED);
        students.add(student9);
        Student student10 = new Student(ColorS.BLUE);
        students.add(student10);
        world = new World(students, mnLocation);
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
        HashMap<ColorS, Player> profs = new HashMap<>();
        profs.put(ColorS.GREEN, lisa);
        profs.put(ColorS.BLUE, bob);
        profs.put(ColorS.YELLOW, alice);
        for(counter = 0; counter < 3; counter++) {
            island.add(new Student(ColorS.GREEN));
        }
        for(counter = 0; counter < 4; counter++) {
            island.add(new Student(ColorS.YELLOW));
        }
        island.add(new Student(ColorS.BLUE));
        island.add(new Student(ColorS.BLUE));
        island.add(new Student(ColorS.RED));
        island.add(new Tower(ColorT.BLACK));
        HashMap<Player, Integer> mapInfluence = world.getInfluenceIsland(island, profs, players, Optional.empty());
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
            Student s1 = new Student(ColorS.GREEN);
            i1.add(s1);
            Student s2 = new Student(ColorS.RED);
            i1.add(s2);
            Student s3 = new Student(ColorS.GREEN);
            i2.add(s3);
        }
        Student s4 = new Student(ColorS.YELLOW);
        i1.add(s4);
        Student s5 = new Student(ColorS.RED);
        i2.add(s5);
        Tower t1 = new Tower(ColorT.WHITE);
        i1.add(t1);
        Tower t2 = new Tower(ColorT.WHITE);
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
        island1.add(new Tower(ColorT.WHITE));
        island2.add(new Tower(ColorT.WHITE));
        world.checkJoin(island1);
        assertEquals(11, world.getSize());
        //nothing needs to be merged.
        Island island3 = world.getIslandByIndex(5);
        island3.add(new Tower(ColorT.WHITE));
        world.checkJoin(island3);
        assertEquals(11, world.getSize());
        //merge of consecutive island.
        Island island4 = world.getIslandByIndex(6);
        island4.add(new Tower(ColorT.WHITE));
        world.checkJoin(island4);
        assertEquals(10, world.getSize());
        //consecutive island with different Tower color, nothing to do.
        Island island5 = world.getIslandByIndex(4);
        island5.add(new Tower(ColorT.BLACK));
        world.checkJoin(island5);
        assertEquals(10, world.getSize());
        //three consecutive island need to bo merged.
        Island island6 = world.getIslandByIndex(7);
        island6.add(new Tower(ColorT.GREY));
        Island island7 = world.getIslandByIndex(8);
        island7.add(new Tower(ColorT.GREY));
        Island island8 = world.getIslandByIndex(9);
        island8.add(new Tower(ColorT.GREY));
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

}