package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterFactory;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.world.Island;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class ExpertGameBoard offers methods to handle a game with expert rules
 */
public class ExpertGameBoard extends GameBoard {
    final private static int NUM_COINS = 20;
    final private static int NUM_TOWERS = 8;
    final private static int NUM_STUDENTS = 7;
    final private static int NT = 6;
    final private static int NS = 9;
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
     * The new player is created considering the fact that the number of pounds such as students and towers
     * in his schoolBoard depends on the number of the players taking part in the game.
     * @param nickname of type String
     * @param color of type ColorT
     * @param mage of type Mage
     */
    public void addPlayer(String nickname, ColorT color, Mage mage){
        super.addPlayer(nickname, color, mage);
        getPlayerByNickname(nickname).setCoins(1);
        coins--;
    }

    /**
     * Method entranceToHall moves a Student form Entrance to Hall in the active player's School Board
     * @param s the color of the Student being moved
     */
    public void entranceToHall(ColorS s)  {
        boolean result = false;
        try {
            result = activePlayer.getMyBoard().entranceToHall(s);
        } catch (PlaceFullException e) {
            e.getMessage();
        }
        if (result){
            activePlayer.setCoins(1);
            coins--;
        }
    }

    /**
     * Method hallToEntrance moves a Student from the Hall to the Entrance
     * @param s the color of the Student being moved
     */
    public void hallToEntrance(ColorS s){activePlayer.getMyBoard().hallToEntrance(s);}

    /**
     * Method addToHall adds a student directly to the Hall
     * @param s the color of the Student being added
     */
    public void addToHall(ColorS s) {
        try {
            if(activePlayer.getMyBoard().addToHall(s)){
                activePlayer.setCoins(1);
                coins--;
            }
        } catch (PlaceFullException e) {
            e.getMessage();
        }
    }

    /**
     * Method removeHall removes a Student directly from the player's Hall in his SchoolBoard
     * @param s the Student being removed
     */
    public void removeHall(ColorS s){
        int num;
        for(Player p : getPlayers()){
            num = p.getMyBoard().getHall().get(s);
            num = num >=3 ? num-3 : 0;
            p.getMyBoard().getHall().put(s, num);
        }
    }

    /**
     * Method playActiveCharacter updates the amount of coins that belongs to active player
     * and the ones that are in the expert GameBoard
     */
    public void playCharacter(Character c) throws NotEnoughCoinsException {
        if(activePlayer.getCoins()>=c.getCost()&&findChar(c)!=null) {
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
            Optional<Player> nextOwner = world.checkConquest(world.getInfluenceIsland(island, profs, players), players);
            nextOwner.ifPresent(owner -> {conquest(owner, island);});
            world.checkJoin(world.getIslandByIndex(world.getMNPosition()));
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


}
