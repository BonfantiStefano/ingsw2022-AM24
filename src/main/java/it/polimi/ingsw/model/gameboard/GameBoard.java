package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.profstrategy.StandardProf;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.World;

import java.util.ArrayList;
import java.util.Map;

public class GameBoard implements HasStrategy<ProfStrategy> {
    private ArrayList<Player> players;
    private World world;
    private ArrayList<ArrayList<Island>> islands;
    private Player activePlayer;
    private StudentContainer bag;
    private ProfStrategy strategy;
    private Map<ColorS, Player> profs;

    public GameBoard(){

    }

    public void checkIsland(Island i){}

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

    @Override
    public void setStrategy(ProfStrategy strategy){
        this.strategy=strategy;
    }

    @Override
    public void resetStrategy() {
        strategy=new StandardProf();
    }


}