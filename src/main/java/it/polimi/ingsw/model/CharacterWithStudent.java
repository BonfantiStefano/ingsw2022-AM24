package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 *  These characters will have a number of Students that can be moved from or to a destination
 *  In the current implementation the controller will handle the movements of all pawns so this class does not need
 *  an implementation of the play() method
 */
public class CharacterWithStudent extends Character implements CanAcceptStudent, CanRemoveStudent{
    private ArrayList<Student> students;

    public CharacterWithStudent(int cost, String description) {
        super(cost, description);
        students=new ArrayList<>();
    }

    @Override
    public void play(){}

    public void add(Student s){
        students.add(s);
    }
    public void remove(Student s){
        students.remove(s);
    }
}
