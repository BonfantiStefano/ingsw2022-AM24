package it.polimi.ingsw.server.answer.Update;

public class UpdateCoins implements Update{
    private final int coins;

    public UpdateCoins(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }
}
