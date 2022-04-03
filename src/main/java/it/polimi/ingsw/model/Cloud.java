package it.polimi.ingsw.model;
import it.polimi.ingsw.model.pawn.Student;

import java.util.ArrayList;

/**
 * Cloud class contains a list of Student that are contained on the cloud. It has the method add and remove inherited from
 * the respective interfaces.
 *
 * @author Bonfanti Stefano
 */
public class Cloud implements CanAcceptStudent, CanRemoveStudent{
    private ArrayList<Student> students;

    /**Constructor Cloud creates a new empty cloud instance.*/
    public Cloud() {
        this.students = new ArrayList<>();
    }

    /**
     * Method add inserts a student in the List of the Students.
     * @param s Student
     */
    @Override
    public void add(Student s){

        students.add(s);
    }

    /**
     * Method remove deletes a student from the List of the Students.
     * @param s Student
     */
    @Override
    public void remove(Student s){

        students.remove(s);
    }

    /**
     * Method getStudents returns the list of the students presents on the Cloud.
     * @return the list of the Student.
     */
    public ArrayList<Student> getStudents() {
        return students;
    }
}
