package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.pawn.Tower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchoolBoard implements CanAcceptStudent, CanRemoveStudent, AcceptTower {
    private ArrayList<Student> entrance;
    private ArrayList<Tower> towers;
    private Map<ColorS,Integer> hall;
    private StudentContainer container = new StudentContainer();

    public SchoolBoard(ColorT color, int numStudents, int numTowers){
        entrance=new ArrayList<Student>();
        for(int i = 0; i < numStudents; i++){
            Student s = container.draw();
            entrance.add(s);
        }
        towers= new ArrayList<>();
        for(int i = 0; i < numTowers; i ++){
            towers.add(new Tower(color));
        }
        hall=new HashMap<ColorS, Integer>();
        for(ColorS c:ColorS.values()){
            hall.put(c,0);
        }
    }

    public boolean entranceToHall(Student s){
        int temp = hall.get(s.getColor()) + 1;
        hall.put(s.getColor(),temp);
        entrance.remove(s);
        return (temp%3 == 0);
    }

    public void removeHall(Student s){
        int temp = hall.get(s.getColor())-1;
        hall.put(s.getColor(),temp);
    }

    public void add(Student s){
        entrance.add(s);
    }
    public void remove(Student s){
        entrance.remove(s);
    }
    public void add(Tower t){towers.add(t);}
    public void remove(Tower t){towers.remove(t);}


    public ArrayList<Student> getEntrance() {
        return entrance;
    }

    public Map<ColorS,Integer> getHall(){
        return hall;
    }
}
