package it.polimi.ingsw.server.answer;

/**
 * Interface AnswerWithString is implemented by all the Answer that contains a String.
 */
public interface AnswerWithString extends Answer {

    /**
     * Method getString returns the string of the message.
     * @return String - the string contained in the message.
     */
    String getString();
}
