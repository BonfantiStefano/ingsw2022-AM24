package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.mnstrategy.MNStandard;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Player class represents the Player and contains all the information about him:
 * his nickname, the magical school board he received in the beginning of the game, the color for the towers
 * that he will build, his Assistant cards and in particular the last one he played, the amount of coins he owns etc.
 */
public class Player implements PlayerInterface {
    private final String nickname;
    private final SchoolBoard myBoard;
    private final Hand myCards;
    private final ColorT color;
    private boolean isPlaying;
    private Assistant lastAssist;
    private MNStrategy strategy;
    private int coins;
    private boolean isConnected;
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);


    /**
     * Constructor Player creates a new Player instance.
     *
     * @param nickname of type String - the nickname of the player
     * @param color of type ColorT - the color of pawns Tower in player's SchoolBoard
     * @param numTowers of type ColorT - the color of pawns Student in player's SchoolBoard
     */
    public Player(String nickname, ColorT color, Mage mage, int numTowers){
        this.nickname=nickname;
        this.color=color;
        this.myBoard=new SchoolBoard(color, numTowers);
        this.lastAssist=null;
        this.isPlaying=false;
        this.myCards=new Hand(mage);
        this.coins=0;
        this.strategy = new MNStandard();
        this.isConnected = true;
    }

    /**
     * Method addListener is used in order to register an event listener
     * @param gameBoard - event listener that is used for receiving the events
     */
    public void addListener(PropertyChangeListener gameBoard){
        listener.addPropertyChangeListener(gameBoard);
    }

    /**
     * Method getColorTower returns the color of the Towers assigned to the player.
     * @return the ColorT.
     */
    public ColorT getColorTower() {
        return color;
    }

    /**
     * According to the active MNStrategy checks the steps value in lastAssist
     * @return the number of steps MN can make
     */
    public int getMNSteps(){
        return  strategy.mnSteps(lastAssist);
    }

    /**
     * Method chooseAssistant returns the Assistant card chosen by the player
     *
     * @param index of type int - the index of the card
     * @return card of type Assistant - the card selected by the player
     */
    public Assistant chooseAssistant(int index) throws InvalidIndexException {
        return myCards.getCard(index);
    }

    /**
     * Sets the Player's status
     * @param connected true if the Player is connected
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Check the Player's connection status
     * @return the Player's connection status
     */
    public boolean isConnected(){
        return isConnected;
    }
    /**
     * Method setLastAssistant sets the last Assistant card chosen by the player
     *
     * @param card of type Assistant - the chosen card.
     */
    public void setLastAssist(Assistant card){
        lastAssist = card;
    }

    /**
     * Method getLastAssistant returns the last Assistant card chosen by the player.
     * @return lastAssistant of type Assistant
     */
    public Assistant getLastAssistant(){
        return lastAssist;
    }

    /**
     * Method getMyCards returns the player's Assistant cards.
     * @return myCards of type Hand
     */
    public Hand getMyCards(){
        return myCards;
    }

    /**
     * Method getNumCards returns the number of the cards owned by the player.
     * @return int
     */
    public int getNumCards(){
        return myCards.numCards();
    }

    /**
     * Method getMage returns the type of the Mage owned by the player.
     * @return Mage
     */
    public Mage getMage(){
        return myCards.getMage();
    }

    /**
     * Method getNickname returns the nickname of the player.
     * @return nickname of type String
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Method setPlaying allows the change the activePlayer.
     * @param playing of type boolean - true if the player will become the activePlayer, false otherwise.
     */
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * Method isPlaying gives the information if the Player is the activePlayer.
     * @return  isPlaying of type boolean - true if the player is playing, false otherwise.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Method getSchoolBoard returns the School Board of the player
     * @return myBoard of type SchoolBoard - the school board that the player owns
     */
    public SchoolBoard getMyBoard(){
        return myBoard;
    }

    /**
     * Method setStrategy allows to set the strategy which is related to Mother Nature pawn
     * @param strategy of type ProfStrategy - the strategy determined by che chosen Character card
     */
    @Override
    public void setStrategy(MNStrategy strategy) {
        this.strategy=strategy;
    }

    /**
     * Method setStrategy allows to switch the strategy depending on the Character card chosen by the player
     */
    @Override
    public void resetStrategy() {
        strategy=new MNStandard();
    }

    /**
     * Method getStrategy gets the strategy that determines the number of the steps of Mother Nature pawn
     * @return strategy of type MNStrategy  - the strategy determined by che chosen Character card
     */
    @Override
    public MNStrategy getStrategy() {
        return strategy;
    }

    /**
     * Method getCoins returns the amount of player's coins
     * @return coins of type int
     */
    public int getCoins(){
        return coins;
    }

    /**
     * Method setCoins changes the amount of player's coins
     * @param amount of type int - coins added (or subtracted) to the previous ones
     */
    public void setCoins(int amount){
        coins = coins + amount;
        listener.firePropertyChange(String.valueOf(EVENT.PLAYER_COINS), null, this);
    }

    /**
     * Get the number of Students of a particular Color in the Player's Hall
     * @param c the chosen ColorS
     * @return the number of Students of that Color in the Player's Hall
     */
    public int getHall(ColorS c){
        return myBoard.getHall().get(c);
    }

    /**
     * Method equals is overridden in order to compare two objects Player
     * @param o of type Object
     * @return boolean - true if they are the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;

        return getNickname().equals(player.getNickname());
    }

    /**
     * Method hashCode is overridden in order to calculate the hash value of the Player
     * @return int - the hash value of the Player
     */
    @Override
    public int hashCode() {
        return getNickname().hashCode();
    }
}
