package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Island implements AcceptStudent, AcceptTower{
    private ArrayList<Student> students;
    private Tower tower;
    private HashMap<ColorS,Integer> influence;

    public Island(){
        students=new ArrayList<>();
        influence=new HashMap<>();
        for(ColorS c:ColorS.values()){
            influence.put(c,0);
        }
    }
    public void add(Student s) {
        students.add(s);
    }
    public void remove(Student s) {
        students.remove(s);
    }

    public void add(Tower t){
        tower=t;
    }
    public void remove(Tower t){
        tower=null;
    }
    public ColorT getTowerColor(){
        return tower.getColor();
    }

    public HashMap<ColorS, Integer>getInfluence(){
        for(Student s:students){
            influence.put(s.getColor(),influence.get(s.getColor())+1);
        }
        return influence;
    }

}
