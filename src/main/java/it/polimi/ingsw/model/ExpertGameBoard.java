package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ExpertGameBoard extends GameBoard{
    private Character activeCharacter;
    private ArrayList<Character> characters;

    public ArrayList<Character> getCharacters(){
        return characters;
    }

    public Character getActiveCharacter() {
        return activeCharacter;
    }
}
