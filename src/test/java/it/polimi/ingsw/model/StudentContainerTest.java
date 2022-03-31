package it.polimi.ingsw.model;

import it.polimi.ingsw.model.pawn.Student;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class StudentContainerTest {
    /**
     *  Ensures that only 2 students per ColorS are drawn at the start of a match
     */
    @Test
    void initialDraw() {
        StudentContainer bag=new StudentContainer();
        HashMap<ColorS, Integer> map=new HashMap<>();
        ColorS c;
        for(int i=0; i<10; i++) {
            c=bag.initialDraw().getColor();
            int count = map.getOrDefault(c, 0);
            map.put(c, count+1);
        }

        for(ColorS color: ColorS.values()){
            assertEquals(2, map.get(color));
        }
    }

    @Test
    void draw() {
        StudentContainer bag=new StudentContainer();
        HashMap<ColorS, Integer> map=new HashMap<>();
        ColorS c;
        for(int i=0; i<120; i++) {
            c=bag.draw().getColor();
            int count = map.getOrDefault(c, 0);
            map.put(c, count+1);
        }

        for(ColorS color: ColorS.values()){
            assertEquals(24, map.get(color));
        }
    }
}
