package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Island class contains the attributes and the method to manage the Island of the Game, are considered Island both
 * single Island and group of Island joined together.
 *
 * @author Bonfanti Stefano
 */
public class Island implements CanAcceptStudent, AcceptTower{

    private final ArrayList<ColorS> students;
    private final ArrayList<ColorT> towers;
    private final int numSubIsland;
    private int numNoEntry;
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

    /**Constructor Island creates a new Island instance.*/
    public Island(){
        students = new ArrayList<>();
        towers = new ArrayList<>();
        numNoEntry = 0;
        numSubIsland = 1;
    }

    /**
     * Method addListener is used in order to register an event listener
     * @param gameBoard - event listener that is used for receiving the events
     */
    public void addListener(PropertyChangeListener gameBoard){
        listener.addPropertyChangeListener(gameBoard);
    }

    /**
     * Method add inserts a student in the List of the Students.
     * @param student ColorS - The student that has to be added to the list.
     */
    @Override
    public void add(ColorS student) {
        students.add(student);
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_ISLAND), null, this);
    }

    /**
     * Method add inserts a tower in the List of the Towers.
     * @param tower ColorT - The tower that has to be added to the list.
     */
    @Override
    public void add(ColorT tower){
        towers.add(tower);
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_ISLAND), null, this);

    }

    /**
     * Method remove deletes a tower from the List of the Towers.
     * @param tower ColorT - The tower that has to be removed to the list.
     */
    @Override
    public void remove(ColorT tower) {
        if(towers.size() > 0) {
            towers.remove(tower);
            listener.firePropertyChange(String.valueOf(EVENT.CHANGE_ISLAND), null, this);
        }
    }

    /**
     * Method getTowerColor returns the color of the towers on the Island, if there aren't towers return null.
     * @return the color of the Towers if present.
     */
    public Optional<ColorT> getTowerColor(){
        if(towers.size() > 0) {
            return Optional.of(towers.get(0));
        }
        return Optional.empty();
    }

    /**
     * Method getNumSubIsland returns the number of the Island that composes the group of Islands.
     * @return the number of subIsland.
     */
    public int getNumSubIsland() {
        return numSubIsland;
    }

    /**
     * Method getNumNoEntry returns the numbers of noEntry Card presents on the Island.
     * @return the number of noEntry Card.
     */
    public int getNumNoEntry() {
        return numNoEntry;
    }

    /**
     * Method setNumNoEntry changes the number of noEntry Card,
     * in order to remove a noEntry Card, ne can have negative value (-1).
     * @param ne int - number of noEntry Card.
     */
    public void setNumNoEntry(int ne) {
        numNoEntry = numNoEntry + ne;
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_ISLAND), null, this);
    }

    /**Constructor Island creates a new Island instance by merging two existing Island.*/
    public Island(Island i1, Island i2) {
        students = new ArrayList<>();
        towers = new ArrayList<>();
        this.students.addAll(i1.students);
        this.students.addAll(i2.students);
        this.towers.addAll(i1.towers);
        this.towers.addAll(i2.towers);
        this.numNoEntry = i1.numNoEntry + i2.numNoEntry;
        this.numSubIsland = i1.numSubIsland + i2.numSubIsland;
    }

    /**
     * Method getNumStudentByColor returns the numbers of Students presents on the Island with a particular color.
     * @param c ColorS - color of the Student
     * @return the number of Students with color c.
     */
    public int getNumStudentByColor(ColorS c) {
        int numStudent = 0;
        for(ColorS student : students) {
            if(student.equals(c)) {
                numStudent++;
            }
        }
        return numStudent;
    }

    /**
     * Method getStudents returns the students placed on the island
     * @return students
     */
    public ArrayList<ColorS> getStudents() {
        return students;
    }


    /**
     * Method getTowers returns the towers placed on the island
     * @return towers
     */
    public ArrayList<ColorT> getTowers() {
        return towers;
    }

}
