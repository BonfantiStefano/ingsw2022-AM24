package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
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
public class GameBoard implements HasStrategy<ProfStrategy>{

    final protected static int NUM_TOWERS = 8;
    final protected static int NUM_STUDENTS = 7;
    final protected static int NT = 6;
    final protected static int NS = 9;
    protected int numPlayers;
    protected ArrayList<Player> players;
    protected ArrayList<Cloud> clouds;
    protected ArrayList<Assistant> lastAssistants;
    protected World world;
    protected Player activePlayer;
    protected StudentContainer container;
    protected ProfStrategy strategy;
    protected HashMap<ColorS, Player> profs;
    protected boolean gameMustEnd;


    /**Constructor GameBoard creates a new empty gameBoard instance.
     * @param numPlayers of type int - the number of the players in the game
     */
    public GameBoard(int numPlayers){
        lastAssistants = new ArrayList<>();
        players = new ArrayList<>();
        activePlayer = null;
        container = new StudentContainer();
        strategy = new StandardProf();
        world = new World(container.initialDraw());
        this.numPlayers = numPlayers;
        this.gameMustEnd = false;
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
     * @return int - the number of the players in the game
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
     * Method chooseAssistants gets a player and the index of the card he would like to play in this round
     * in order to eventually add this card in the list of all the Assistant cards
     *
     * @param player of type Player - the player which Assistant card will be added to the list with other players' cards
     * @return result of type boolean - true if the Assistant card chosen by the player is correctly added to the list
     *                                   false if the player has to choose another Assistant card
     */
    public boolean chooseAssistants(Player player, int index) throws InvalidIndexException {
        boolean result = false;
        Assistant assistant = player.chooseAssistant(index);
        if(lastAssistants.isEmpty()){
            result = true;
        }
        else{
            for (Assistant a : lastAssistants){
                if ( a.compareTo(assistant) != 0 || lastAssistants.containsAll(player.getMyCards().getCards()))
                    result = true;
            }
        }
        if(result){
            lastAssistants.add(assistant);
            player.setLastAssist(assistant);
            player.getMyCards().removeCard(assistant);
        }
        //Check if any Player has used all cards, if so the game must end after the current round
        for(Player p: players)
            if(p.getNumCards() == 0)
                gameMustEnd = true;

        return result;
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
     * Method getFirstPlayer is used for sorting players by their card value that determines the turn order
     * and returns the first player of the next round
     * @return first of type int - the first player of the next round
     */
    public int getFirstPlayer(){
        int first = -1;
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        for(Player p : players) sortedPlayers.add(p);

        Collections.sort(sortedPlayers, (p1, p2) -> {
            return p1.getLastAssistant().compareTo(p2.getLastAssistant());
        });
        first = players.indexOf(sortedPlayers.get(0));
        return first;
    }

    /** Method nextPlayer skips to the next player and sets him as Active player */
    public void nextPlayer(){
        Player nextPlayer = null;
        Player activePlayer = getActivePlayer();
        if (activePlayer == null) nextPlayer = players.get(getFirstPlayer());
        else if (players.indexOf(activePlayer) == players.size()-1) nextPlayer = players.get(0);
        else nextPlayer = players.get(players.indexOf(activePlayer) + 1);

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
        int numS = numPlayers==3? NS : NUM_STUDENTS;
        int numT = numPlayers==3? NT : NUM_TOWERS;
        Player p = new Player(nickname, color, mage, numT);
        for (int i = 0; i < numS; i++) {
            ColorS s = container.draw();
            p.getMyBoard().getEntrance().add(s);
        }
        players.add(p);
    }

    /**
     * Method Studentcontainer returns the container with pawns student
     * @return container of type StudentContainer
     */
    public StudentContainer getContainer(){
        return container;
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
     * @param from of type AcceptTower - the place from which the tower is moved
     * @param to of type AcceptTower - where the tower is mowed
     */
    public void moveTower(ColorT t, AcceptTower from, AcceptTower to){
        from.remove(t);
        to.add(t);
    }

    /**
     * Method getPlayers returns the list of the players taking part in the game.
     * @return List<Player> - list containing all the players
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Method moveStudent allows to move the pawns student
     * @param s of type ColorS - the student that has to be moved
     * @param from of type CanRemoveStudent - place from which the student is relocated
     * @param to of type CanAcceptStudent - place where the student is shifted
     */
    public void moveStudent(ColorS s, CanRemoveStudent from, CanAcceptStudent to){
        from.remove(s);
        to.add(s);
    }

    /**
     * method moveMN checks if the move is legal, then if there isn't noEntryTiles on the arrival Island
     * it calculates the influence on that island and in necessary change the owner of the island and join the Island.
     * @param numMNSteps int - number of steps that Mother Nature want to do.
     */
    public void moveMN(int numMNSteps) throws InvalidMNStepsException {
        if(numMNSteps > activePlayer.getMNSteps()) {
            throw new InvalidMNStepsException();
        }
        Island island = world.moveMN(numMNSteps);
        checkIsland(island);
    }

    /**
     * Examines and Island and if necessary changes the owner
     * @param island the Island to check
     */
    public void checkIsland(Island island){
        Optional<Player> nextOwner = world.checkConquest(world.getInfluenceIsland(island, profs, players), players);
        nextOwner.ifPresent(owner -> {conquest(owner, island);});
        world.checkJoin(world.getIslandByIndex(world.getMNPosition()));
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
            oldOwner.ifPresent(owner -> {moveTower(owner.getColorTower(), island, owner.getMyBoard()); });
            moveTower(nextOwner.getColorTower(), nextOwner.getMyBoard(), island);
        }
    }

    /**
     * Fills Clouds with the correct Number of Students
     */
    public void newClouds(){
        int numStudents = numPlayers%2==0 ? 3 : 4; // 2 or 4 Players -> 3 Students, 3 Players -> 4 Students per Cloud
        for(Cloud c: clouds)
            for(int i=0;i<numStudents; i++)
                if(container.canDraw())
                    c.add(container.draw());
                else
                    gameMustEnd = true;
    }

    /**
     * Gets the selected Cloud
     * @param i the Cloud's index
     * @return the selected Cloud
     */
    public Cloud getCloudByIndex(int i){
        return clouds.get(i);
    }

    /**
     * Method getWorld returns World object with twelve islands in it.
     * @return world of type World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Method getProfs returns the Map in which every player is paired with the professor on whom he exercises his control
     * @return profs of type Map - the professors controlled by different players
     */
    public HashMap<ColorS, Player> getProfs() {
        return profs;
    }

    /**
     * Method setStrategy allows to set the strategy that determines which player takes the control on the professors
     * @param strategy of type ProfStrategy - the strategy determined by che chosen Character card
     */
    @Override
    public void setStrategy(ProfStrategy strategy){
        this.strategy=strategy;
    }

    /**
     * Method setStrategy allows to switch the strategy depending on the Character card chosen by the player
     */
    @Override
    public void resetStrategy() {
        strategy=new StandardProf();
    }

    /**
     * Method getStrategy gets the strategy that determines which player takes the control over the professors
     * @return strategy of type ProfStrategy  - the strategy determined by che chosen Character card
     */
    @Override
    public ProfStrategy getStrategy() {
        return strategy;
    }

    /**
     * Checks if the game must end after this round
     * @return true if the game is in its last round
     */
    public boolean checkGameMustEnd() {
        return gameMustEnd;
    }

    /**
     * Method checkWin checks if any winning condition is verified and in case return the Winner, game can end in a draw when two players
     * has the same towers on the World and the same number of profs.
     * @return Optional<Player> the winner of the game, if presents.
     */
    //Remember that there are two different case of null return value: if the first condition is verified the game ends in a draw, otherwise
    //there the game must continue.
    public Optional<Player> checkWin() {
        if(world.getSize() == 3 || gameMustEnd) {
            Player playerWinning = players.get(0);
            Optional<Player> winner = Optional.of(playerWinning);
            for(Player p : players) {
                if (p.getMyBoard().getTowers().size() < playerWinning.getMyBoard().getTowers().size()) {
                    playerWinning = p;
                    winner = Optional.of(p);
                } else if(p.getMyBoard().getTowers().size() == playerWinning.getMyBoard().getTowers().size() && !p.equals(playerWinning)) {
                    int countWinnerProfs = 0;
                    int countPlayerProfs = 0;
                    for(ColorS c: ColorS.values()) {
                        if(profs.get(c) != null && profs.get(c).equals(p)) {
                            countPlayerProfs++;
                        } else if(profs.get(c) != null && profs.get(c).equals(playerWinning)) {
                            countWinnerProfs++;
                        }
                    }
                    if(countPlayerProfs > countWinnerProfs) {
                        playerWinning = p;
                        winner = Optional.of(p);
                    } else if(countPlayerProfs == countWinnerProfs) {
                        winner = Optional.empty();
                    }
                }
            }
            return winner;
        } else if(players.stream().map(player -> {return player.getMyBoard().getTowers().size();}).anyMatch(num -> num == 0)) {
            return players.stream().filter(player -> player.getMyBoard().getTowers().size() == 0).findFirst();
        }
        return Optional.empty();
    }

    /**
     * Method checkProfs checks if any prof has to change the owner and in case apply these changes, calling the method checkProfs
     * of the ProfStrategy.
     */
    public void checkProfs() {
        profs = strategy.checkProfs(players, profs);
    }
}