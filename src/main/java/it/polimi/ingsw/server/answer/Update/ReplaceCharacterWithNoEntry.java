package it.polimi.ingsw.server.answer.Update;


import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithNoEntry;

public class ReplaceCharacterWithNoEntry implements Update{
    private final VirtualCharacterWithNoEntry characterWithNoEntry;
    private final int index;

    public ReplaceCharacterWithNoEntry(VirtualCharacterWithNoEntry characterWithNoEntry, int index) {
        this.characterWithNoEntry = characterWithNoEntry;
        this.index = index;
    }

    public VirtualCharacterWithNoEntry getCharacterWithNoEntry() {
        return characterWithNoEntry;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
