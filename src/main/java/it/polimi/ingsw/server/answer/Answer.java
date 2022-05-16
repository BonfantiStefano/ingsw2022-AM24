package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.CLIView.CLI;

import java.io.Serializable;

public interface Answer extends Serializable{
    void accept(CLI c);
}
