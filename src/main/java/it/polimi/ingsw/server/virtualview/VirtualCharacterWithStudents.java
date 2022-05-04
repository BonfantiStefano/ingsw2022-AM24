package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterWithStudent;

import java.util.ArrayList;

/** VirtualCharacterWithStudents class is a simplified representation of a VirtualCharacterWithStudents card containing students.*/
public class VirtualCharacterWithStudents extends VirtualCharacter {

    private final ArrayList<ColorS> students;
    public VirtualCharacterWithStudents(Character character) {
        super(character);
        this.students = ((CharacterWithStudent) character).getStudents();
    }

    public ArrayList<ColorS> getStudents() {
        return students;
    }
}
