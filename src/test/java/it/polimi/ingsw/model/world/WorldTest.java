package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.pawn.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//WORK IN PROGRESS

/**
 * Class WorldTest tests Worls class.
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

    @Test
    void getInfluenceIsland() {
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

    @Test
    void checkJoin() {
    }

    /** Method getSize tests the size of the World's getter, means as number of distinct Island.*/
    @Test
    @DisplayName("World's size getter test")
    void getSize() {
        assertEquals(12, world.getSize());
    }

    @Test
    void setStrategy() {
    }

    @Test
    void resetStrategy() {
    }

    /** Method getStrategy tests the influence of the World's getter.*/
    @Test
    @DisplayName("World's influence strategy getter test")
    void getStrategy() {
    }
}