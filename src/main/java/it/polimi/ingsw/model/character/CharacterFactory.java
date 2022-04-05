package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.StudentContainer;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.profstrategy.EqualProf;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.world.influence.AdditionalInfluence;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import it.polimi.ingsw.model.world.influence.NoTowerInfluence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory used to generate random Characters at the start of the game
 */
public class CharacterFactory {
    private HasStrategy<InfluenceStrategy> world;
    private HasStrategy<ProfStrategy> gameBoard;
    private StudentContainer bag;
    private List<Integer> random;
    private ArrayList<PlayerInterface> players;

    /**
     * Constructor creates a new CharacterFactory with a list of numbers in random order to generate random Characters
     */
    public CharacterFactory(HasStrategy<InfluenceStrategy> world, HasStrategy<ProfStrategy> gameBoard, StudentContainer bag, ArrayList<PlayerInterface> players) {
        this.world = world;
        this.gameBoard = gameBoard;
        this.bag = bag;
        this.players=players;
        random=new ArrayList<>();
        for(int i=1;i<=12;i++)
            random.add(i);
        Collections.shuffle(random);
    }

    /**
     * Invokes gerRandom to obtain a random Integer and generates the corresponding Character
     * @return Character a random Character chosen from the ones not yet created
     */
    public Character createCharacter(){
        int i=getRandom();
        int cost=CharacterDescription.values()[i-1].getCost();
        String desc = CharacterDescription.values()[i-1].getDesc();

        Character c=null;
        CharacterWithStudent temp;

        switch (i){
            case 1:
            case 11:
                temp = new CharacterWithStudent(cost, desc, 4);
                for(int j=0;j<4;j++)
                    temp.add(bag.draw());
                c=temp;
                break;
            case 2:
                c = new CharacterProf(cost, desc, new EqualProf(), gameBoard);
                break;
            case 3:
            case 10:
            case 12:
                c = new Character(cost,desc);
                break;
            case 4:
                c = new CharacterMN(cost, desc, players);
                break;
            case 5:
                c = new CharacterWithNoEntry(cost, desc);
                break;
            case 6:
                c = new CharacterInfluence(cost, desc, new NoTowerInfluence(), world);
                break;
            case 7:
                temp = new CharacterWithStudent(cost, desc, 6);
                for(int j=0;j<6;j++)
                    temp.add(bag.draw());
                c=temp;
                break;
            case 8:
                c = new CharacterInfluence(cost, desc, new AdditionalInfluence(), world);
                break;
            case 9:
                c = new CharacterInfluence(cost, desc, new NoColorInfluence(), world);
                break;

        }
        return c;
    }

    /**
     * Gets the first element from the random list
     * @return int a random Integer
     */
    private int getRandom(){
        return random.remove(0);
    }
}
