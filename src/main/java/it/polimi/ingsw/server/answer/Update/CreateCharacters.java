package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCharacter;

import java.util.ArrayList;

/**
 * Class CreateCharacters notifies the client about the game's characters.
 */
public class CreateCharacters implements Update{
    private final ArrayList<VirtualCharacter> characters;

    /**
     * Constructor CreateCharacters creates a new instance with the ArrayList given by parameter.
     * @param characters ArrayList - the List of all the Characters.
     */
    public CreateCharacters(ArrayList<VirtualCharacter> characters) {
        this.characters = characters;
    }

    /**
     * Method getCharacters returns the List of the Characters.
     * @return ArrayList - the List of the characters.
     */
    public ArrayList<VirtualCharacter> getCharacters() {
        return characters;
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
