package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Island implements CanAcceptStudent, AcceptTower{
    private ArrayList<Student> students;
    private ArrayList<Tower> towers;
    private HashMap<ColorS,Integer> influence;
    private int numSubIsland;
    private int numNoEntry;



    public Island(){
        students = new ArrayList<>();
        towers = new ArrayList<>();
        influence = new HashMap<>();
        numNoEntry = 0;
        numSubIsland = 1;
        for(ColorS c:ColorS.values()){
            influence.put(c,0);
        }
    }


    @Override
    public void add(Student s) {
        students.add(s);
        influence.replace(s.getColor(), influence.get(s.getColor())+1);
    }


    @Override
    public void add(Tower t){
        if(towers.size() > 0 && t.getColor() != towers.get(0).getColor()) {
            //throw WrongColorException;
        }
        towers.add(t);
    }



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


    public Optional<ColorT> getTowerColor(){
        if(towers.size() > 0) {
            return Optional.of(towers.get(0).getColor());
        }
        return null;
        // oppure basta return Optional.ofNullable(towers.get(0).getColor());
    }

    public int getNumSubIsland() {
        return numSubIsland;
    }

    public int getNumNoEntry() {
        return numNoEntry;
    }


    //in order to subctract a NoEntryCard, ne can have negative value
    public void setNumNoEntry(int ne) {
        numNoEntry = numNoEntry + ne;
    }


    public Island(Island i1, Island i2) {
        students = new ArrayList<>();
        towers = new ArrayList<>();
        influence = new HashMap<>();
        this.students.addAll(i1.students);
        this.students.addAll(i2.students);
        this.towers.addAll(i1.towers);
        this.towers.addAll(i2.towers);
        this.numNoEntry = i1.numNoEntry + i2.numNoEntry;
        this.numSubIsland = i1.numSubIsland + i2.numSubIsland;
        for(ColorS c:ColorS.values()){
            this.influence.put(c,i1.influence.get(c) + i2.influence.get(c));
        }
    }

    public int getNumStudentByColor(ColorS c) {
        return influence.get(c);
    }

}
