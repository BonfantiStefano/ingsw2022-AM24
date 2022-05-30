package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.profstrategy.StandardProf;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.World;
import it.polimi.ingsw.server.virtualview.VirtualCloud;
import it.polimi.ingsw.server.virtualview.VirtualIsland;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
public class GameBoard implements HasStrategy<ProfStrategy>, Model, PropertyChangeListener {

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

    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

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
        world.addListener(this);
        this.numPlayers = numPlayers;
        this.gameMustEnd = false;
        profs=new HashMap<>();
        for(ColorS c:ColorS.values()){
            profs.put(c,null);
        }
        clouds = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) {
            clouds.add(new Cloud());
        }
        clouds.forEach(cloud -> cloud.addListener(this));
    }

    /**
     * Method addListener is used in order to register an event listener.
     * This method is also used to initialize the VirtualView at the beginning of the game.
     * @param controller - event listener that is used for receiving the events
     */
    public void addListener(PropertyChangeListener controller){
        listener.addPropertyChangeListener(controller);
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_WORLD), null, world.createVirtualWorld());
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_CLOUDS), null, createVirtualClouds());
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_PLAYERS), null, createVirtualPlayers());
        listener.firePropertyChange(String.valueOf(EVENT.MN_POS), null, world.getMNPosition());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_PROFS), null, profs);
    }


    /**
     * Method getNumPlayers returns the number of the players taking part in the game.
     * @return int - the number of the players in the game
     */
    public int getNumPlayers(){
        return players.size();
    }

    /**
     * Method chooseAssistants gets a player and the index of the card he would like to play in this round
     * in order to eventually add this card in the list of all the Assistant cards
     *
     * @param player of type Player - the player which Assistant card will be added to the list with other players' cards
     * @return result of type boolean - true if the Assistant card chosen by the player is correctly added to the list
     *                                   false if the player has to choose another Assistant card
     * @throws InvalidIndexException - if the index position of the card doesn't exist in the player's hand
     */
    public boolean chooseAssistants(Player player, int index) throws InvalidIndexException {
        if(lastAssistants.size()==numPlayers)
            lastAssistants = new ArrayList<>();
        boolean result = false;
        Assistant assistant = player.chooseAssistant(index);

        ArrayList<Integer> assistants = new ArrayList<>();
        ArrayList<Integer> cards = new ArrayList<>();
        for(Assistant a: lastAssistants)
            assistants.add(a.getTurn());
        for(Assistant c: player.getMyCards().getCards())
            cards.add(c.getTurn());
        if(assistants.containsAll(cards))
            result = true;
        if(lastAssistants.isEmpty()){
            result = true;
        }
        if(!assistants.contains(assistant.getTurn()))
            result = true;

        if(result){
            lastAssistants.add(assistant);
            player.setLastAssist(assistant);
            player.getMyCards().removeCard(assistant);

            int indexPlayer = players.indexOf(player);
            VirtualPlayer virtualPlayer = new VirtualPlayer(player);
            listener.firePropertyChange(String.valueOf(EVENT.REPLACE_PLAYER), indexPlayer, virtualPlayer);
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
    @Override
    public int getFirstPlayer() {
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        for(Player p : players) {
            if (p.getLastAssistant() != null) {
                sortedPlayers.add(p);
            }
        }
        sortedPlayers.sort((p1, p2) ->
            p1.getLastAssistant().compareTo(p2.getLastAssistant()));
        return !sortedPlayers.isEmpty() ? players.indexOf(sortedPlayers.get(0)) : 0;
    }



    /**
     * Method getSortedPlayers returns the list containing sorted players
     * @return sortedPlayers of type ArrayList<Player> - the sorted players
     */
    @Override
    public ArrayList<Player> getSortedPlayers(){
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        if(lastAssistants.isEmpty()) {
            sortedPlayers.addAll(players);
        } else {
            Player firstPlayer = players.get(getFirstPlayer());
            int indexFirstPlayer = players.indexOf(firstPlayer);
            sortedPlayers.add(firstPlayer);
            for (int i = 1; i < players.size(); i++)
                sortedPlayers.add(players.get((indexFirstPlayer + i) % numPlayers));
        }
        return sortedPlayers;
    }

    /** Method nextPlayer skips to the next player and sets him as Active player */
    @Override
    public void nextPlayer(boolean newRound){
        Player nextPlayer;
        Player activePlayer = getActivePlayer();
        if (newRound || activePlayer == null) nextPlayer = players.get(getFirstPlayer());
        else if (players.indexOf(activePlayer) == players.size()-1) nextPlayer = players.get(0);
        else nextPlayer = players.get(players.indexOf(activePlayer) + 1);

        setActivePlayer(nextPlayer);
    }

    /**
     * Method addPlayer adds a new player
     * @param player of type Player
     */
    public void addPlayer(Player player) {
        listener.firePropertyChange(String.valueOf(EVENT.ADD_PLAYER), null, player);
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
    @Override
    public void addPlayer(String nickname, ColorT color, Mage mage) {
        int numS = numPlayers==3? NS : NUM_STUDENTS;
        int numT = numPlayers==3? NT : NUM_TOWERS;
        Player p = new Player(nickname, color, mage, numT);
        listener.firePropertyChange(String.valueOf(EVENT.ADD_PLAYER), null, p);
        p.addListener(this);
        p.getMyBoard().addListener(this);
        for (int i = 0; i < numS; i++) {
            ColorS s = container.draw();
            p.getMyBoard().getEntrance().add(s);
        }
        players.add(p);
    }

    /**
     * Method getContainer returns the container with pawns student
     * @return container of type StudentContainer
     */
    public StudentContainer getContainer(){
        return container;
    }

    /**
     * Method getActivePlayer returns the active player in this round
     * @return activePlayer of type Player
     */
    @Override
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
    public void moveTower(ColorT t, AcceptTower from, AcceptTower to) {
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
     * @throws NoSuchStudentException - if there is no students of the selected color to be removed
     */
    @Override
    public void moveStudent(ColorS s, CanRemoveStudent from, CanAcceptStudent to) throws NoSuchStudentException {
        from.remove(s);
        to.add(s);
    }

    /**
     * method moveMN checks if the move is legal, then if there isn't noEntryTiles on the arrival Island
     * it calculates the influence on that island and in necessary change the owner of the island and join the Island.
     * @param numMNSteps int - number of steps that Mother Nature want to do.
     * @throws InvalidMNStepsException - if Mother Nature can't make the indicated number of steps.
     */
    @Override
    //Checks that the numMNSteps is between 1 and 7 are done by the controller.
    public void moveMN(int numMNSteps) throws InvalidMNStepsException {
        int oldWoldSize = world.getSize();
        System.out.println(activePlayer.getMNSteps());
        if(numMNSteps > activePlayer.getMNSteps()) {
            throw new InvalidMNStepsException();
        }
        Island island = world.moveMN(numMNSteps);
        checkIsland(island);
        int newWorldSize = world.getSize();
        if(newWorldSize != oldWoldSize){
            listener.firePropertyChange(String.valueOf(EVENT.CREATE_WORLD), null, world.createVirtualWorld());
            listener.firePropertyChange(String.valueOf(EVENT.MN_POS), null, world.getMNPosition());
        }

    }

    /**
     * Examines an Island and if necessary changes the owner
     * @param island the Island to check
     */
    public void checkIsland(Island island){
        if(world.checkEntry(island)) {
            Optional<Player> nextOwner = world.checkConquest(world.getInfluenceIsland(island, profs, players), players, island);
            nextOwner.ifPresent(owner -> conquest(owner, island));
            world.checkJoin(island);
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
            oldOwner.ifPresent(owner -> moveTower(owner.getColorTower(), island, owner.getMyBoard()));
            if(nextOwner.getMyBoard().getTowers().size() > 0) {
                moveTower(nextOwner.getColorTower(), nextOwner.getMyBoard(), island);
            }
        }
    }

    /**
     * Fills Clouds with the correct Number of Students
     */
    @Override
    public void newClouds() {
        int numStudents = numPlayers%2==0 ? 3 : 4; // 2 or 4 Players -> 3 Students, 3 Players -> 4 Students per Cloud
        for(Cloud c: clouds)
            if(c.getStudents().isEmpty()) {
                for (int i = 0; i < numStudents; i++)
                    if (container.canDraw()) {
                        c.add(container.draw());
                    } else
                        gameMustEnd = true;
            }
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_CLOUDS), null, createVirtualClouds());
    }

    /**
     * Sets the Player's connected status
     * @param nickname the Player's nickname
     * @param status the connection status
     */
    @Override
    public void setConnected(String nickname, boolean status){
        players.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().ifPresent(p -> p.setConnected(status));
    }

    /**
     * Resets the Model to be ready for the next round
     */
    @Override
    public void resetRound(){
        newClouds();
        for(Player p : players) {
            p.setPlaying(false);
            p.resetStrategy();
        }
        resetStrategy();
        world.resetStrategy();
    }

    /**
     * Performs all actions needed to reset the turn: sets all players as not playing and resets all strategies
     */
    public void resetTurn(){
        for(Player p : players) {
            p.setPlaying(false);
            p.resetStrategy();
        }
        resetStrategy();
        world.resetStrategy();
    }

    /**
     * Gets the selected Cloud
     * @param i the Cloud's index
     * @return the selected Cloud
     */
    @Override
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
    @Override
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
        } else if(players.stream().map(player -> player.getMyBoard().getTowers().size()).anyMatch(num -> num == 0)) {
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
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_PROFS), null, profs);
    }

    /**
     * Method getSchoolBoard returns the active player's School Board
     * @return SchoolBoard
     */
    @Override
    public SchoolBoard getSchoolBoard(){
        return activePlayer.getMyBoard();
    }

    /**
     * Method getSizeWorld returns the size of the World.
     * @return an Integer, that represents the number of the Islands.
     */
    @Override
    public int getSizeWorld() {
        return world.getSize();
    }

    /**
     * Method getGameMustEnd returns if the game must end at the finish of the round.
     * @return boolean - the value of GameMustEnd.
     */
    @Override
    public boolean getGameMustEnd() {
        return gameMustEnd;
    }

    /**
     * Method entranceToHall moves a Student from Entrance to Hall in the active player's School Board
     * @param s the color of the Student being moved
     * @throws PlaceFullException - if there is no space for the students of the selected color in the hall
     * @throws NoSuchStudentException - if the entrance is empty
     */
    @Override
    public void entranceToHall(ColorS s) throws PlaceFullException, NoSuchStudentException {
        activePlayer.getMyBoard().entranceToHall(s);
        checkProfs();
    }

    /**
     * Method getIslandByIndex returns the Island corresponding to the index given by parameter.
     * @param index int - The index of the Island.
     * @return an Island - the Island of the world corresponding to the index.
     */
    @Override
    public Island getIslandByIndex(int index) {
        return world.getIslandByIndex(index);
    }

    /**
     * Method propertyChange receives updates whenever the gameBoard's state changes and
     * communicates to the controller the received events.
     * @param evt - the received event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        EVENT event = EVENT.valueOf(evt.getPropertyName());
        switch (event) {
            case CHANGE_SCHOOLBOARD -> {
                int indexPlayer = -1;
                VirtualPlayer virtualPlayer = null;
                SchoolBoard modelBoard = ((SchoolBoard) evt.getNewValue());
                for (Player p : players)
                    if (p.getMyBoard().equals(modelBoard)) {
                        indexPlayer = players.indexOf(p);
                        virtualPlayer = new VirtualPlayer(p);
                    }
                checkProfs();
                listener.firePropertyChange(String.valueOf(EVENT.REPLACE_PLAYER), indexPlayer, virtualPlayer);
            }
            case PLAYER_COINS -> {
                Player modelPlayer = (Player) evt.getNewValue();
                int indexP = players.indexOf(modelPlayer);
                VirtualPlayer virtualP = new VirtualPlayer(modelPlayer);
                listener.firePropertyChange(String.valueOf(EVENT.REPLACE_PLAYER), indexP, virtualP);
            }
            case CHANGE_ISLAND -> {
                Island modelIsland = (Island) evt.getNewValue();
                int indexIsland = world.getIndexByIsland(modelIsland);
                VirtualIsland virtualIsland = new VirtualIsland(modelIsland);
                listener.firePropertyChange(String.valueOf(EVENT.REPLACE_ISLAND), indexIsland, virtualIsland);
            }
            case CHANGE_CLOUD -> {
                Cloud modelCloud = (Cloud) evt.getNewValue();
                int indexCloud = clouds.indexOf(modelCloud);
                VirtualCloud virtualCloud = new VirtualCloud(modelCloud);
                listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CLOUD), indexCloud, virtualCloud);
            }
            case CHANGE_MN_POS -> {
                int pos = (int) evt.getNewValue();
                listener.firePropertyChange(String.valueOf(EVENT.MN_POS), null, pos);
            }
            case CHANGE_WORLD -> {
                ArrayList<VirtualIsland> islands = (ArrayList<VirtualIsland>) evt.getNewValue();
                listener.firePropertyChange(String.valueOf(EVENT.CREATE_WORLD), null, islands);
            }
        }
    }

    /**
     * Method createVirtualClouds creates virtual clouds
     * @return virtualCharacters - virtual clouds
     */
    public ArrayList<VirtualCloud> createVirtualClouds(){
        ArrayList<VirtualCloud> virtualClouds = new ArrayList<>();
        clouds.forEach(cloud -> virtualClouds.add(new VirtualCloud(cloud)));
        return virtualClouds;
    }

    /**
     * Method createVirtualPlayers is used to create a simplified representation of the players
     * @return virtualPlayers - a simplified representation of the Players used in VirtualView
     */
    public ArrayList<VirtualPlayer> createVirtualPlayers(){
        ArrayList<VirtualPlayer> virtualPlayers = new ArrayList<>();
        players.forEach(player -> virtualPlayers.add(new VirtualPlayer(player)));
        return virtualPlayers;
    }


}