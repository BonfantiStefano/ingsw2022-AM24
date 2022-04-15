package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

public class GameParams implements Request{
    private int numPlayers;
    private boolean expert;
    private String nickname;
    private Mage mage;
    private ColorT colorT;

    public ColorT getColorT() {
        return colorT;
    }

    public void setColorT(ColorT colorT) {
        this.colorT = colorT;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Mage getMage() {
        return mage;
    }

    public void setMage(Mage mage) {
        this.mage = mage;
    }
}
