package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterWithStudent;

import java.util.ArrayList;

/** VirtualCharacterWithStudents class is a simplified representation of a VirtualCharacterWithStudents card containing students.*/
public class VirtualCharacterWithStudents extends VirtualCharacter {

    private ArrayList<ColorS> students;

    /**Constructor VirtualCharacterWithStudents creates a new VirtualCharacterWithStudents instance.*/
    public VirtualCharacterWithStudents(Character character) {
        super(character);
        this.students = ((CharacterWithStudent) character).getStudents();
    }

    /**
     * Method getStudents returns the list containing all the students placed of the virtual card
     * @return - the students placed on the virtual card
     */
    public ArrayList<ColorS> getStudents() {
        return students;
    }

}
