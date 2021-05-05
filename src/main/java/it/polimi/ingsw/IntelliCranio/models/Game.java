package it.polimi.ingsw.IntelliCranio.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Game implements Serializable {

    private UUID uuid;

    private int currentPlayerIndex; //0 - 3: The index of the player turn
    private FaithTrack faithTrack;
    private CardMarket cardMarket;
    private ResourceMarket resourceMarket;
    private ArrayList<Player> players = new ArrayList<>();

    public Game () {

    }

    public Game(ArrayList<String> nicknames) {

        uuid = UUID.randomUUID();

        currentPlayerIndex = 0;

        faithTrack = new FaithTrack("src/main/resources/faithtrack_config.json");
        cardMarket = new CardMarket("src/main/resources/devcards_config.json", 3, 4, true);
        resourceMarket = new ResourceMarket(3, 4);

        nicknames.forEach(nick -> players.add(new Player(nick, 3, 3)));

        createLeaderCards("src/main/resources/leadcards_config.json", true);

        shufflePlayers();

    }


    //region GETTERS


    public UUID getUuid() {
        return uuid;
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

    /**
     * Get the faithtrack.
     * @return This faithtrack
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Get the CardMarket
     * @return This cardmarket
     */
    public CardMarket getCardMarket() {
        return cardMarket;
    }

    /**
     * Get the resource market
     * @return This resource market
     */
    public ResourceMarket getResourceMarket() {
        return resourceMarket;
    }

    /**
     * Return the list of all players
     * @return A list of Player
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Get the player that is actually playing its turn
     * @return A single Player. The active one in this turn.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Get the index of the active player
     * @return An integer representing the index in the list of players.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    //endregion

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

        leaders.forEach(LeadCard::setupAbility);

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

    /**
     * Randomize the list of players defining the game turns.
     * Must be called before the setup.
     */
    public void shufflePlayers() {
        ArrayList<Player> temp = new ArrayList<>(); //Temporary array for randomized players
        Random random = new Random();

        do{
            int randIndex = random.ints(0, players.size()).findFirst().getAsInt();

            temp.add(players.get(randIndex));
            players.remove(randIndex);
        } while (players.size() > 0);

        players = temp;
    }

    public void addPlayerFaith(Player player) {
        //Increment player Faith
        //Check every increment the position of the player on the faith track

        player.addFaith();

        //Todo: Gestione posizioni e fine del gioco

        throw new UnsupportedOperationException();
    }

    public void addFaithToAll(int faithAmount) {

        for(int i = 0; i < faithAmount; ++i) {
            //Increment faith for every player other than current one
            players.stream()
                    .filter(player -> !player.equals(getCurrentPlayer()))
                    .forEach(Player::addFaith);

            //Check the positions on the faithtrack
            faithTrack.checkStatus(players);
        }
    }

    //Remove me
    public void addPlayer(Player player, int turn) {
        //Bisogna fare un check sul numero di giocatori presenti
        players.add(turn, player);
    }

    public void changeTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }


}
