package it.polimi.ingsw.model;

import it.polimi.ingsw.model.pawn.Student;

import java.util.ArrayList;
import java.util.Collections;

public class StudentContainer {
    private ArrayList<Student> students;
    private ArrayList<Student> initial;

    public StudentContainer() {
        students = new ArrayList<>(120);
        for(ColorS c: ColorS.values())
            for(int i=0;i<24;i++)
                students.add(new Student(c));

        for(ColorS c: ColorS.values())
            for(int i=0;i<2;i++)
                initial.add(new Student(c));

        Collections.shuffle(students);
        Collections.shuffle(initial);
    }

    /**
     * Is used to populate the islands when the game is initialized
     * @return s a Student chosen from a pool smaller than the total number of Students
     */
    public Student initialDraw(){
        Student s=initial.get(0);
        initial.remove(0);
        return s;
    }

    /**
     * Returns a random Student
     * @return s a random Student chosen from all Students that aren't on the board yet
     */
    public Student draw(){
        Student s=students.get(0);
        students.remove(0);
        return s;
    }
}
