package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.character.CharacterDescription;

/**
 * PlayCharacter class is a Request used for requesting to play a character.
 * @see Request
 */
public class PlayCharacter implements Request {
    private final CharacterDescription c;

    /**
     * Constructor PlayCharacter creates a new PlayCharacter instance.
     * @param c CharacterDescription - the description of the character.
     */
    public PlayCharacter(CharacterDescription c) {
        this.c = c;
    }

    /**
     * Method getC returns the character's description.
     * @return CharacterDescription - character's description.
     */
    public CharacterDescription getC() {
        return c;
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