package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *  Defines the signature of the method used to calculate who controls Profs
 */
public interface ProfStrategy {
    HashMap<ColorS, Player> checkProfs(ArrayList<Player> players, HashMap<ColorS, Player> profs);
}
