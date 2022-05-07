package it.polimi.ingsw.controller;

/**
 * TurnController will handle the selection of the next turn phase by checking if all requirements to change phase are met
 */
public class TurnController {
    private boolean moveStudentsCheck; //true if 3 moveStudents have been performed
    private boolean moveMNCheck; //true if MN has moved
    private boolean chooseCloudCheck; //true if a Cloud has been chosen
    private boolean chooseAssistantsCheck; //true if all Assistants have been chosen
    private boolean characterActionCheck; //true if the selected Character doesn't require other actions
    private boolean gameStarted; //true if the game has started
    private boolean gameEnded; //true if the game is over
    private boolean allPlayedCheck; // true if all players completed their turn
    private boolean playerConnected; //true if the current player is connected

    /**
     * Creates a new TurnController
     */
    public TurnController(){
        moveMNCheck = false;
        moveStudentsCheck = false;
        chooseAssistantsCheck = false;
        chooseCloudCheck = false;
        gameStarted = false;
        characterActionCheck = true;
        gameEnded = false;
        allPlayedCheck = false;
        playerConnected = true;
    }

    /**
     * Acts as an FSM by choosing the next phase based on the current one and checking if it's possible to move to the
     * next one. If the requirements are not met the Controller will remain in its current phase
     * @param currentPhase the current turn Phase
     * @return the next turn phase
     */
    public PHASE nextPhase(PHASE currentPhase){
        if(gameEnded)
            return PHASE.GAME_WON;
        if(!playerConnected)
            return PHASE.RESET_TURN;

        switch (currentPhase) {
            case SETUP:
                if(gameStarted)
                    return PHASE.PLANNING;
                break;
            case PLANNING:
                if(chooseAssistantsCheck)
                    return PHASE.MOVE_STUDENTS;
                break;
            case MOVE_STUDENTS:
                if(moveStudentsCheck)
                    return PHASE.MOVE_MN;
                break;
            case MOVE_MN:
                if(moveMNCheck)
                    return PHASE.CHOOSE_CLOUD;
                break;
            case CHOOSE_CLOUD:
                if(chooseCloudCheck) {
                    if(characterActionCheck)
                        return PHASE.RESET_TURN;
                    return PHASE.CHARACTER_ACTION;
                }
                break;
            case CHARACTER_ACTION:
                if(characterActionCheck)
                    return PHASE.RESET_TURN;
                break;
            case RESET_TURN:
                if(allPlayedCheck)
                    return PHASE.RESET_ROUND;
                resetTurn();
                return PHASE.MOVE_STUDENTS;
            case RESET_ROUND:
                resetRound();
                return PHASE.PLANNING;
        }
        return currentPhase;
    }

    /**
     * After every turn some indicators must be reset in order to track the next Player's actions
     */
    public void resetRound(){
        moveMNCheck = false;
        moveStudentsCheck = false;
        chooseAssistantsCheck = false;
        chooseCloudCheck = false;
        characterActionCheck = true;
        allPlayedCheck = false;
        playerConnected = true;
    }

    /**
     * After every turn some indicators must be reset in order to track the next Player's actions
     */
    public void resetTurn(){
        moveMNCheck = false;
        moveStudentsCheck = false;
        chooseAssistantsCheck = false;
        chooseCloudCheck = false;
        characterActionCheck = true;
    }

    /**
     * Sets the playerConnected indicator
     * @param playerConnected the Active Player's status
     */
    public void setPlayerConnected(boolean playerConnected) {
        this.playerConnected = playerConnected;
    }

    /**
     * Sets the characterActionCheck indicator
     * @param characterActionCheck true if no further action is needed
     */
    public void setCharacterActionCheck(boolean characterActionCheck) {
        this.characterActionCheck = characterActionCheck;
    }

    /**
     * Sets the GameStarted indicator
     * @param gameStarted ture if the game is in progress
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Sets the moveStudentsCheck indicator
     * @param moveStudentsCheck true if 3 Students have been moved in this turn
     */
    public void setMoveStudentsCheck(boolean moveStudentsCheck) {
        this.moveStudentsCheck = moveStudentsCheck;
    }

    /**
     * Sets the moveMNCheck indicator
     * @param moveMNCheck true if MN has moved in this turn
     */
    public void setMoveMNCheck(boolean moveMNCheck) {
        this.moveMNCheck = moveMNCheck;
    }

    /**
     * Sets the chooseCloudCheck indicator
     * @param chooseCloudCheck true if the Player has chosen a Cloud this turn
     */
    public void setChooseCloudCheck(boolean chooseCloudCheck) {
        this.chooseCloudCheck = chooseCloudCheck;
    }

    /**
     * Sets che chooseAssistantsCheck indicator
     * @param chooseAssistantsCheck true if all Player have chosen their Assistant
     */
    public void setChooseAssistantsCheck(boolean chooseAssistantsCheck) {
        this.chooseAssistantsCheck = chooseAssistantsCheck;
    }

    /**
     * Sets the gameEnded indicator
     * @param gameEnded true if someone won the game
     */
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Sets the allPlayedCheck indicator
     * @param allPlayedCheck true if all Players have completed their turn
     */
    public void setAllPlayedCheck(boolean allPlayedCheck) {
        this.allPlayedCheck = allPlayedCheck;
    }

}
