package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

public class Join implements Request{
    private final String nickname;
    private final Mage mage;
    private final ColorT colorT;

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

    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
