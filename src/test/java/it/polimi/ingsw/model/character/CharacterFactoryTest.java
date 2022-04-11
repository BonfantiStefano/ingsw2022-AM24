package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.StudentContainer;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.world.World;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class CharacterFactoryTest tests CharacterFactory
 */
class CharacterFactoryTest {
    /**
     * Creates all Characters and adds them to a list, checks if all have been created
     */
    @Test
    void createCharacter() {
        StudentContainer bag = new StudentContainer();
        GameBoard g = new GameBoard(3);
        World w = new World();
        Player p = new Player("1", ColorT.WHITE, Mage.MAGE2, 1);
        p.setPlaying(true);
        ArrayList<PlayerInterface> players = new ArrayList<>();
        players.add(p);
        CharacterFactory factory= new CharacterFactory(w, g, bag, players);
        ArrayList<Character> characters = new ArrayList<>();
        for(int i=0;i <12;i++)
            characters.add(factory.createCharacter());
        for(Character c: characters){
            c.play();
        }
    }
}