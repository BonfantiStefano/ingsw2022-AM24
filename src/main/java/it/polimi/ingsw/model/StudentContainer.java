package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class StudentContainer models a container with a set amount of Students
 */
public class StudentContainer {
    private final ArrayList<ColorS> students;
    private final ArrayList<ColorS> initial;

    /**
     * Creates a new StudentContainer object filled with Students
     */
    public StudentContainer() {
        int numStudents = 120;
        students = new ArrayList<>(numStudents);
        for(ColorS c: ColorS.values())
            for(int i=0;i<24;i++)
                students.add(c);
        initial=new ArrayList<>();
        Collections.shuffle(students);
        Collections.shuffle(initial);
    }

    /**
     * Is used to populate the islands when the game is initialized
     * @return s a Student chosen from a pool smaller than the total number of Students
     */
    public ArrayList<ColorS> initialDraw(){
        //no exception needed because the initial draw won't be called after the game initialization
        for(ColorS c: ColorS.values())
            for(int i=0;i<2;i++)
                initial.add(c);
        return initial;
    }

    /**
     * Returns a random Student
     * @return s a random Student chosen from all Students that aren't on the board yet
     */
    public ColorS draw(){
        //add exception to signal that all Students have been drawn
        return students.remove(0);
    }

    /**
     * Checks if there are Students remaining
     * @return true if it's possible to draw other Students
     */
    public boolean canDraw(){
        return students.size()>0;
    }

    /**
     * Method addStudent puts the student back in the bag
     * @param c of type Student - the student put in the bag
     */
    public void addStudent(ColorS c){
        students.add(c);
    }

}
