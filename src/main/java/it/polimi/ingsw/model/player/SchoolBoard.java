package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchoolBoard implements CanAcceptStudent, CanRemoveStudent, AcceptTower {
    private ArrayList<ColorS> entrance;
    private ArrayList<ColorT> towers;
    private Map<ColorS,Integer> hall;

    public SchoolBoard(ColorT color, int numTowers){
        entrance = new ArrayList<>();
        towers= new ArrayList<>();
        for(int i = 0; i < numTowers; i ++){
            towers.add(color);
        }
        hall=new HashMap<ColorS, Integer>();
        for(ColorS c:ColorS.values()){
            hall.put(c,0);
        }
    }

    /**
     * Moves a Student form Entrance to Hall
     * @param s the color of the Student being moved
     * @return true if the Player gains a coin
     */
    public boolean entranceToHall(ColorS s){
        int temp = hall.get(s) + 1;
        hall.put(s,temp);
        entrance.remove(s);
        return (temp%3 == 0) && temp!=0;
    }

    /**
     * Removes a Student directly from the Hall
     * @param s the Student being removed
     */
    public void removeHall(ColorS s){
        int temp = hall.get(s)-1;
        hall.put(s,temp);
    }

    /**
     * Moves a Student from the Hall to the Entrance
     * @param s the Student being moved
     */
    public void hallToEntrance(ColorS s){
        hall.put(s, hall.get(s)-1);
    }

    /**
     * Adds a student directly to the Hall
     * @param s the Student being added
     * @return true if the Player gains a coin
     */
    public boolean addToHall(ColorS s){
        int temp = hall.get(s) + 1;
        hall.put(s, temp);
        return temp%3==0 && temp!=0;
    }

    /**
     * Adds a Student to the Entrance
     * @param s the color of the Student being added
     */
    public void add(ColorS s){
        entrance.add(s);
    }

    /**
     * Removes a Student from the Entrance
     * @param s the color of the Student being removed
     */
    public void remove(ColorS s){
        entrance.remove(s);
    }

    /**
     * Adds a Tower to the Player's Board
     * @param t the color of the Tower being added
     */
    public void add(ColorT t){
        towers.add(t);
    }

    /**
     * Removes a Tower from the Player's Board
     * @param t the color of the Tower being removed
     */
    public void remove(ColorT t){towers.remove(t);}

    /**
     * Get the Entrance to access the Students it contains
     * @return the Board's Entrance
     */
    public ArrayList<ColorS> getEntrance() {
        return entrance;
    }

    /**
     * Get the number of Students by ColorS
     * @param c ColorS
     * @return the number of ColoS Students in the player's Hall
     */
    public int getHall(ColorS c){
        return hall.get(c);
    }

    /**
     * Method getHall gives the access to the player's hall
     * @return the Board's Hall
     */
    public Map<ColorS,Integer> getHall(){
        return hall;
    }

    /**
     * Method getTowers returns the list which contains the player's towers
     * @return towers of type ArrayList<ColorT>
     */
    public ArrayList<ColorT> getTowers(){
        return towers;
    }
}
