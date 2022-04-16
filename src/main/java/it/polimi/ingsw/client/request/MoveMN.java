package it.polimi.ingsw.client.request;

public class MoveMN implements Request{
    private int index;

    public MoveMN(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
