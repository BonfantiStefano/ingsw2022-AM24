package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.server.answer.Answer;

public interface Update extends Answer {
    void accept(CLI c);
}
