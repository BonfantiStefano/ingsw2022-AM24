package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.server.virtualview.VirtualCharacter;

public class ReplaceCharacter implements Update{
    private final VirtualCharacter character;

    public ReplaceCharacter(VirtualCharacter character) {
        this.character = character;
    }

    public VirtualCharacter getCharacter() {
        return character;
    }
}
