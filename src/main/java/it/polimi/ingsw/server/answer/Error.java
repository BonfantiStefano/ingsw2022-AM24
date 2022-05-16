package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.CLIView.CLI;

public class Error implements AnswerWithString{
    private String string;

    public Error(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public void accept(CLI c) {
        c.visit(this);
    }
}
