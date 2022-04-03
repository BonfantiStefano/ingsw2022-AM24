package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.profstrategy.StandardProf;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.World;

import java.util.*;

public class GameBoard implements HasStrategy<ProfStrategy> {
    private final static String ERROR_CARD = "Attention: This card has been chosen by another player! Choose another card";
    private final static String MESSAGE = "choose an Assistant card";

    private MotherNature mn;
    private List<Player> players;
    private List<Cloud> clouds;
    private List<Assistant> lastAssistants;
    private World world;
    private Player activePlayer;
    private StudentContainer bag;
    private ProfStrategy strategy;
    private Map<ColorS, Player> profs;

    public GameBoard(){

        players = new ArrayList<>();
        activePlayer = null;
        bag = new StudentContainer();


        profs=new HashMap<ColorS, Player>();
        for(ColorS c:ColorS.values()){
            profs.put(c,null);
        }

        clouds = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            clouds.add(new Cloud());
        }
    }

    public void setChoosenAssistant(Player player, int index){
        player.chooseAssistant(index);
    }

    public Assistant getChoosenAssistant(Player player){
        return player.getLastAssistant();
    }

    /**
     * Method playAssistans allows each player to choose an Assistant card
     * and makes sure that no one plays the same card someone else has played in the round
     */
    public void playAssistants() {
        for (Player p : players) {
            boolean finish = false;
            do {
                System.out.println(p.getNickname() + ", " + MESSAGE);
                Assistant assistant = getChoosenAssistant(p);

                for (Assistant a : lastAssistants) {
                    if ((!a.equals(assistant)) || p.getMyCards().numCards() == 1) {
                        finish = false;
                        System.out.println(ERROR_CARD);
                        break;
                    }
                }
            } while (!finish);
        }
    }

    /**
     * Method nthPlayer is used for sorting palyers by their card value that determines the turn order
     * of the next round.
     */
    public void sortPlayers(){
        Collections.sort(players, (p1, p2) -> {
            return p1.getLastAssistant().compareTo(p2.getLastAssistant());
        });
    }

    /** Method nextPlayer skips to the next player. */
    public void nextPlayer(){
        Player p = getActivePlayer();
        Player nextPlayer = (p == null)? players.get(0)
                :  players.get(players.indexOf(p) + 1);
        setActivePlayer(nextPlayer);
    }

    /**
     * Method addPlayer adds a new player
     * @param player of type Player
     */
    public void addPlayer(Player player) {
        players.add(player);
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

    @Override
    public ProfStrategy getStrategy() {
        return strategy;
    }


}