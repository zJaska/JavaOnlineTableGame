package it.polimi.ingsw.IntelliCranio.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.IntelliCranio.server.ability.*;
import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.server.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.server.player.Player;
import it.polimi.ingsw.IntelliCranio.server.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class GameManager implements Runnable{

    private int currentPlayer;
    private FaithTrack faithTrack;
    private CardMarket cardMarket;
    private ResourceMarket resourceMarket;
    private ArrayList<Player> players = new ArrayList<>();

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
        return players.get(currentPlayer);
    }

}
