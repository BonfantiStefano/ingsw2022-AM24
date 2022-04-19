package it.polimi.ingsw.client.request;

public class ChooseCloud implements Request{
    private int index;

    public ChooseCloud(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
