package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;

public class StudentContainer {
    private ArrayList<ColorS> students;
    private ArrayList<ColorS> initial;

    public StudentContainer() {
        students = new ArrayList<>(120);
        for(ColorS c: ColorS.values())
            for(int i=0;i<24;i++)
                students.add(c);
        initial=new ArrayList<>();
        for(ColorS c: ColorS.values())
            for(int i=0;i<2;i++)
                initial.add(c);

        Collections.shuffle(students);
        Collections.shuffle(initial);
    }

    /**
     * Is used to populate the islands when the game is initialized
     * @return s a Student chosen from a pool smaller than the total number of Students
     */
    public ArrayList<ColorS> initialDraw(){
        //no exception needed because the initial draw won't be called after the game initialization
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
}
