package it.polimi.ingsw.model.gameboard;

//import it.polimi.ingsw.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.profstrategy.StandardProf;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.World;

import java.util.*;

/**
 * GameBoard class contains the main logic of Eriantys game, which is divided in areas. The
 * first area is the Player section, which contains information about the single player and
 * his SchoolBoard, Hand and Assistant . The second area is the World section,
 * which contains The Islands.  The third section is the Character section,
 * which contains several classes to manage every effect of the Characters.
 *
 * @author Baratto Marco, Bonfanti Stefano, Chyzheuskaya Hanna.
 */
public class GameBoard implements HasStrategy<ProfStrategy> {

    final private static int NUM_TOWERS = 8;
    final private static int NUM_STUDENTS = 7;
    final private static int NT = 6;
    final private static int NS = 9;
    private int numPlayers;
    private List<Player> players;
    private List<Cloud> clouds;
    private List<Assistant> lastAssistants;
    private World world;
    private Player activePlayer;
    private StudentContainer container;
    private ProfStrategy strategy;
    private Map<ColorS, Player> profs;

    /**Constructor GameBoard creates a new empty gameBoard instance.*/
    public GameBoard(int numPlayers){
        lastAssistants = new ArrayList<>();
        players = new ArrayList<>();
        activePlayer = null;
        container = new StudentContainer();
        strategy = new StandardProf();
        world = new World(container.initialDraw());
        this.numPlayers = numPlayers;

        profs=new HashMap<ColorS, Player>();
        for(ColorS c:ColorS.values()){
            profs.put(c,null);
        }

        clouds = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) {
            clouds.add(new Cloud());
        }
    }

    /**
     * Method getNumPlayers returns the number of the players taking part in the game.
     */
    public int getNumPlayers(){
        return players.size();
    }

    /**
     * Method SetNumPlayers changes the number of the players taking part in the game.
     * @param nPlayers of type int
     */
    public void setNumPlayers(int nPlayers){
      this.numPlayers = nPlayers;
    }

    /**
     * Method setChosenAssistant allows the player to change his last Assistant card
     *
     * @param player of type Player - the player that will change his Assistant card.
     * @param index of type int - the index of the card that will replace the previous one
     */
    public void setChosenAssistant(Player player, int index){

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

    /**
     * Method addPlayer creates and adds a new player.
     * The new player is created considering the fact that the number of pounds such as students and towers
     * in his schoolBoard depends on the number of the players taking part in the game.
     * @param nickname of type String
     * @param color of type ColorT
     * @param mage of type Mage
     */
    public void addPlayer(String nickname, ColorT color, Mage mage){
        if(numPlayers==3){
            Player p = new Player(nickname, color, mage, NT);
            for (int i = 0; i < NS; i++) {
                ColorS s = container.draw();
                p.getMyBoard().getEntrance().add(s);
            }
            players.add(p);
        }
        else{
            Player p = new Player(nickname, color, mage, NUM_TOWERS);
            for (int i = 0; i < NUM_STUDENTS; i++) {
                ColorS s = container.draw();
                p.getMyBoard().getEntrance().add(s);
            }
            players.add(p);
        }
    }

    /**
     * Method getActivePlayer returns the active player in this round
     * @return activePlayer of type Player
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Method setActivePlayer changes the active player.
     * @param nextActivePlayer of type Player - the next active player.
     */
    public void setActivePlayer(Player nextActivePlayer) {
        if(activePlayer != null) {
            this.activePlayer.setPlaying(false);
        }
        this.activePlayer = nextActivePlayer;
        nextActivePlayer.setPlaying(true);
    }

    /**
     * Method getSizeList returns the size of lastAssistants' list
     * @return  int - the size of the list which contains the last Assistant cards
     */
    public int getSizeList(){
        return lastAssistants.size();
    }

    /**
     * Method moveTower moves the towers from player's School Board to an island and the other way
     * @param t of type ColorT - the tower that will be moved
     * @param from of type AcceptTower - the place from whitch the tower is moved
     * @param to of type AcceptTower - where the tower is mowed
     */
    public void moveTower(ColorT t, AcceptTower from, AcceptTower to){
        from.remove(t);
        to.add(t);
    }

    /**
     * Method getPlayers returns the list of the players taking part in the game.
     * @return List<Player>
     */
    public List<Player> getPlayers(){
        return players;
    }

    //implementare varie move
    public void move(ColorS s, CanRemoveStudent from, CanAcceptStudent to){
        from.remove(s);
        to.add(s);
    }

    /**
     * method moveMN checks if the move is legal, then if there isn't noEntryTiles on the arrival Island
     * it calculates the influence on that island and in necessary change the owner of the island.
     * @param numMNSteps int - number of steps that Mother Nature want to do.
     */
    public void moveMN(int numMNSteps) /*throws IllegalMoveException*/{
        if(numMNSteps > activePlayer.getMNSteps()) {
            //throw new IllegalMoveException();
        }
        Island island =world.moveMN(numMNSteps);
        if(world.checkEntry()) {
            Optional<Player> nextOwner = world.checkConquest(world.getInfluenceIsland(island, profs, players), players);
            nextOwner.ifPresent(owner -> {conquest(owner, island);});
        }
    }

    /**
     * Method conquest removes the towers of the old owner, if presents, and adds the towers of the new owner.
     * @param nextOwner Player - The next owner of the Island.
     * @param island Island - The island to conquest.
     */
    void conquest(Player nextOwner, Island island) {
        Optional<Player> oldOwner = Optional.empty();
        for(Player p : players) {
            if(Optional.of(p.getColorTower()).equals(island.getTowerColor())) {
                oldOwner = Optional.of(p);
            }
        }
        for(int counter = 0; counter < island.getNumSubIsland(); counter++) {
            //oldOwner.ifPresent(/*move(new Tower(oldOwner.getColorTower(), island, oldOwner.getMyBoard()))*/);
            //move(new Tower(nextOwner,getColorTower()), nextOwner.getMyBoard(), island);
        }
    }

    public World getWorld() {
        return world;
    }

    public Map<ColorS, Player> getProfs() {
        return profs;
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