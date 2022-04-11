package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyPlaceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentContainer {
    private ArrayList<ColorS> students;
    private ArrayList<ColorS> initial;

    public StudentContainer() {
        students = new ArrayList<>(120);
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
     * @throws  EmptyPlaceException if there is no more students in the bag
     */
    public ColorS draw() throws EmptyPlaceException {
        if(students.isEmpty()) throw new EmptyPlaceException();
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
