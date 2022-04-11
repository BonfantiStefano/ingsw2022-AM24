package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.HasStrategy;
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
        posMN = (posMN + numMNSteps) % getSize();
        return islands.get(posMN);
    }

    /**
     * Method checkEntry checks if the Island has any noEntryTiles on it, if presents removes one and returns false, otherwise
     * returns true.
     * @return a boolean, true when the influence can be calculated, otherwise is false
     */
    public boolean checkEntry(Island island) {
        if (island.getNumNoEntry() > 0) {
            island.setNumNoEntry(-1);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method checkConquest checks if the island must change the owner.
     * @param mapInfluence Map<Player, Integer> - The map of the island's influence.
     * @param players List<Player> - The list of the players.
     * @param island Island - The island that has to be checked.
     * @return the new owner, if it is different from the old onw or if there aren't multiple players with the same influence.
     */
    public Optional<Player> checkConquest(HashMap<Player, Integer> mapInfluence, ArrayList<Player> players, Island island) {
        int actualInfluence = 0;
        Optional<Player> nextOwner = Optional.empty();
        for(Player player : players) {
            if(Optional.of(player.getColorTower()).equals(island.getTowerColor())) {
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
    public HashMap<Player, Integer> getInfluenceIsland(Island i, HashMap<ColorS, Player> profs, ArrayList<Player> players) {
        HashMap<Player, Integer> mapInfluence = new HashMap<>();
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
        if(!islands.get((indexIsland+1) % getSize()).getTowerColor().equals(Optional.empty()) &&
                !i.getTowerColor().equals(Optional.empty()) && islands.get((indexIsland+1) % getSize()).getTowerColor().equals(i.getTowerColor()) && getSize()>3) {
            Island newIsland = join(islands.get(Math.min((indexIsland+1) % getSize(), indexIsland)), islands.get(Math.max((indexIsland+1) % getSize(), indexIsland)));
            checkJoin(newIsland);
            indexIsland = islands.indexOf(newIsland);
        }
        int indexPreviousIsland = (indexIsland-1) % getSize() < 0 ? (indexIsland-1) % getSize() + getSize() : (indexIsland-1) % getSize();
        if(!islands.get(indexPreviousIsland).getTowerColor().equals(Optional.empty()) &&
                !i.getTowerColor().equals(Optional.empty()) && islands.get(indexPreviousIsland).getTowerColor().equals(i.getTowerColor()) && getSize()>3) {
            Island newIsland = join(islands.get(Math.min(indexPreviousIsland, indexIsland)), islands.get(Math.max(indexPreviousIsland, indexIsland)));
            checkJoin(newIsland);
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

    /**
     * Method getIslandByIndex takes an index and returns the relative Island of the World.
     * @param index int - The index of the Island.
     * @return the Island with the index given by parameter.
     */
    public Island getIslandByIndex(int index) {
        return islands.get(index);
    }

    /**
     * Method getMNPosition returns the index of the Island where Mother Nature is.
     * @return an index of an Island.
     */
    public int getMNPosition() {
        return posMN;
    }

    //method used only for testing
    public Optional<ColorS> getBannedColorS() {
        return bannedColorS;
    }
}
