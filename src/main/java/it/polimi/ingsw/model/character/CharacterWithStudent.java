package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.CanAcceptStudent;
import it.polimi.ingsw.model.CanRemoveStudent;
import it.polimi.ingsw.model.ColorS;

import java.util.ArrayList;

/**
 *  These characters will have a number of Students that can be moved from or to a destination
 *  In the current implementation the controller will handle the movements of all pawns so this class does not need
 *  an implementation of the play() method
 *  NOT FINAL
 */
public class CharacterWithStudent extends Character implements CanAcceptStudent, CanRemoveStudent {
    private ArrayList<ColorS> students;
    private int maxStudents;

    /**
     * Creates a new CharacterWithStudent object
     */
    public CharacterWithStudent(int cost, String description, int maxStudents) {
        super(cost, description);
        students=new ArrayList<>();
        this.maxStudents=maxStudents;
    }

    /**
     * Adds a Student
     * @param s the Color of the Student being added
     */
    public void add(ColorS s){
        students.add(s);
    }

    /**
     * Remove a Student
     * @param s the Color of the Student being removed
     */
    public void remove(ColorS s){
        students.remove(s);
    }
}
