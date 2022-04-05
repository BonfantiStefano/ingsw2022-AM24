package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.influence.StandardInfluence;

import java.util.*;

/**
 * World class contains the list of the Islands that represents the world of the game. It also contains an attribute
 * InfluenceStrategy that allowed changing dynamically the algorithm used to calculate the Influence of an Island (pattern Strategy).
 *
 * @author Bonfanti Stefano
 */
public class World implements HasStrategy<InfluenceStrategy> {
    private ArrayList<Island> islands;
    private InfluenceStrategy influenceStrategy;
    private int posMN;
    private Optional<ColorS> bannedColorS;

    /**Constructor World creates a new World instance with already the students allocated.*/
    public World(ArrayList<ColorS> initialStudent) {
        this.islands = new ArrayList<>();
        this.influenceStrategy = new StandardInfluence();
        this.bannedColorS = Optional.empty();
        init(initialStudent);
    }

    /**Constructor World creates a new empty world instance.*/
    public World() {
        this.islands = new ArrayList<>();
        this.influenceStrategy = new StandardInfluence();
        this.bannedColorS = Optional.empty();
    }

    /**
     * Private method init is used from the World constructor and gives Mother nature a random position and places
     * Student according to the rule.
     * @param initialStudent ArrayList<Student> - a list of 10 Student, 2 of each color.
     */
    private void init(ArrayList<ColorS> initialStudent){
        Random random = new Random();
        posMN = random.nextInt(12);
        int oppositePosMN;
        if (posMN >= 6) {
            oppositePosMN = posMN-6;
        } else {
            oppositePosMN = posMN+6;
        }
        for(int counter = 0; counter < 12; counter++) {
            Island island = new Island();
            islands.add(island);
            if(counter != oppositePosMN && counter != posMN) {
                island.add(initialStudent.get(0));
                initialStudent.remove(0);
            }
        }
    }

    /**
     * Method moveMN moves Mother Nature.
     * @param numMNSteps int - number of steps done by Mother Nature.
     */
    public Island moveMN(int numMNSteps) {
        if (posMN + numMNSteps >= getSize()) {
            posMN = posMN + numMNSteps - getSize();
        } else {
            posMN = numMNSteps + posMN;
        }
        return islands.get(posMN);
    }

    /**
     * Method checkEntry checks if the Island has any noEntryTiles on it, if presents removes one and returns false, otherwise
     * returns true.
     * @return a boolean, true when the influence can be calculated, otherwise is false
     */
    public boolean checkEntry() {
        if (islands.get(posMN).getNumNoEntry() > 0) {
            islands.get(posMN).setNumNoEntry(-1);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method checkConquest checks if the island must change the owner.
     * @param mapInfluence Map<Player, Integer> - The map of the island's influence.
     * @param players List<Player> - The list of the players.
     * @return the new owner, if it is different from the old onw or if there aren't multiple players with the same influence.
     */
    public Optional<Player> checkConquest(Map<Player, Integer> mapInfluence, List<Player> players) {
        int actualInfluence = 0;
        Optional<Player> nextOwner = Optional.empty();
        for(Player player : players) {
            if(Optional.of(player.getColorTower()).equals(islands.get(posMN).getTowerColor())) {
                actualInfluence = mapInfluence.get(player);
            }
        }
        for(Player player : players) {
            if(mapInfluence.get(player) > actualInfluence) {
                nextOwner = Optional.of(player);
                actualInfluence = mapInfluence.get(player);
            } else if(mapInfluence.get(player) == actualInfluence) {
                nextOwner = Optional.empty();
            }
        }
        return nextOwner;
    }


    /**
     * Method getInfluenceIsland calculate the influence for every single player using the method getInfluence and put
     * the result in a map.
     * @param i Island - the island on which the influence is calculated.
     * @param profs Map<ColorS, Player> - Map that contains the association of a prof and the owner.
     * @param players List<Player> - List where are contained all the players of the game.
     * @return a map with every player and his influence on the Island i.
     */
    public Map<Player, Integer> getInfluenceIsland(Island i, Map<ColorS, Player> profs, List<Player> players) {
        Map<Player, Integer> mapInfluence = new HashMap<>();
        for(Player p : players) {
            mapInfluence.put(p, influenceStrategy.getInfluence(i, p, bannedColorS, profs));
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
        posMN = indexIsland;
        return newIsland;
    }

    /**
     * Method checkJoin looks if the Island needs to be merged with the next and previous Islands.
     * @param i Island - Island that will be checked.
     */
    public void checkJoin(Island i) {
        int indexIsland = islands.indexOf(i);
        if(islands.size() > indexIsland+1) {
            if(!Optional.ofNullable(islands.get(indexIsland+1).getTowerColor()).equals(Optional.empty()) &&
                    !Optional.ofNullable(i.getTowerColor()).equals(Optional.empty()) && islands.get(indexIsland+1).getTowerColor().equals(i.getTowerColor())) {
                Island newIsland = join(i, islands.get(indexIsland+1));
                checkJoin(newIsland);
            }
        } else {
            if(!Optional.ofNullable(islands.get(0).getTowerColor()).equals(Optional.empty()) &&
                    !Optional.ofNullable(i.getTowerColor()).equals(Optional.empty()) && islands.get(0).getTowerColor().equals(i.getTowerColor())) {
                Island newIsland = join(islands.get(0), i);
                checkJoin(newIsland);
            }
        }
        if(indexIsland == 0) {
            if(!Optional.ofNullable(islands.get(islands.size()-1).getTowerColor()).equals(Optional.empty()) &&
                    !Optional.ofNullable(i.getTowerColor()).equals(Optional.empty()) && islands.get(islands.size()-1).getTowerColor().equals(i.getTowerColor())) {
                Island newIsland = join(i, islands.get(islands.size()-1));
                checkJoin(newIsland);
            }
        } else {
            if(!Optional.ofNullable(islands.get(indexIsland-1).getTowerColor()).equals(Optional.empty()) &&
                    !Optional.ofNullable(i.getTowerColor()).equals(Optional.empty()) && islands.get(indexIsland-1).getTowerColor().equals(i.getTowerColor())) {
                Island newIsland = join(islands.get(indexIsland-1), i);
                checkJoin(newIsland);
            }
        }
    }

    /**
     * Method setBannedColorS sets the color that is used by the method getInfluenceIsland.
     * @param colorS ColorS - The color used in getInfluenceIsland.
     */
    public void setBannedColorS(ColorS colorS) {
        this.bannedColorS = Optional.of(colorS);
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

    //method used only for testing
    public Island getIslandByIndex(int index) {
        return islands.get(index);
    }

    //method used only for testing
    public int getMNPosition() {
        return posMN;
    }

    //method used only for testing
    public Optional<ColorS> getBannedColorS() {
        return bannedColorS;
    }
}
