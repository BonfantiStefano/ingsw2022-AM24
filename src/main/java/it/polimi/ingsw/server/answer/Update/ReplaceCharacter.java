package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCharacter;

public class ReplaceCharacter implements Update{
    private final VirtualCharacter character;
    private final int index;

    public ReplaceCharacter(VirtualCharacter character, int index) {
        this.character = character;
        this.index = index;
    }

    public VirtualCharacter getCharacter() {
        return character;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
