package it.polimi.ingsw.client.GUIView;

/**
 * Enum to collect all Image Paths
 */
public enum IMAGE_PATHS {
    MAIN_BACKGROUND("/graphics/Background.jpg"),
    BOARD("/graphics/Plancia_DEF.png"),
    ISLAND("/graphics/island1.png"),
    MN("/graphics/wooden_pieces/mother_nature.png"),
    NO_ENTRY("/graphics/wooden_pieces/deny_island_icon.png"),
    CLOUD("/graphics/wooden_pieces/cloud_card.png"),
    ASSISTANT("/graphics/Assistants/Animali_1_"),
    COIN("/graphics/wooden_pieces/Moneta_base.png"),
    STUDENT("/graphics/wooden_pieces/student_"),
    PROF("/graphics/wooden_pieces/teacher_"),
    TOWER("/graphics/wooden_pieces/"),
    CHARACTER("/graphics/Characters/CarteTOT_front"),
    WELCOME_BACKGROUND("/graphics/WelcomeBackground.png"),
    ;

    private final String path;

    /**
     * Image path information
     * @param path String with the Image's path
     */
    IMAGE_PATHS(String path) {
        this.path = path;
    }

    /**
     * Get the Image's path
     * @return the Image's path
     */
    public String toString(){
        return path;
    }
}
