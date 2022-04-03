package it.polimi.ingsw.model;

import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.pawn.Tower;
import it.polimi.ingsw.model.world.World;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class CloudTest tests Cloud class.
 *
 * @author Bonfanti Stefano
 * @see Cloud
 */
class CloudTest {

    /** Method listStudent tests the Student's getter and setter that are in the ArrayList<Student>.*/
    @Test
    @DisplayName("Add Student test")
    void listStudent() {
        Cloud cloud = new Cloud();
        Student s1 = new Student(ColorS.BLUE);
        Student s2 = new Student(ColorS.YELLOW);
        Student s3 = new Student(ColorS.RED);
        cloud.add(s1);
        cloud.add(s2);
        cloud.add(s3);
        ArrayList<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        assertEquals(students, cloud.getStudents());
        cloud.remove(s1);
        students.remove(s1);
        assertEquals(students, cloud.getStudents());
    }
}