package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;

public class UpdateActiveCharacter implements Update{
    private int index;
    private boolean active;

    public UpdateActiveCharacter(int index, boolean active) {
        this.index = index;
        this.active = active;
    }

    public int getIndex() {
        return index;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
