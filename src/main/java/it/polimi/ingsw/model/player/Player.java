package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.mnstrategy.MNStandard;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;

public class Player implements PlayerInterface {
    private String nickname;
    private SchoolBoard myboard;
    private Hand myCards;
    private ColorT color;
    private boolean isPlaying;
    private Assistant lastAssist;
    private MNStrategy strategy;
    private int coins;
    private Mage mage;

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
        this.mage = mage;
        this.myboard=new SchoolBoard(color, numTowers);
        this.lastAssist=null;
        this.isPlaying=false;
        this.myCards = new Hand(mage);
        this.strategy = new MNStandard();
    }

    /**
     * Constructor Player creates a new Player instance which represents the expert version of Player.
     *
     * @param nickname of type String - the nickname of the player
     * @param color of type ColorT - the color of pawns Tower in player's SchoolBoard
     * @param numTowers of type ColorT - the color of pawns Student in player's SchoolBoard
     * @param coins of type int - the coins gained by the player
     */
    public Player(String nickname, ColorT color, Mage mage, int numTowers, int coins){
        this.nickname=nickname;
        this.color=color;
        this.mage = mage;
        this.myboard=new SchoolBoard(color, numTowers);
        this.lastAssist=null;
        this.isPlaying=false;
        this.myCards=new Hand(mage);
        this.coins=coins;
        this.strategy = new MNStandard();
    }

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
     * Method chooseAssistant replace lastAssistant with the selected card and removes it from the
     * player's cards
     *
     * @param index of type int - the card selected by the player.
     */
    public void chooseAssistant(int index){
        try{
            Assistant card = myCards.getCard(index);
            lastAssist = card;
            myCards.removeCard(card);
        }catch (InvalidIndexException e){
            System.out.println(e.getMessage());
        }
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
        return this.mage;
    }

    /**
     * Method getNickname returns the nickname of the player.
     * @return nickname of type String
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Method setPlaying allows the player to join/quit the game
     * @param playing of type boolean - true if the player joins the game, false if he decides to quit
     */
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * Method isPlaying allows the player to join/quit the game
     * @return  isPlaying of type boolean - true if the player is playing, false otherwise
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Method getSchoolBoard returns the School Board of the player
     * @return myboard of type SchoolBoard
     */
    public SchoolBoard getMyBoard(){
        return myboard;
    }

    @Override
    public void setStrategy(MNStrategy strategy) {
        this.strategy=strategy;
    }

    @Override
    public void resetStrategy() {
        strategy=new MNStandard();
    }

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
     * Method setCoins changes the amount of palyer's coins
     * @param amount of type int - coins added (or subtracted) to the previous ones
     */
    public void setCoins(int amount){
        coins = coins + amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        return getNickname().equals(player.getNickname());
    }

    @Override
    public int hashCode() {
        return getNickname().hashCode();
    }
}
