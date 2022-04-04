package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;

import java.util.ArrayList;
import java.util.Optional;

public class ExpertGameBoard extends GameBoard {
    final private static int NUM_COINS = 20;
    final private static int NUM_TOWERS = 8;
    final private static int NUM_STUDENTS = 7;
    final private static int NT = 6;
    final private static int NS = 9;
    private Character activeCharacter;
    private ArrayList<Character> characters;
    int coins;

    /**Constructor ExpertGameBoard creates a new empty ExpertGameBoard instance with 20 coins.*/
    public ExpertGameBoard(int numPlayers){
        super(numPlayers);
        this.coins = NUM_COINS;
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
        if(getNumPlayers()==3){
            getPlayers().add(new Player(nickname, color, mage, NT, 0));
        }
        else{
            getPlayers().add(new Player(nickname, color, mage, NUM_TOWERS, 0));
        }
    }

    /**
     * Method entranceToHall moves a Student form Entrance to Hall in the active player's School Board
     * @param s the color of the Student being moved
     */
    public void entranceToHall(ColorS s){
        boolean result = getActivePlayer().getMyBoard().entranceToHall(s);
        if (result){
            getActivePlayer().setCoins(1);
            coins--;
        }
    }

    /**
     * Method hallToEntrance moves a Student from the Hall to the Entrance
     * @param s the color of the Student being moved
     */
    public void hallToEntrance(ColorS s){
        getActivePlayer().getMyBoard().hallToEntrance(s);
    }

    /**
     * Method addToHall adds a student directly to the Hall
     * @param s the color of the Student being added
     */
    public void addToHall(ColorS s){
        if(getActivePlayer().getMyBoard().addToHall(s)){
            getActivePlayer().setCoins(1);
            coins--;
        }
    }

    /**
     * Method add adds a Student to the Entrance of the player's SchoolBoard
     * @param s the color of the Student being added
     */
    public void add(ColorS s){
        getActivePlayer().getMyBoard().add(s);
    }

    /**
     * Method removeHall removes a Student directly from the player's Hall in his SchoolBoard
     * @param s the Student being removed
     */
    public void removeHall(ColorS s){
        getActivePlayer().getMyBoard().removeHall(s);
    }

    /**
     * Method playActiveCharacter updates the amount of coins that belongs to active player
     * and the ones that are in the expert GameBoard
     */
    public void playActiveCharacter(){
        activeCharacter.play();
        int cost = activeCharacter.getCost();
        coins = coins + cost;
        getActivePlayer().setCoins(-cost);
    }

    /**
     * Method checkIsland is utilized by the character whose effect is to calculate the influence on an Island as if Mother Nature were there.
     * @param island Island - the Island on which the influence has to be calculated.
     */
    public void checkIsland(Island island) {
        if(getWorld().checkEntry()) {
            Optional<Player> nextOwner = getWorld().checkConquest(getWorld().getInfluenceIsland(island, getProfs(), getPlayers()), getPlayers());
            nextOwner.ifPresent(owner -> {conquest(owner, island);});
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
    }

    /**
     * Method getAvailableCoins returns the available amount of coins in the game.
     * @return coins of type int
     */
    public int getAvailableCoins() {
        return coins;
    }


}
