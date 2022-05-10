package it.polimi.ingsw.server.answer;

public class NotifyDisconnection implements AnswerWithString{
    private String string;

    public NotifyDisconnection(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }
}
