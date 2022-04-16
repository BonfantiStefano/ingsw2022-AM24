package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

public class Join{
    private String nickname;
    private Mage mage;
    private ColorT colorT;

    public Join(String nickname, Mage mage, ColorT colorT) {
        this.nickname = nickname;
        this.mage = mage;
        this.colorT = colorT;
    }

    public ColorT getColorT() { return colorT; }

    public String getNickname() {
        return nickname;
    }

    public Mage getMage() {
        return mage;
    }
}
