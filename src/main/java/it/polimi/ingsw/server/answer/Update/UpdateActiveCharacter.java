package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;

/**
 * Class UpdateActiveCharacter is used from the server to notify about the active character to the client.
 */
public class UpdateActiveCharacter implements Update{
    private final int index;
    private final boolean active;

    /**
     * Constructor UpdateActiveCharacter creates a new update message.
     * @param index int - the index of the character.
     * @param active boolean - true if the character is active, false otherwise.
     */
    public UpdateActiveCharacter(int index, boolean active) {
        this.index = index;
        this.active = active;
    }

    /**
     * Method getIndex returns the Character's index.
     * @return the index of the Character.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Method isActive returns a boolean, true if the character is active, false otherwise.
     * @return a boolean, true if the character is active, false otherwise.
     */
    public boolean isActive() {
        return active;
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
