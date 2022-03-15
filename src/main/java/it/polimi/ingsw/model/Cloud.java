package it.polimi.ingsw.model;
import java.util.ArrayList;

public class Cloud implements AcceptStudent{
    private ArrayList<Student> students;

    public void add(Student s){
        students.add(s);
    }
    public void remove(Student s){
        students.remove(s);
    }
}
