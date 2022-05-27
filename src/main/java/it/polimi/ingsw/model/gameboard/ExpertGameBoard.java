package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.EVENT;
import it.polimi.ingsw.model.ExpertModel;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.server.virtualview.VirtualCharacter;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithNoEntry;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithStudents;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class ExpertGameBoard offers methods to handle a game with expert rules
 */
public class ExpertGameBoard extends GameBoard implements ExpertModel {
    final private static int NUM_COINS = 20;
    private Character activeCharacter;
    private ArrayList<Character> characters;
    private CharacterFactory factory;
    int coins;

    /**Constructor ExpertGameBoard creates a new empty ExpertGameBoard instance with 20 coins.*/
    public ExpertGameBoard(int numPlayers){
        super(numPlayers);
        this.coins = NUM_COINS;
        factory=createFactory();
        characters=new ArrayList<>();
        for(int i=0;i<3;i++){
            characters.add(factory.createCharacter());
        }
    }

    /**
     * Method addListener is used in order to register an event listener
     * @param expertController - event listener that is used for receiving the events
     */
    @Override
    public void addListener(PropertyChangeListener expertController){
        super.addListener(expertController);
        characters.forEach(character -> character.addListener(this));
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_CHARACTERS), null, createVirtualCharacters());
    }

    /**
     * Method addPlayer creates and adds a new player.
     * The new player is created considering the fact that the number of pawns such as students and towers
     * in his schoolBoard depends on the number of the players taking part in the game.
     * @param nickname of type String
     * @param color of type ColorT
     * @param mage of type Mage
     */
    @Override
    public void addPlayer(String nickname, ColorT color, Mage mage) {
        super.addPlayer(nickname, color, mage);
        getPlayerByNickname(nickname).setCoins(1);
        coins--;
        listener.firePropertyChange(String.valueOf(EVENT.BOARD_COINS), null, coins);
        factory.setPlayers(players.stream().map(p -> (PlayerInterface) p).collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Method entranceToHall moves a Student from Entrance to Hall in the active player's School Board and checks if the
     * player needs to earn a coins.
     * @param s the color of the Student being moved
     * @throws PlaceFullException - if there is no space for the students of the selected color in the hall
     * @throws NoSuchStudentException - if the entrance is empty
     */
    @Override
    public void entranceToHall(ColorS s) throws PlaceFullException, NoSuchStudentException {
        boolean result = activePlayer.getMyBoard().entranceToHall(s);
        if (result&&coins>0){
            activePlayer.setCoins(1);
            coins--;
            listener.firePropertyChange(String.valueOf(EVENT.BOARD_COINS), null, coins);
        }
        checkProfs();
    }

    /**
     * Method hallToEntrance moves a Student from the Hall to the Entrance
     * @param s the color of the Student being moved
     * @throws NoSuchStudentException if there is no students of the selected color in the hall
     */
    @Override
    public void hallToEntrance(ColorS s) throws NoSuchStudentException {
        activePlayer.getMyBoard().hallToEntrance(s);
        checkProfs();
    }

    /**
     * Method addToHall adds a student directly to the Hall
     * @param s the color of the Student being added
     * @throws PlaceFullException - if there is no space for the students of the selected color in the hall
     */
    @Override
    public void addToHall(ColorS s) throws PlaceFullException {
        if(activePlayer.getMyBoard().addToHall(s)){
            activePlayer.setCoins(1);
            coins--;
            listener.firePropertyChange(String.valueOf(EVENT.BOARD_COINS), null, coins);
        }
        checkProfs();
    }

    /**
     * Method switchStudents exchanges two students between hall and entrance
     * @param hallS - student moved from hall to entrance
     * @param entranceS - student moved from entrance to hall
     * @throws PlaceFullException - if there is no space for the students of the selected color in the hall
     * @throws NoSuchStudentException if there is no students of the selected color in the hall
     * @throws NoSuchStudentException if there is no students of the selected color in the entrance
     */
    @Override
    public void switchStudents(ColorS hallS, ColorS entranceS) throws PlaceFullException, NoSuchStudentException {
        if(activePlayer.getMyBoard().entranceToHall(entranceS)){
            if(coins > 0) {
                activePlayer.setCoins(1);
                coins--;
                listener.firePropertyChange(String.valueOf(EVENT.BOARD_COINS), null, coins);
            }
        }
        activePlayer.getMyBoard().hallToEntrance(hallS);
        checkProfs();
    }

    /**
     * Method removeHall removes three students of the chosen color from the player's Hall and puts them in the bag.
     * If any player has fewer than three students of that color, all the students of that color are put back in the bag
     * @param s the Student being removed
     */
    @Override
    public void removeHall(ColorS s){
        int num;
        for(Player p : getPlayers()){
            num = p.getMyBoard().getHall().get(s);
            if(num >=3 ){
                p.getMyBoard().getHall().put(s, num-3);
                for (int i = 0; i < 3; i++) {
                    getContainer().addStudent(s);
                }
            }
            else{
                p.getMyBoard().getHall().put(s, 0);
                for (int i = 0; i < num; i++) {
                    getContainer().addStudent(s);
                }
            }
        }
        checkProfs();
    }

    /**
     * Method playActiveCharacter updates the amount of coins that belongs to active player
     * and the ones that are in the expert GameBoard
     * @param c of type Character - the picked Character card
     * @throws NotEnoughCoinsException - if the player hasn't enough coins to play the picked Character card
     */
    @Override
    public void playCharacter(Character c) throws NotEnoughCoinsException {
        Character characterToPlay = findChar(c);
        if(activePlayer.getCoins() >= characterToPlay.getCost()) {
            if(findChar(c)!= null) {
                activePlayer.setCoins(-characterToPlay.getCost());
                coins += characterToPlay.isAlreadyPlayed()? characterToPlay.getCost():(characterToPlay.getCost()-1);
                setActiveCharacter(characterToPlay);
                listener.firePropertyChange(String.valueOf(EVENT.BOARD_COINS), null, coins);
                int indexActiveCharacter = characters.indexOf(activeCharacter);
                listener.firePropertyChange(String.valueOf(EVENT.ACTIVE_CHARACTER), null, indexActiveCharacter);
            }
        }
        else
            throw new NotEnoughCoinsException();
    }

    /**
     * Method findChar returns the Character in the list of Characters in game matching the parameter's description
     * @param c the Character to search
     * @return Character found in the characters list
     */
    private Character findChar(Character c){
        return characters.stream().filter(character -> character.getDescription().equals(c.getDescription())).findFirst().orElse(null);
    }

    /**
     * Method checkIsland is utilized by the character whose effect is to calculate the influence on an Island as if Mother Nature were there.
     * @param island Island - the Island on which the influence has to be calculated.
     */
    @Override
    public void checkIsland(Island island) {
        if(world.checkEntry(island)) {
            Optional<Player> nextOwner = world.checkConquest(world.getInfluenceIsland(island, profs, players), players, island);
            nextOwner.ifPresent(owner -> conquest(owner, island));
            world.checkJoin(island);
        }
        else{
            resetNoEntryCharacter();
        }
    }

    /**
     * Method getCharacters returns the Character cards.
     * @return characters of type ArrayList<Character>
     */
    @Override
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * Method getActiveCharacter returns the Character card chosen in this round
     * by the active Player
     * @return activeCharacter of type Character
     */
    @Override
    public Character getActiveCharacter() {
        return activeCharacter;
    }

    /**
     * Method setActiveCharacter replace the last Character card with a new one.
     * @param activeCharacter of type Character
     */
    public void setActiveCharacter(Character activeCharacter) {
        this.activeCharacter = activeCharacter;
        if(activeCharacter!=null){
            activeCharacter.play();
        }
    }

    /**
     * Resets the Model to be ready for the next turn
     */
    @Override
    public void resetTurn() {
        super.resetTurn();
        int indexActiveCharacter = characters.indexOf(activeCharacter);
        if(indexActiveCharacter != -1 )
            listener.firePropertyChange(String.valueOf(EVENT.NO_ACTIVE_CHARACTER), null, indexActiveCharacter);
        setActiveCharacter(null);
    }

    /**
     * Method getAvailableCoins returns the available amount of coins in the game.
     * @return coins of type int
     */
    public int getAvailableCoins() {
        return coins;
    }

    /**
     * Creates a new CharacterFactory with the required parameters
     * @return factory a new CharacterFactory
     */
    private CharacterFactory createFactory(){
        ArrayList<PlayerInterface> players= this.players.stream().map(p -> (PlayerInterface) p).collect(Collectors.toCollection(ArrayList::new));
        factory = new CharacterFactory(world, this, container, players);
        return factory;
    }

    /**
     * method moveMN checks if the move is legal, then if there isn't noEntryTiles on the arrival Island
     * it calculates the influence on that island and in necessary change the owner of the island and join the Island.
     * @param numMNSteps int - number of steps that Mother Nature want to do.
     * @throws InvalidMNStepsException - if Mother Nature can't make the indicated number of steps
     */
    @Override
    public void moveMN(int numMNSteps) throws InvalidMNStepsException {
        int initSize = world.getSize();
        if(numMNSteps > activePlayer.getMNSteps()) {
            throw new InvalidMNStepsException();
        }
        Island island = world.moveMN(numMNSteps);
        checkIsland(island);
        int finalSize = world.getSize();
        if(initSize != finalSize){
            listener.firePropertyChange(String.valueOf(EVENT.CREATE_WORLD), null, world.createVirtualWorld());
        }
        listener.firePropertyChange(String.valueOf(EVENT.MN_POS), null, world.getMNPosition());

    }

    /**
     * Adds a new Student to the Character after it's played
     * @throws ClassCastException if the activeCharacter isn't
     */
    @Override
    public void resetCharacterStudent() throws ClassCastException {
        CharacterWithStudent c = (CharacterWithStudent) activeCharacter;
        c.add(container.draw());
    }

    /**
     * Adds a NoEntry tile on the CharacterWithNoEntry
     */
    @Override
    public void resetNoEntryCharacter(){
        CharacterWithNoEntry c = (CharacterWithNoEntry) findChar(new Character(CharacterDescription.CHAR5.getCost(), CharacterDescription.CHAR5.getDesc()));
        if(c!=null)
            c.resetNoEntry();
    }

    /**
     * Removes a NoEntry tile on the CharacterWithNoEntry
     */
    @Override
    public void removeNoEntry() {
        CharacterWithNoEntry c = (CharacterWithNoEntry) findChar(new Character(CharacterDescription.CHAR5.getCost(), CharacterDescription.CHAR5.getDesc()));
        if(c!=null)
            c.removeNoEntry();
    }

    /**
     * Method setBannedColor invokes setBannedColorS method of class World
     * @param color of type ColorS - The color used in getInfluenceIsland.
     */
    @Override
    public void setBannedColor(ColorS color){
        world.setBannedColorS(color);
    }

    /**
     * Method getInfluence invokes getInfluence method of class World
     * @param i of type Island - The island where the influence is calculated
     */
    public HashMap<Player, Integer> getInfluence(Island i){
        return getWorld().getInfluenceIsland(i, getProfs(), getPlayers());
    }

    /**
     * Method propertyChange receives updates whenever the expert gameBoard's state changes and
     * communicates to the controller the received events.
     * @param evt - the received event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt){
        super.propertyChange(evt);
        EVENT event = EVENT.valueOf(evt.getPropertyName());
        switch (event) {
            case CHANGE_CHARACTER_NE -> {
                CharacterWithNoEntry modelCard = (CharacterWithNoEntry) evt.getNewValue();
                int indexCard = characters.indexOf(modelCard);
                VirtualCharacterWithNoEntry virtualCard = new VirtualCharacterWithNoEntry(modelCard);
                listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CHARACTER_NE), indexCard, virtualCard);
            }
            case CHANGE_CHARACTER_S -> {
                CharacterWithStudent modelC = (CharacterWithStudent) evt.getNewValue();
                int indexC = characters.indexOf(modelC);
                VirtualCharacterWithStudents virtualC = new VirtualCharacterWithStudents(modelC);
                listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CHARACTER_S), indexC, virtualC);
            }
            case CHARACTER_COST -> {
                Character modelCharacter = (Character) evt.getNewValue();
                int indexCharacter = characters.indexOf(modelCharacter);
                listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CHARACTER), indexCharacter, modelCharacter);
            }
        }

    }

    /**
     * Method createVirtualCharacters creates a simplified representation of the characters
     * @return virtualCharacters - virtual characters used in VirtualView
     */
    public ArrayList<VirtualCharacter> createVirtualCharacters(){
        ArrayList<VirtualCharacter> virtualCharacters = new ArrayList<>();
        for(Character character : characters){
            String desc = character.getDescription();
            if(desc.equals(CharacterDescription.CHAR1.getDesc()) ||
                   desc.equals(CharacterDescription.CHAR7.getDesc()) ||
                        desc.equals(CharacterDescription.CHAR11.getDesc()))
                virtualCharacters.add(new VirtualCharacterWithStudents(character));
            else if (desc.equals(CharacterDescription.CHAR5.getDesc()))
                virtualCharacters.add(new VirtualCharacterWithNoEntry(character));
            else virtualCharacters.add(new VirtualCharacter(character));
        }
        return virtualCharacters;
    }

    /**
    * Method set is used for replace one of the available Character cards
     */
    public void set(int index, Character c){
        characters.set(index, c);
        characters.get(index).addListener(this);
    }

}