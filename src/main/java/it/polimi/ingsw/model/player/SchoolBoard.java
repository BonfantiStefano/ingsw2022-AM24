package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gameboard.GameBoard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * SchoolBoard represents the magical school board received by each player before the start of the game.
 * It contains pawns of five different colors representing students and towers of the color chosen by the player.
 * Different magical schools will compete against each other for the influence of the five professors
 *
 */
public class SchoolBoard implements CanAcceptStudent, CanRemoveStudent, AcceptTower {
    private final static int MAX_STUD = 10;

    private ArrayList<ColorS> entrance;
    private ArrayList<ColorT> towers;
    private Map<ColorS,Integer> hall;
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

    /**
     * Constructor SchoolBoard creates a new SchoolBoard instance subdivided into three areas, two for the students
     * (one of which is used to check if the player has the majority for any of the colors for the control of professors)
     * and one for the towers.
     */
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
     * Method addListener is used in order to register an event listener
     * @param gameBoard - event listener that is used for receiving the events
     */
    public void addListener(PropertyChangeListener gameBoard){
        listener.addPropertyChangeListener(gameBoard);
    }

    /**
     * Moves a Student form Entrance to Hall
     * @param s the color of the Student being moved
     * @return true if the Player gains a coin
     * @throws PlaceFullException if there is no more available space for the students in the hall
     * @throws NoSuchStudentException if there is no students in the entrance
     */
    public boolean entranceToHall(ColorS s) throws PlaceFullException, NoSuchStudentException {
        int temp = hall.get(s) + 1;
        if(temp > MAX_STUD){
            throw new PlaceFullException();
        }
        if(!entrance.contains(s)){
            throw new NoSuchStudentException();
        }
        else{
            boolean result = addToHall(s);
            entrance.remove(s);
            return result;
        }
    }

    /**
     * Removes a Student directly from the Hall
     * @param s the Student being removed
     * @throws NoSuchStudentException if there is no students of the chosen color in the hall
     */
    public void removeHall(ColorS s) throws NoSuchStudentException {
        int temp = hall.get(s)-1;
        if(temp < 0) throw new NoSuchStudentException();
        else hall.put(s,temp);
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_SCHOOLBOARD), null, this);
    }

    /**
     * Moves a Student from the Hall to the Entrance
     * @param s the Student being moved
     * @throws NoSuchStudentException if there is no students of the chosen color in the hall
     */
    public void hallToEntrance(ColorS s) throws NoSuchStudentException {
        int temp = hall.get(s);
        if(temp == 0){
            throw new NoSuchStudentException();
        }
        else{
            temp = hall.get(s)-1;
            hall.put(s, temp);
            entrance.add(s);
        }
    }

    /**
     * Adds a student directly to the Hall
     * @param s the Student being added
     * @return true if the Player gains a coin
     * @throws PlaceFullException if there is no more available space for the students in the hall
     */
    public boolean addToHall(ColorS s) throws PlaceFullException {
        int temp = hall.get(s) + 1;
        if(temp > MAX_STUD){
            throw new PlaceFullException();
        }
        else {
            hall.put(s, temp);
            listener.firePropertyChange(String.valueOf(EVENT.CHANGE_SCHOOLBOARD), null, this);
            return temp % 3 == 0 && temp != 0;
        }
    }

    /**
     * Adds a Student to the Entrance
     * @param s the color of the Student being added
     */
    public void add(ColorS s){
        entrance.add(s);
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_SCHOOLBOARD), null, this);
    }

    /**
     * Removes a Student from the Entrance
     * @param s the color of the Student being removed
     * @throws NoSuchStudentException if there is no students in the entrance
     */
    public void remove(ColorS s) throws NoSuchStudentException {
        if(!entrance.contains(s))
            throw new NoSuchStudentException();
        entrance.remove(s);
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_SCHOOLBOARD), null, this);
    }

    /**
     * Adds a Tower to the Player's Board
     * @param t the color of the Tower being added
     */
    public void add(ColorT t){
        towers.add(t);
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_SCHOOLBOARD), null, this);
    }

    /**
     * Removes a Tower from the Player's Board
     * @param t the color of the Tower being removed
     */
    public void remove(ColorT t){
        towers.remove(t);
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_SCHOOLBOARD), null, this);
    }

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
