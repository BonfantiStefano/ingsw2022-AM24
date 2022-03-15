package it.polimi.ingsw.model;

import java.util.ArrayList;

public class GameBoard {
    private ArrayList<Player> players;
    //private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Cloud> clouds;
    private ArrayList<ArrayList<Island>> islands;
    private Player activePlayer;
    private StudentContainer bag;

    public GameBoard(){

    }
    public void checkIsland(Island i){}
    public void join(Island i1, Island i2){
        islands.remove(i2);
        islands.get(islands.indexOf(i1)).add(i2);
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }
    //implementare varie move
    public void move(Student s, AcceptStudent from, AcceptStudent to){
        from.remove(s);
        to.add(s);
    }
    public ArrayList<Assistant> getLastAssistants() {
        return null;
    }



}