package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.pawn.Tower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Island class contains the attributes and the method to manage the Island of the Game, are considered Island both
 * single Island and group of Island joined togheter.
 *
 * @author Bonfanti Stefano
 */
public class Island implements CanAcceptStudent, AcceptTower {
    private ArrayList<Student> students;
    private ArrayList<Tower> towers;
    private int numSubIsland;
    private int numNoEntry;

    /**Constructor Island creates a new Island instance.*/
    public Island(){
        students = new ArrayList<>();
        towers = new ArrayList<>();
        numNoEntry = 0;
        numSubIsland = 1;
    }

    /**
     * Method add(Student) inserts a student in the List of the Students.
     * @param s Student
     */
    @Override
    public void add(Student s) {
        students.add(s);
    }

    /**
     * Method add inserts a tower in the List of the Towers.
     * @param t Tower
     */
    @Override
    public void add(Tower t){
        if(towers.size() > 0 && t.getColor() != towers.get(0).getColor()) {
            //throw WrongColorException;
        }
        towers.add(t);
    }

    /**
     * Method remove deletes a tower in the List of the Towers.
     * @param t Tower
     */
    @Override
    public void remove(Tower t) /*throws WrongColorException, EmptyListException*/{
        if(towers.size() > 0) {
            if(t.getColor() != towers.get(0).getColor()) {
                //throw WrongColorException;
            }
            towers.remove(t);
        }
        //throw EmptyListException;
    }

    /**
     * Method getTowerColor returns the color of the towers on the Island, if there aren't towers return null.
     * @return the color of the Towers if present.
     */
    public Optional<ColorT> getTowerColor(){
        if(towers.size() > 0) {
            return Optional.of(towers.get(0).getColor());
        }
        return null;
        // oppure basta return Optional.ofNullable(towers.get(0).getColor());
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
        for(Student s : students) {
            if(s.getColor().equals(c)) {
                numStudent++;
            }
        }
        return numStudent;
    }

}