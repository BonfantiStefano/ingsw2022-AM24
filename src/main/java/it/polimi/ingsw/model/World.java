package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class World {
    private ArrayList<Island> islands;
    private InfluenceStrategy influenceStrategy;

    public InfluenceStrategy getInfluenceStrategy() {
        return influenceStrategy;
    }

    public void setInfluenceStrategy(InfluenceStrategy influenceStrategy) {
        this.influenceStrategy = influenceStrategy;
    }


    public HashMap<Player, Integer> getInfluenceIsland(Island i, HashMap<ColorS, Player> profs, ArrayList<Player> players, Optional<ColorS> colorS) {
        HashMap<Player, Integer> mapInfluence = new HashMap<>();
        for(Player p : players) {
            mapInfluence.put(p, influenceStrategy.getInfluence(i, p, colorS, profs));
        }
        return mapInfluence;
    }


    public Island join(Island i1, Island i2) {
        int indexIsland = islands.indexOf(i1);
        Island newIsland = new Island(i1, i2);
        islands.remove(i1);
        islands.remove(i2);
        islands.add(indexIsland, newIsland);
        return newIsland;
    }

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

    public int getSize() {
        return islands.size();
    }

    /* mi manca il modo per sapere chi sta controllando l'isola adesso, poichè devo rimettergli le torri
       e poi la rimozione delle torri è complicata poichè dovrei sapere che torre rimuovere ma ciò implica che devo
       chiedere la lista delle torri e poi toglierle una ad una

    public int conquest(Island i, Player p) {
        for(int indexTower = 0; indexTower < i.getNumSubIsland(); indexTower++) {
            p.getSchoolBoard()
        }
        return i.getNumSubIsland();
    }
    */
}
