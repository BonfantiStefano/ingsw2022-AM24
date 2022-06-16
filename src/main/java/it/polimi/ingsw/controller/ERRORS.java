package it.polimi.ingsw.controller;

/**
 * Enum ERRORS enumerates all the possible error with the corresponding string that will be sent to the client.
 */
public enum ERRORS {

    INVALID_MESSAGE ("Invalid Message"),
    NICKNAME_TAKEN ("Nickname already in use"),
    MAGE_TAKEN("Mage already in use"),
    COLOR_TOWER_TAKEN("Towers' color already in use"),
    CHARACTER_NOT_AVAILABLE("This card is not available"),
    NOT_ENOUGH_COINS("You don't have enough coins"),
    NO_SUCH_STUDENT("There's no such Student"),
    NO_MOVES_REMAINING("You have no Character moves remaining"),
    NO_ACTIVE_CHARACTER("There isn't an Active Character"),
    INVALID_INDEX("Invalid index");



    private final String error;

    /**
     * Constructor ERRORS creates a new enum's value with its string.
     * @param error String - the error's string.
     */
    ERRORS(String error){
        this.error = error;
    }

    /**
     * Method toString returns the error corresponding to the enum's value..
     * @return String - the error.
     */
    @Override
    public String toString() {
        return "ERROR: " + error + "!";
    }
}
