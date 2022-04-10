package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a Player doesn't have enough coins to play a Character
 */
public class NotEnoughCoinsException extends Throwable{
    /**
     * Method getMessage returns the message of NotEnoughCoinsException object.
     *
     * @return the message (type String) of NotEnoughCoinsException object.
     */
    @Override
    public String getMessage(){
        return "Error: the Player doesn't have enough coins";
    }
}
