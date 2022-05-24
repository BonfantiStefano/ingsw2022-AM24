package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;

public class UpdateCoins implements Update{
    private final int coins;

    public UpdateCoins(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
