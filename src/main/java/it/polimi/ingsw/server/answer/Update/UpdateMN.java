package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;

public class UpdateMN implements Update{
    private final int index;

    public UpdateMN(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
