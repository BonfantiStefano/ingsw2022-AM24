package it.polimi.ingsw.model;

import java.util.ArrayList;

public class GameBoard implements HasStrategy<ProfStrategy>{
    private ArrayList<Player> players;
    //private ArrayList<SchoolBoard> schoolBoards;
    private World world;
    private ArrayList<ArrayList<Island>> islands;
    private Player activePlayer;
    private StudentContainer bag;
    private ProfStrategy strategy;

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
    public void move(Student s, CanRemoveStudent from, CanAcceptStudent to){
        from.remove(s);
        to.add(s);
    }
    public ArrayList<Assistant> getLastAssistants() {
        return null;
    }

    public void setStrategy(ProfStrategy strategy){
        this.strategy=strategy;
    }

    @Override
    public void resetStrategy() {
        strategy=new StandardProf();
    }


}