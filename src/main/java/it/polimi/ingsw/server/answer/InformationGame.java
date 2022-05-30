package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;

/**
 * Class InformationGame is used from the server to notify the client about a game's information.
 */
public class InformationGame implements AnswerWithString{
    private String string;

    /**
     * Constructor InformationGame creates a new message with the string given by parameter.
     * @param string String - the string containing the information.
     */
    public InformationGame(String string) {
        this.string = string;
    }

    /**
     * Method getString returns the string of the message.
     * @return String - the connection's information.
     */
    @Override
    public String getString() {
        return string;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
