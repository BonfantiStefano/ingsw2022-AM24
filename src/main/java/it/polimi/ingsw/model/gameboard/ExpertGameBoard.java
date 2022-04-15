package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.ExpertModel;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.World;

import java.util.ArrayList;
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
     * Method addPlayer creates and adds a new player.
     * The new player is created considering the fact that the number of pawns such as students and towers
     * in his schoolBoard depends on the number of the players taking part in the game.
     * @param nickname of type String
     * @param color of type ColorT
     * @param mage of type Mage
     */
    public void addPlayer(String nickname, ColorT color, Mage mage) {
        super.addPlayer(nickname, color, mage);
        getPlayerByNickname(nickname).setCoins(1);
        coins--;
    }

    /**
     * Method entranceToHall moves a Student form Entrance to Hall in the active player's School Board
     * @param s the color of the Student being moved
     * @throws PlaceFullException - if there is no space for the students of the selected color in the hall
     * @throws EmptyPlaceException - if the entrance is empty
     */
    public void entranceToHall(ColorS s) throws PlaceFullException, EmptyPlaceException {
        boolean result = false;
        result = activePlayer.getMyBoard().entranceToHall(s);
        if (result){
            activePlayer.setCoins(1);
            coins--;
        }
    }

    /**
     * Method hallToEntrance moves a Student from the Hall to the Entrance
     * @param s the color of the Student being moved
     * @throws EmptyPlaceException if there is no students of the selected color in the hall
     */
    public void hallToEntrance(ColorS s) throws EmptyPlaceException {
        activePlayer.getMyBoard().hallToEntrance(s);
    }

    /**
     * Method addToHall adds a student directly to the Hall
     * @param s the color of the Student being added
     * @throws PlaceFullException - if there is no space for the students of the selected color in the hall
     */
    public void addToHall(ColorS s) throws PlaceFullException {
        if(activePlayer.getMyBoard().addToHall(s)){
            activePlayer.setCoins(1);
            coins--;
        }
    }

    /**
     * Method switchStudents exchanges two students between hall and entrance
     * @param hallS - student moved from hall to entrance
     * @param entranceS - student moved from entrance to hall
     * @throws PlaceFullException - if there is no space for the students of the selected color in the hall
     * @throws EmptyPlaceException if there is no students of the selected color in the hall
     * @throws EmptyPlaceException if there is no students of the selected color in the entrance
     */
    public void switchStudents(ColorS hallS, ColorS entranceS) throws PlaceFullException, EmptyPlaceException {
            activePlayer.getMyBoard().entranceToHall(entranceS);
            activePlayer.getMyBoard().hallToEntrance(hallS);
    }

    /**
     * Method removeHall removes three students of the chosen color from the player's Hall and puts them in the bag.
     * If any player has fewer than three students of that color, all the students he owns are put back in the bag
     * @param s the Student being removed
     */
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
    }

    /**
     * Method playActiveCharacter updates the amount of coins that belongs to active player
     * and the ones that are in the expert GameBoard
     * @param c of type Character - the picked Character card
     * @throws NoSuchStudentException - if the player hasn't enough coins to play the picked Character card
     */
    public void playCharacter(Character c) throws NotEnoughCoinsException {
        if(activePlayer.getCoins() >= c.getCost() && findChar(c) != null) {
            activePlayer.setCoins(-c.getCost());
            setActiveCharacter(findChar(c));
            coins+=findChar(c).getCost();
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
    public void checkIsland(Island island) {
        if(world.checkEntry(island)) {
            Optional<Player> nextOwner = world.checkConquest(world.getInfluenceIsland(island, profs, players), players, island);
            nextOwner.ifPresent(owner -> conquest(owner, island));
            world.checkJoin(island);
        }
    }

    /**
     * Method getCharacters returns the Character cards.
     * @return characters of type ArrayList<Character>
     */
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * Method getActiveCharacter returns the Character card chosen in this round
     * by the active Player
     * @return activeCharacter of type Character
     */
    public Character getActiveCharacter() {
        return activeCharacter;
    }

    /**
     * Method setActiveCharacter replace the last Character card with a new one.
     * @param activeCharacter of type Character
     */
    public void setActiveCharacter(Character activeCharacter) {
        this.activeCharacter = activeCharacter;
        activeCharacter.play();
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
    public void moveMN(int numMNSteps) throws InvalidMNStepsException {
        if(numMNSteps > activePlayer.getMNSteps()) {
            throw new InvalidMNStepsException();
        }
        Island island = world.moveMN(numMNSteps);
        if(world.checkEntry(island)) {
            checkIsland(island);
        }
        else
            resetNoEntryCharacter();
    }

    /**
     * Adds a new Student to the Character after it's played
     * @throws ClassCastException if the activeCharacter isn't
     */
    public void resetCharacterStudent() throws ClassCastException {
        CharacterWithStudent c = (CharacterWithStudent) activeCharacter;
        c.add(container.draw());
    }

    /**
     * Adds a NoEntry tile on the CharacterWithNoEntry
     */
    public void resetNoEntryCharacter(){
        CharacterWithNoEntry c = (CharacterWithNoEntry) findChar(new Character(CharacterDescription.CHAR5.getCost(), CharacterDescription.CHAR5.getDesc()));
        if(c!=null)
            c.resetNoEntry();
    }
}
