package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCharacter;

import java.util.ArrayList;

public class CreateCharacters implements Update{
    private final ArrayList<VirtualCharacter> characters;

    public CreateCharacters(ArrayList<VirtualCharacter> characters) {
        this.characters = characters;
    }

    public ArrayList<VirtualCharacter> getCharacters() {
        return characters;
    }

    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
