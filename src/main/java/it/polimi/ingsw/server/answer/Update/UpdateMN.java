package it.polimi.ingsw.server.answer.Update;

public class UpdateMN implements Update{
    private final int index;

    public UpdateMN(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
