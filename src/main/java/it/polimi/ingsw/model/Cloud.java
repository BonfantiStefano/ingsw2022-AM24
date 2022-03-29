package it.polimi.ingsw.model;
import it.polimi.ingsw.model.pawn.Student;

import java.util.ArrayList;

public class Cloud implements CanAcceptStudent, CanRemoveStudent{
    private ArrayList<Student> students;

    public void add(Student s){

        students.add(s);
    }


    public void remove(Student s){

        students.remove(s);
    }
}
