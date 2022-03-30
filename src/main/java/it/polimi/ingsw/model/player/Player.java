package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.mnstrategy.MNStandard;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;

import java.util.Objects;

public class Player implements HasStrategy<MNStrategy> {
    private String nickname;
    private SchoolBoard myboard;
    private Hand myCards;
    private ColorT color;
    private boolean isPlaying;
    private Assistant lastAssist;
    private MNStrategy strategy;
    private int coins;

    public Player(String nickname, ColorT color, Mage mage, int numStudents, int numTowers){
        this.nickname=nickname;
        this.color=color;
        this.myboard=new SchoolBoard(color,numStudents,numTowers);
        this.lastAssist=null;
        this.isPlaying=false;
        this.myCards = new Hand(mage);
    }

    public Player(String nickname, ColorT color, Mage mage, int numStudents, int numTowers, int coins){
        this.nickname=nickname;
        this.color=color;
        this.myboard=new SchoolBoard(color, numStudents, numTowers);
        this.lastAssist=null;
        this.isPlaying=false;
        this.myCards=new Hand(mage);
        this.coins=coins;
    }

    public void chooseAssist(){}

    public void playCard(int index){
        Assistant card = myCards.getCard(index);
        lastAssist = card;
    }
    public Assistant getLastAssistant(){
        return lastAssist;
    }

    public Hand getMyCards(){
        return myCards;
    }

    public ColorT getColorTower() {
        return color;
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

    public int getCoins(){
        return coins;
    }

    public void setCoins(int amount){
        coins = coins + amount;
    }

    public String getNickname(){
        return nickname;
    }
}
