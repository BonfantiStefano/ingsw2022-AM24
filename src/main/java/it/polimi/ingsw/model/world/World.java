package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.influence.StandardInfluence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

/**
 * World class contains the list of the Islands that represents the world of the game. It also contains an attribute
 * InfluenceStrategy that allowed changing dynamically the algorithm used to calculate the Influence of an Island.
 *
 * @author Bonfanti Stefano
 */
public class World implements HasStrategy<InfluenceStrategy> {
    private ArrayList<Island> islands;
    private InfluenceStrategy influenceStrategy;

    /**Constructor World creates a new World instance with already the students allocated.*/
    public World(ArrayList<Student> initialStudent, Island mnLocation) {
        this.islands = new ArrayList<>();
        this.influenceStrategy = new StandardInfluence();
        Random random = new Random();
        int posMN = random.nextInt(12);
        int oppositePosMN;
        // int randomPos;
        if (posMN >= 6) {
            oppositePosMN = posMN-6;
        } else {
            oppositePosMN = posMN+6;
        }
        for(int counter = 0; counter < 12; counter++) {
            if(counter != posMN) {
                Island island = new Island();
                islands.add(island);
                if(counter != oppositePosMN) {
                    //se gli studenti sono già stati inseriti casualmente faccio:
                    island.add(initialStudent.get(0));
                    initialStudent.remove(0);
                    //se gli studenti devono essere estratti in maniera casuale faccio:
                     /*
                    randomPos = random.nextInt(initialStudent.size());
                    i.add(initialStudent.get(randomPos));
                    initialStudent.remove(randomPos);
                     */
                }
            } else {
                islands.add(mnLocation);
            }
        }
    }

    /**Constructor World creates a new empty world instance.*/
    public World() {
        this.islands = new ArrayList<>();
        this.influenceStrategy = new StandardInfluence();
    }

    /**
     * Method getInfluenceIsland calculate the influence for every single player using the method getInfluence and put
     * the result in a map.
     * @param i Island - the island on which the influence is calculated.
     * @param profs HashMap<ColorS, Player> - Map that contains the association of a prof and the owner.
     * @param players ArrayList<Player> - List where are contained all the players of the game.
     * @param colorS Optional<ColorS> - ColorS utilized by the NoColorStrategy, otherwise is null.
     * @return a map with every player and his influence on the Island i.
     */
    public HashMap<Player, Integer> getInfluenceIsland(Island i, HashMap<ColorS, Player> profs, ArrayList<Player> players, Optional<ColorS> colorS) {
        HashMap<Player, Integer> mapInfluence = new HashMap<>();
        for(Player p : players) {
            mapInfluence.put(p, influenceStrategy.getInfluence(i, p, colorS, profs));
        }
        return mapInfluence;
    }

    /**
     * Method join merges two island, remove the two original Island from the List and add the new one.
     * @param i1 Island - first island to join.
     * @param i2 Island - second island to join.
     * @return the new Island.
     */
    public Island join(Island i1, Island i2) {
        int indexIsland = islands.indexOf(i1);
        Island newIsland = new Island(i1, i2);
        islands.remove(i1);
        islands.remove(i2);
        islands.add(indexIsland, newIsland);
        return newIsland;
    }

    /**
     * Method checkJoin looks if the Island needs to be merged with the next and previous Islands.
     * @param i Island - Island that will be checked.
     */
    public void checkJoin(Island i) {
        int indexIsland = islands.indexOf(i);
        if(islands.size() > indexIsland+1) {
            if(islands.get(indexIsland+1).getTowerColor().equals(i.getTowerColor())) {
                Island newIsland = join(i, islands.get(indexIsland+1));
                checkJoin(newIsland);
            }
        } else {
             if(islands.get(0).getTowerColor().equals(i.getTowerColor())) {
                 Island newIsland = join(islands.get(0), i);
                 checkJoin(newIsland);
             }
        }
        if(indexIsland == 0) {
            if(islands.get(islands.size()-1).getTowerColor().equals(i.getTowerColor())) {
                Island newIsland = join(i, islands.get(islands.size()-1));
                checkJoin(newIsland);
            }
        } else {
            if(islands.get(indexIsland-1).getTowerColor().equals(i.getTowerColor())) {
                Island newIsland = join(islands.get(indexIsland-1), i);
                checkJoin(newIsland);
            }
        }
    }

    /**
     * Method getSize returns the number of the remaining islands.
     * @return int - number of the islands.
     */
    public int getSize() {
        return islands.size();
    }

    /**
     * Method setStrategy changes the algorithm used to be used to calculate the influence.
     * @param strategy InfluenceStrategy
     */
    @Override
    public void setStrategy(InfluenceStrategy strategy) {
        this.influenceStrategy=strategy;
    }

    /**
     * Resets the strategy so the class will have the standard behaviour.
     */
    @Override
    public void resetStrategy() {
        this.influenceStrategy=new StandardInfluence();
    }

    /**
     * Method getStrategy returns the strategy currently used to calculate the influence of an Island.
     * @return InfluenceStrategy
     */
    @Override
    public InfluenceStrategy getStrategy() {
        return influenceStrategy;
    }

    //metodo usato per fare i testing
    public Island getIslandByIndex(int index) {
        return islands.get(index);
    }
}