package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;

public class InformationGame implements AnswerWithString{
    private String string;

    public InformationGame(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
