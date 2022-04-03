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

    public Player(String nickname, ColorT color, Mage mage, int numStudents, int numTowers){
        this.nickname=nickname;
        this.color=color;
        this.mage = mage;
        this.myboard=new SchoolBoard(color,numStudents,numTowers);
        this.lastAssist=null;
        this.isPlaying=false;
        this.myCards = new Hand(mage);
    }

    public Player(String nickname, ColorT color, Mage mage, int numStudents, int numTowers, int coins){
        this.nickname=nickname;
        this.color=color;
        this.mage = mage;
        this.myboard=new SchoolBoard(color, numStudents, numTowers);
        this.lastAssist=null;
        this.isPlaying=false;
        this.myCards=new Hand(mage);
        this.coins=coins;
    }

    public void chooseAssistant(int index){
        try{
            Assistant card = myCards.getCard(index);
            lastAssist = card;
            myCards.removeCard(card);
        }catch (InvalidIndexException e){
            System.out.println(e.getMessage());
        }
    }

    public Assistant getLastAssistant(){
        return lastAssist;
    }

    public Hand getMyCards(){
        return myCards;
    }

    public int getNumCards(){
        return myCards.numCards();
    }

    public Mage getMage(){
        return this.mage;
    }

    public ColorT getColorTower() {
        return color;
    }

    public String getNickname(){
        return nickname;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

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

    public int getCoins(){
        return coins;
    }

    public void setCoins(int amount){
        coins = coins + amount;
    }

    public SchoolBoard getSchoolBoard(){
        return myboard;
    }

}
