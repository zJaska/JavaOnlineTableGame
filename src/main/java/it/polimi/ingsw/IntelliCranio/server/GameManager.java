package it.polimi.ingsw.IntelliCranio.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.server.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.server.player.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class GameManager implements Runnable{

    private int currentPlayerIndex; //0 - 3: The index of the player turn
    private FaithTrack faithTrack;
    private CardMarket cardMarket;
    private ResourceMarket resourceMarket;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<String> lastActionReturnArgs;

    @Override
    public void run() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates all the cards from given json file and assign 4 of them randomly to each player.
     * @param path The path of the leader cards config file
     * @param shuffle If true, cards get shuffled
     */
    public void createLeaderCards(String path, boolean shuffle) {

        ArrayList<LeadCard> leaders = new ArrayList<>();

        //Get the json file as String
        try {
            String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            //Generate the list of cards
            leaders = gson.fromJson(text, new TypeToken<ArrayList<LeadCard>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        leaders.forEach(card -> {
            card.setupAbility(card.getAbilityType(), card.getResourceType());
        });

        assignCards(leaders, shuffle);

    }

    /**
     * Called inside createLeaderCards method.
     * <p>
     *     Assign 4 random cards from the input to each player.
     * </p>
     * @param cards The input list to get cards from
     */
    private void assignCards(ArrayList<LeadCard> cards, boolean shuffle) {

        players.forEach(player -> {
            ArrayList<LeadCard> playerCards = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < 4; ++i) {
                if(shuffle) {
                    int index = random.ints(0, cards.size()).findFirst().getAsInt();
                    playerCards.add(cards.get(index));
                    cards.remove(index);
                }else {
                    playerCards.add(cards.get(0));
                    cards.remove(0);
                }
            }
            player.setLeaders(playerCards);
        });

    }

    private void shufflePlayers() {
        throw new UnsupportedOperationException();
    }

    private void playerSetup() {
        throw new UnsupportedOperationException();
    }

    private void playGame() {
        throw new UnsupportedOperationException();
    }

    private void endingGame() {
        throw new UnsupportedOperationException();
    }

    public ArrayList<String> getLastActionReturnArgs() {
        return lastActionReturnArgs;
    }

    //Test only
    public void setLastActionReturnArgs(ArrayList<String> lastActionReturnArgs) {
        this.lastActionReturnArgs = lastActionReturnArgs;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     *  Get the amount of resources a player can take at the start of
     *  the game given the current player index
     *
     * @param index The index of the player is currently playing
     * @return The amount of resources a player can take at the start of the game
     */
    public int getInitRes(int index) {
        ArrayList<Integer> table = new ArrayList<>();

        table.add(0);
        table.add(1);
        table.add(1);
        table.add(2);

        return table.get(index);
    }

    /**
     *  Get the amount of resources a player has at the start of
     *  the game given the current player index
     *
     * @param index The index of the player is currently playing
     * @return The amount of faith a player has at the start of the game
     */
    public int getInitFaith(int index) {
        ArrayList<Integer> table = new ArrayList<>();

        table.add(0);
        table.add(0);
        table.add(1);
        table.add(1);

        return table.get(index);
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public CardMarket getCardMarket() {
        return cardMarket;
    }

    public ResourceMarket getResourceMarket() {
        return resourceMarket;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player, int turn) {
        players.add(turn, player);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

}
