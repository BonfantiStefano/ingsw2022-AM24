package it.polimi.ingsw.controller;

public enum ERRORS {

    INVALID_MESSAGE ("Invalid Message"),
    NICKNAME_TAKEN ("Nickame already in use"),
    MAGE_TAKEN("Mage already in use"),
    CHARACTER_NOT_AVAILABLE("This card is not available"),
    NOT_ENOUGH_COINS("You don't have enough coins"),
    NO_SUCH_STUDENT("There's no such Student"),
    NO_MOVES_REMAINING("You have no Character moves remaining"),
    NO_ACTIVE_CHARACTER("There isn't an Active Character"),
    INVALID_INDEX("Invalid index");



    private final String error;

    ERRORS(String error){
        this.error = error;
    }

    @Override
    public String toString() {
        return "ERROR: " + error + "!";
    }
}
