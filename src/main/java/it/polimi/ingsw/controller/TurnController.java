package it.polimi.ingsw.controller;

public class TurnController {
    private boolean moveStudentsCheck;
    private boolean moveMNCheck;
    private boolean chooseCloudCheck;
    private boolean chooseAssistantsCheck;
    private boolean characterActionCheck;
    private boolean gameStarted;

    public TurnController(){
        moveMNCheck=false;
        moveStudentsCheck=false;
        chooseAssistantsCheck=false;
        chooseCloudCheck=false;
        gameStarted=false;
        characterActionCheck = true;
    }

    public PHASE nextPhase(PHASE currentPhase){
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
                break;
            case RESET_ROUND:
                reset();
                return PHASE.PLANNING;
        }
        return currentPhase;
    }

    public void reset(){
        moveMNCheck=false;
        moveStudentsCheck=false;
        chooseAssistantsCheck=false;
        chooseCloudCheck=false;
        characterActionCheck=true;
    }

    public boolean isCharacterActionCheck() {
        return characterActionCheck;
    }

    public void setCharacterActionCheck(boolean characterActionCheck) {
        this.characterActionCheck = characterActionCheck;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void setMoveStudentsCheck(boolean moveStudentsCheck) {
        this.moveStudentsCheck = moveStudentsCheck;
    }

    public void setMoveMNCheck(boolean moveMNCheck) {
        this.moveMNCheck = moveMNCheck;
    }

    public void setChooseCloudCheck(boolean chooseCloudCheck) {
        this.chooseCloudCheck = chooseCloudCheck;
    }

    public void setChooseAssistantsCheck(boolean chooseAssistantsCheck) {
        this.chooseAssistantsCheck = chooseAssistantsCheck;
    }
}
