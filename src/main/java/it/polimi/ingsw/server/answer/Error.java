package it.polimi.ingsw.server.answer;

public class Error implements AnswerWithString{
    private String string;

    public Error(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }
}