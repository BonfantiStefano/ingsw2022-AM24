package it.polimi.ingsw.server.answer.Update;


import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithNoEntry;

/**
 * Class ReplaceCharacterWithNoEntry is used from the server to notify about a character's update to the client.
 */
public class ReplaceCharacterWithNoEntry implements Update{
    private final VirtualCharacterWithNoEntry characterWithNoEntry;
    private final int index;

    /**
     * Constructor ReplaceCharacterWithNoEntry creates a new update message.
     * @param characterWithNoEntry VirtualCharacterWithNoEntry - all the data of the character.
     * @param index int - the index of the character.
     */
    public ReplaceCharacterWithNoEntry(VirtualCharacterWithNoEntry characterWithNoEntry, int index) {
        this.characterWithNoEntry = characterWithNoEntry;
        this.index = index;
    }

    /**
     * Method getCharacterWithNoEntry returns the VirtualCharacterWithNoEntry.
     * @return the VirtualCharacterWithNoEntry contained in the message.
     */
    public VirtualCharacterWithNoEntry getCharacterWithNoEntry() {
        return characterWithNoEntry;
    }

    /**
     * Method getIndex returns the VirtualCharacterWithNoEntry's index.
     * @return the index of the VirtualCharacterWithNoEntry.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
