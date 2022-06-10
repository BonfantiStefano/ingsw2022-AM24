package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCharacter;

/**
 * Class ReplaceCharacter is used from the server to notify about a character's update to the client.
 */
public class ReplaceCharacter implements Update{
    private final VirtualCharacter character;
    private final int index;

    /**
     * Constructor ReplaceCharacter creates a new update message.
     * @param character VirtualCharacter - all the data of the character.
     * @param index int - the index of the character.
     */
    public ReplaceCharacter(VirtualCharacter character, int index) {
        this.character = character;
        this.index = index;
    }

    /**
     * Method getCharacter returns the VirtualCharacter.
     * @return the virtualCharacter contained in the message.
     */
    public VirtualCharacter getCharacter() {
        return character;
    }

    /**
     * Method getIndex returns the VirtualCharacter's index.
     * @return the index of the VirtualCharacter.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
