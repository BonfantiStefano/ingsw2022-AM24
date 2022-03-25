package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchoolBoard implements CanAcceptStudent, CanRemoveStudent, AcceptTower{
    private ArrayList<Student> entrance;
    private ArrayList<Prof> profs;
    private ArrayList<Tower> towers;
    private Map<ColorS,Integer> hall;

    public SchoolBoard(){
        entrance=new ArrayList<Student>();
        profs=new ArrayList<Prof>();
        towers=new ArrayList<Tower>();
        hall=new HashMap<ColorS, Integer>();
        for(ColorS c:ColorS.values()){
            hall.put(c,0);
        }
    }


    public void add(Student s){
        entrance.add(s);
    }
    public void remove(Student s){
        entrance.remove(s);
    }
    public void add(Tower t){towers.add(t);}
    public void remove(Tower t){towers.remove(t);}

    public void entranceToHall(Student s){
        entrance.remove(s);
        hall.put(s.getColor(),hall.getOrDefault(s.getColor(),0)+1);
    }

    public void PrintEntrance(){
        entrance.forEach(s->System.out.println(s.getColor()));
    }
}
