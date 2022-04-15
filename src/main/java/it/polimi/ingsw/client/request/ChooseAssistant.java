package it.polimi.ingsw.client.request;

public class ChooseAssistant implements Request{
    private int index;

    public ChooseAssistant(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
