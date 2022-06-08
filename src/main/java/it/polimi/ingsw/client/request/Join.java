package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

/**
 * Join class is a Request used for requesting to join a lobby.
 * @see Request
 */
public class Join implements Request{
    private final String nickname;
    private final Mage mage;
    private final ColorT colorT;
    private final int index;

    /**
     * Constructor Join creates a new Join instance.
     * @param nickname String - the player's nickname.
     * @param mage Mage - the mage chosen by the player.
     * @param colorT ColorT - the color of the tower.
     * @param index int - the lobby's index.
     */
    public Join(String nickname, Mage mage, ColorT colorT, int index) {
        this.nickname = nickname;
        this.mage = mage;
        this.colorT = colorT;
        this.index = index;
    }

    /**
     * Method getColorT returns the color of the towers chosen.
     * @return ColorT - towers' color.
     */
    public ColorT getColorT() { return colorT; }

    /**
     * Method getNickname returns the player's nickname.
     * @return String - player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Method getMage returns the mage chosen.
     * @return Mage - mage chosen.
     */
    public Mage getMage() {
        return mage;
    }

    /**
     * Method getIndex returns the index of the lobby chosen.
     * @return int - lobby's index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Controller - the object that will handle the message.
     */
    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
