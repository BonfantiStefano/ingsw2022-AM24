package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.profstrategy.StandardProf;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.World;

import java.util.*;

public class GameBoard implements HasStrategy<ProfStrategy> {
    private final static String ERROR_CARD = "Attention: This card has been chosen by another player! Choose another card";
    private final static String MESSAGE = "choose an Assistant card";

    private MotherNature mn;
    private List<Player> players;
    private List<Cloud> clouds;
    private List<Assistant> lastAssistants;
    private World world;
    private Player activePlayer;
    private StudentContainer bag;
    private ProfStrategy strategy;
    private Map<ColorS, Player> profs;

    private Random random = new Random();

    public GameBoard(){
        mn = new MotherNature();
        int randomPos = random.nextInt(12);
        mn.setLocation(world.getIsland(randomPos));

        World world = new World(randomPos, randomPos+6);

        profs=new HashMap<ColorS, Player>();
        for(ColorS c:ColorS.values()){
            profs.put(c,null);
        }

        clouds = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            clouds.add(new Cloud());
        }
    }

    public void setChoosenAssistant(Player player, int index){
        player.chooseAssistant(index);
    }

    public Assistant getChoosenAssistant(Player player){
        return player.getLastAssistant();
    }

    /**
     * Method playAssistans allows each player to choose an Assistant card
     * and makes sure that no one plays the same card someone else has played in the round
     */
    public void playAssistants() {
        for (Player p : players) {
            boolean finish = false;
            do {
                System.out.println(p.getNickname() + ", " + MESSAGE);
                Assistant assistant = getChoosenAssistant(p);

                for (Assistant a : lastAssistants) {
                    if ((!a.equals(assistant)) || p.getMyCards().numCards() == 1) {
                        finish = false;
                        System.out.println(ERROR_CARD);
                        break;
                    }
                }
            } while (!finish);
        }
    }

    /**
     * Method nthPlayer is used for sorting palyers by their card value that determines the turn order
     * of the next round. The player that has the lowest card value stars the round followed by other
     * players.
     * @param n int (n=1 the first player of the round, n=2 the second one, ecc)
     */
    public void nthPlayer(int n){
        int index = n-1;
        Player p = null;
        List<Player> orderedPlayers = new ArrayList<>();
        Collections.copy(orderedPlayers,players);
        Collections.sort(orderedPlayers, (p1, p2) -> {
            if (p1.getLastAssistant().getTurn() < p2.getLastAssistant().getTurn()) return 1;
            else if (p1.getLastAssistant().getTurn() > p2.getLastAssistant().getTurn()) return -1;
            else return 0;
        });
        p = orderedPlayers.get(index);
        setActivePlayer(p);
    }

    public void checkIsland(Island i){}


    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    //implementare varie move
    public void move(Student s, CanRemoveStudent from, CanAcceptStudent to){
        from.remove(s);
        to.add(s);
    }

    public ArrayList<Assistant> getLastAssistants() {
        return null;
    }

    @Override
    public void setStrategy(ProfStrategy strategy){
        this.strategy=strategy;
    }

    @Override
    public void resetStrategy() {
        strategy=new StandardProf();
    }


}