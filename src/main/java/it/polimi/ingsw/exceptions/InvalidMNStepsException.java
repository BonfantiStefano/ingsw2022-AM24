package it.polimi.ingsw.exceptions;

/**
 * Class InvalidMNStepsException is thrown when a user tries to give a number of Mother Nature steps too high.
 *
 * @author Bonfanti Stefano
 */
public class InvalidMNStepsException extends Throwable {

    /**
     * Method getMessage returns the message of InvalidMNStepsException object.
     *
     * @return the message (type String) of InvalidMNStepsException object.
     */
    @Override
    public String getMessage() {
        return ("Error: Mother Nature can't do so many steps");
    }
}
