package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.CanAcceptStudent;
import it.polimi.ingsw.model.CanRemoveStudent;
import it.polimi.ingsw.model.pawn.Student;

import java.util.ArrayList;

/**
 *  These characters will have a number of Students that can be moved from or to a destination
 *  In the current implementation the controller will handle the movements of all pawns so this class does not need
 *  an implementation of the play() method
 *  NOT FINAL
 */
public class CharacterWithStudent extends Character implements CanAcceptStudent, CanRemoveStudent {
    private ArrayList<Student> students;
    private int maxStudents;

    public CharacterWithStudent(int cost, String description, int maxStudents) {
        super(cost, description);
        students=new ArrayList<>();
        this.maxStudents=maxStudents;
    }

    public void add(Student s){
        students.add(s);
    }
    public void remove(Student s){
        students.remove(s);
    }
}
