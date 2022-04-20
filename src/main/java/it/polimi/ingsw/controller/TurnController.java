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
    private boolean gameStarted; //true if the game is in progress
    private boolean gameEnded;

    /**
     * Creates a new TurnController
     */
    public TurnController(){
        moveMNCheck=false;
        moveStudentsCheck=false;
        chooseAssistantsCheck=false;
        chooseCloudCheck=false;
        gameStarted=false;
        characterActionCheck = true;
        gameEnded = false;
    }

    /**
     * Acts as an FSM by choosing the next phase based on the current one and checking if it's possible to move to the
     * next one. If the requirements are not met the Controller will remain in its current phase
     * @param currentPhase the current Phase of the turn
     * @return the next phase of the turn
     */
    public PHASE nextPhase(PHASE currentPhase){
        if(gameEnded)
            return PHASE.GAME_WON;

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
                if(chooseCloudCheck && characterActionCheck)
                    return PHASE.RESET_ROUND;
                else if(!chooseAssistantsCheck)
                    return PHASE.CHARACTER_ACTION;
                break;
            case CHARACTER_ACTION:
                if(characterActionCheck)
                    return PHASE.RESET_ROUND;
                break;
            case RESET_ROUND:
                reset();
                return PHASE.PLANNING;
        }
        return currentPhase;
    }

    /**
     * After every turn some indicators must be reset in order to track the next Player's actions
     */
    public void reset(){
        moveMNCheck=false;
        moveStudentsCheck=false;
        chooseAssistantsCheck=false;
        chooseCloudCheck=false;
        characterActionCheck=true;
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
}
