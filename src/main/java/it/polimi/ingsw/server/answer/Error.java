package it.polimi.ingsw.server.answer;

public class Error implements Answer{
    private String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
