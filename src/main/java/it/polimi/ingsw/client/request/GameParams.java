package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

public class GameParams implements Request {
    private int numPlayers;
    private boolean expert;
    private String nickname;
    private Mage mage;
    private ColorT colorT;

    public GameParams(int numPlayers, boolean expert, String nickname, Mage mage, ColorT colorT) {
        this.numPlayers = numPlayers;
        this.expert = expert;
        this.nickname = nickname;
        this.mage = mage;
        this.colorT = colorT;
    }

    public ColorT getColorT() {
        return colorT;
    }


    public int getNumPlayers() {
        return numPlayers;
    }


    public boolean isExpert() {
        return expert;
    }


    public String getNickname() {
        return nickname;
    }


    public Mage getMage() {
        return mage;
    }

}
