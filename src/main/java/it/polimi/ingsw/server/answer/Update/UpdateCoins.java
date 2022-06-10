package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;

/**
 * Class UpdateCoins is used from the server to notify about the game's coins to the client.
 */
public class UpdateCoins implements Update{
    private final int coins;

    /**
     * Constructor UpdateCoins creates a new update message.
     * @param coins int - the amount of available coins.
     */
    public UpdateCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Method getCoins returns the amount of available coins.
     * @return the amount of available coins.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
