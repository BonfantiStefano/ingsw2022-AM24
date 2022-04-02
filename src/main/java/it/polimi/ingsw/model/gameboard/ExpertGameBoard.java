package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public class ExpertGameBoard extends GameBoard {
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

    public void hallToEntrance(Student s){
        getActivePlayer().getMyBoard().hallToEntrance(s);
    }

    public void addToHall(Student s){
        if(getActivePlayer().getMyBoard().addToHall(s))
            getActivePlayer().setCoins(1);
    }

    public void removeHall(Student s, Player p){
        p.getMyBoard().removeHall(s);
    }



}
