package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.character.CharacterDescription;

public class PlayCharacter implements Request {
    private CharacterDescription c;

    public CharacterDescription getC() {
        return c;
    }
}