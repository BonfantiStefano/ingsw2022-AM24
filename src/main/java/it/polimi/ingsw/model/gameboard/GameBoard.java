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

    private final static String ERROR_MESS = " has to choose another card!";
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
        lastAssistants = new ArrayList<>();
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

    /**
     * Method setChoosenAssistant allows the player to change his last Assistant card
     *
     * @param player of type Player - the player that will change his Assistant card.
     * @param index of type int - the index of the card that will replace the previous one
     */
    public void setChoosenAssistant(Player player, int index){
        player.chooseAssistant(index-1);
    }

    /**
     * Method setLastAssistants gets a player and adds his Assistant card in the list of all the cards of this round
     *
     * @param player of type Player - the player which Assistant card will be added to the list with other
     * players' cards
     */
    public void setLastAssistants(Player player){
        Assistant assistant = player.getLastAssistant();
        if(lastAssistants.isEmpty()){
            lastAssistants.add(assistant);
        }
        else{
            boolean result = false;
            for (Assistant a : lastAssistants) {
                if (!(a.getTurn()==assistant.getTurn())|| (player.getMyCards().numCards() == 1))
                    result = true;
            }
            if(result) lastAssistants.add(assistant);
        }
    }

    /**
     * Method getPlayerByNickname searches the player by his nickname in the list which contains all
     * the players
     *
     * @param nickname of type String - the nickname of the player.
     * @return Player - the player, null if there's no player with that nickname.
     */
    public Player getPlayerByNickname(String nickname) {
        for (Player p : players) {
            if (p.getNickname().equalsIgnoreCase(nickname)) {
                return p;
            }
        }
        return null;
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

    /**
     * Method getActivePlayer returns the active player in this round
     *
     * @return activePlayer of type Player
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Method setCard adds a card chosen by the challenger to the deck.
     *
     * @param activePlayer of type Player - the next active player.
     */
    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Method getSizeList returs the size of lastAssisnts' list
     *
     * @return  int - the size of the list which contains the last Assistant cards
     */
    public int getSizeList(){
        return lastAssistants.size();
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