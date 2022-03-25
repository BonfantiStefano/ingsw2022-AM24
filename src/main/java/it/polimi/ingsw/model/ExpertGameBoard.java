package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ExpertGameBoard extends GameBoard{
    private Character activeCharacter;
    private ArrayList<Character> characters;
    private int coins;

    public void playActiveCharacter(){
        activeCharacter.play();
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public Character getActiveCharacter() {
        return activeCharacter;
    }

    public void setActiveCharacter(Character activeCharacter) {
        this.activeCharacter = activeCharacter;
    }

    public int getAvailableCoins() {
        return coins;
    }

    public void setAvailableCoins(int coins) {
        this.coins = coins;
    }

}
