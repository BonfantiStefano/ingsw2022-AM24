package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;

public class StudentContainer {
    private ArrayList<Student> students;
    private static StudentContainer instance=null;

    private StudentContainer() {
        students = new ArrayList<>(130);
        for(ColorS c: ColorS.values())
            for(int i=0;i<26;i++)
                students.add(new Student(c));
        Collections.shuffle(students);
    }

    public StudentContainer getInstance(){
        if (instance == null)
            instance = new StudentContainer();
        return instance;
    }

    public Student draw(){
        Student s=students.get(0);
        students.remove(0);
        return s;
    }
}
