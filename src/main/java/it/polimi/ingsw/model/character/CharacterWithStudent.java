package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.CanAcceptStudent;
import it.polimi.ingsw.model.CanRemoveStudent;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.EVENT;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;
import it.polimi.ingsw.model.gameboard.GameBoard;

import java.beans.PropertyChangeSupport;
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
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

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
        getListener().firePropertyChange(String.valueOf(EVENT.CHANGE_CHARACTER_S), null, this);
    }


    /**
     * Remove a Student
     * @param s the Color of the Student being removed
     */
    @Override
    public void remove(ColorS s) throws NoSuchStudentException {
        if(students.contains(s)){
            students.remove(s);
            getListener().firePropertyChange(String.valueOf(EVENT.CHANGE_CHARACTER_S), null, this);
        }
        else {throw new NoSuchStudentException();}
    }

    /**
     * Gets the Students on this Character
     * @return list of Students on the Character
     */
    public ArrayList<ColorS> getStudents(){
        return students;
    }


}
