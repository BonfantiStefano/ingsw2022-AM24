package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private SchoolBoard myboard;
    private ArrayList<Assistant> hand;
    private ColorT color;
    private int points;
    private boolean isPlaying;
    private Assistant lastAssist;

    public Player(String nickname, ColorT color){
        this.nickname=nickname;
        this.color=color;
        this.myboard=new SchoolBoard();
        this.points=0;
        this.lastAssist=null;
        this.isPlaying=false;
        this.hand=new ArrayList<Assistant>();
    }

    public void chooseAssist(){}

    public ArrayList<Prof> getMyProfs(){ return null;}

    public ArrayList<Assistant> getHand(){ return null;}

    public void playCard(){}

    public SchoolBoard getSchoolBoard() {
        return myboard;
    }

    public ColorT getColorTower() {
        return color;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
