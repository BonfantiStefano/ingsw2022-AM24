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
    void nextPhaseNormalTurn() {
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
        assertEquals(PHASE.RESET_TURN, p);
        p = t.nextPhase(p);
        assertEquals(PHASE.PLANNING, p);
        t.setGameEnded(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.GAME_WON, p);
    }

    /**
     * Ensures that only if everyone has played the Game changes round
     */
    @Test
    void resetRound(){
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
        t.setAllPlayedCheck(true);
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
        t.setChooseCloudCheck(false);
        p = t.nextPhase(p);
        assertEquals(PHASE.CHOOSE_CLOUD, p);
        t.setChooseCloudCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.CHARACTER_ACTION, p);
        p = t.nextPhase(p);
        assertEquals(PHASE.CHARACTER_ACTION, p);
        t.setCharacterActionCheck(true);
        p = t.nextPhase(p);
        assertEquals(PHASE.RESET_TURN, p);

    }

    /**
     * Ensures that if a Player disconnects his turn will be skipped
     */
    @Test
    void playerDisconnected(){
        p = PHASE.MOVE_STUDENTS;
        t.setPlayerConnected(false);
        p = t.nextPhase(p);
        assertEquals(PHASE.RESET_TURN, p);
    }

    @Test
    void reset() {
        t.resetRound();
        p = t.nextPhase(p);
        assertEquals(PHASE.SETUP, p);
    }
}