package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithStudents;

/**
 * Class ReplaceCharacterStudents is used from the server to notify about a character's update to the client.
 */
public class ReplaceCharacterStudents implements Update{
    private final VirtualCharacterWithStudents virtualCharacterWithStudents;
    private final int index;

    /**
     * Constructor ReplaceCharacterStudents creates a new update message.
     * @param virtualCharacterWithStudents VirtualCharacterWithStudents - all the data of the character with students.
     * @param index int - the index of the character.
     */
    public ReplaceCharacterStudents(VirtualCharacterWithStudents virtualCharacterWithStudents, int index) {
        this.virtualCharacterWithStudents = virtualCharacterWithStudents;
        this.index = index;
    }

    /**
     * Method getVirtualCharacterWithStudents returns the VirtualCharacterWithStudents.
     * @return the VirtualCharacterWithStudents contained in the message.
     */
    public VirtualCharacterWithStudents getVirtualCharacterWithStudents() {
        return virtualCharacterWithStudents;
    }

    /**
     * Method getIndex returns the VirtualCharacterWithStudents' index.
     * @return the index of the VirtualCharacterWithStudents.
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
