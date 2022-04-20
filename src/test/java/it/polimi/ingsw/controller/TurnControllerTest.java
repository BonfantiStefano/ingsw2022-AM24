package it.polimi.ingsw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class TurnControllerTest tests the behaviour of TurnController
 */
class TurnControllerTest {
    TurnController t;
    PHASE p;

    @BeforeEach
    void setUp() {
        t = new TurnController();
        p = PHASE.SETUP;
    }

    /**
     * Checks if phases are cycled through correctly
     */
    @Test
    void nextPhaseNormal() {
        t.setGameStarted(true);
        p = t.nextPhase(p);
        t.setChooseAssistantsCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.MOVE_STUDENTS, p);
        t.setMoveStudentsCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.MOVE_MN, p);
        t.setMoveMNCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.CHOOSE_CLOUD, p);
        t.setChooseCloudCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.RESET_ROUND, p);
        p = t.nextPhase(p);
        assertEquals(PHASE.PLANNING, p);
    }

    /**
     * If a Character action has not been performed the game will not progress
     */
    @Test
    void WaitingForAction(){
        p = PHASE.CHOOSE_CLOUD;
        t.setCharacterActionCheck(false);
        t.setChooseCloudCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.CHARACTER_ACTION, p);
        p = t.nextPhase(p);
        assertEquals(PHASE.CHARACTER_ACTION, p);
        t.setCharacterActionCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.RESET_ROUND, p);

    }

    @Test
    void reset() {
        t.reset();
        p = t.nextPhase(p);
        assertEquals(PHASE.SETUP, p);
    }
}