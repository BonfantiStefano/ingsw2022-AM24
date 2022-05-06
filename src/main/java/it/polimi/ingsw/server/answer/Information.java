package it.polimi.ingsw.server.answer;

public class Information implements AnswerWithString{
    private String string;

    public Information(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }
}
