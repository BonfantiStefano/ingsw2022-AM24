package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

public class GameParams{
    private final int numPlayers;
    private final boolean expert;
    private final String nickname;
    private final Mage mage;
    private final ColorT colorT;

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
