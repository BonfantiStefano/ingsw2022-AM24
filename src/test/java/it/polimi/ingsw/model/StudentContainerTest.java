package it.polimi.ingsw.model;

import it.polimi.ingsw.model.pawn.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        ArrayList<ColorS> students=bag.initialDraw();
        for(ColorS c: ColorS.values()){
            assertEquals(2, students.stream().filter(s -> s.equals(c)).count());
        }
    }

    @Test
    void draw() {
        StudentContainer bag=new StudentContainer();
        HashMap<ColorS, Integer> map=new HashMap<>();
        ColorS c;
        for(int i=0; i<120; i++) {
            c=bag.draw();
            int count = map.getOrDefault(c, 0);
            map.put(c, count+1);
        }

        for(ColorS color: ColorS.values()){
            assertEquals(24, map.get(color));
        }
    }
}
