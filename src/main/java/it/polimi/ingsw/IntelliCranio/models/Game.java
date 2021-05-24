package it.polimi.ingsw.IntelliCranio.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.IntelliCranio.models.cards.Card;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.cards.PopeCard;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.util.Save;
import javafx.util.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.models.cards.PopeCard.Status.*;
import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

public class Game implements Serializable {

    private UUID uuid;

    private int currentPlayerIndex; //0 - 3: The index of the player turn
    private FaithTrack faithTrack;
    private CardMarket cardMarket;
    private ResourceMarket resourceMarket;
    private ArrayList<Player> players = new ArrayList<>();

    private boolean endTurn = false;
    private boolean endGame = false;
    private boolean singlePlayer = false;

    private SinglePlayerData singlePlayerData;

    public Game() {
    }

    public Game(ArrayList<String> nicknames) {

        uuid = UUID.randomUUID();

        currentPlayerIndex = 0;

        faithTrack = Save.getDatabase("faithtrack_config.json", FaithTrack.class);
        cardMarket = new CardMarket("devcards_config.json", 3, 4, true);
        resourceMarket = new ResourceMarket(3, 4);

        nicknames.forEach(nick -> players.add(new Player(nick, 3, 3)));

        createLeaderCards("leadcards_config.json", true);

        if (players.size() == 1) {
            singlePlayerData = new SinglePlayerData();
            singlePlayer = true;
        } else
            shufflePlayers();

    }


    //region GETTERS


    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get the amount of resources a player can take at the start of
     * the game given the current player index
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
     * Get the amount of resources a player has at the start of
     * the game given the current player index
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

    /**
     * Get a player providing a nickname
     *
     * @param nickname The nickname to look for
     * @return The player if nickname match, null otherwise
     */
    public Player getPlayer(String nickname) {
        try {
            return players.stream().filter(x -> x.getNickname().equals(nickname)).findAny().get();
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * Get the player that is actually playing its turn
     *
     * @return A single Player. The active one in this turn.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isTurnEnded() {
        return endTurn;
    }

    public boolean isGameEnded() {
        return endGame;
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    public SinglePlayerData getSinglePlayerData() {
        return singlePlayerData;
    }
    //endregion

    /**
     * Creates all the cards from given json file and assign 4 of them randomly to each player.
     *
     * @param filename    The filename of the json config file
     * @param shuffle If true, cards get shuffled
     */

    public void createLeaderCards(String filename, boolean shuffle) {

        ArrayList<LeadCard> leaders = Save.getDatabase(filename, new TypeToken<ArrayList<LeadCard>>() {}.getType());

        leaders.forEach(LeadCard::setupAbility);

        assignCards(leaders, shuffle);
    }

    /**
     * Called inside createLeaderCards method.
     * <p>
     * Assign 4 random cards from the input to each player.
     * </p>
     *
     * @param cards   The input list to get cards from
     * @param shuffle Whether to shuffle the cards or not
     */
    private void assignCards(ArrayList<LeadCard> cards, boolean shuffle) {

        players.forEach(player -> {
            ArrayList<LeadCard> playerCards = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < 4; ++i) {
                if (shuffle) {
                    int index = random.ints(0, cards.size()).findFirst().getAsInt();
                    playerCards.add(cards.get(index));
                    cards.remove(index);
                } else {
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

        do {
            int randIndex = random.ints(0, players.size()).findFirst().getAsInt();

            temp.add(players.get(randIndex));
            players.remove(randIndex);
        } while (players.size() > 0);

        players = temp;
    }

    /**
     * Increment the position in the faithtrack of current player and check for the updated position
     */
    public void addCurrentPlayerFaith() {
        //Increment player Faith
        //Check every increment the position of the player on the faith track
        Player player = getCurrentPlayer();

        if (player.getFaithPosition() < faithTrack.getTrackLength())
            getCurrentPlayer().addFaith();

        faithTrack.checkStatus(this, player.getFaithPosition());
    }

    /**
     * Increment by 1 the faith position of every player different from current one.
     * Check for all the new positions.
     * Repeat for faithAmount times
     *
     * @param faithAmount The total amount of faith to add
     */
    public void addFaithToOthers(int faithAmount) {

        for (int i = 0; i < faithAmount; ++i) {
            //Increment faith for every player other than current one
            players.stream()
                    .filter(player -> (!player.equals(getCurrentPlayer()) && player.getFaithPosition() < faithTrack.getTrackLength()))
                    .forEach(Player::addFaith);

            //Check the positions on the faithtrack
            players.stream()
                    .map(Player::getFaithPosition)
                    .forEach(pos -> faithTrack.checkStatus(this, pos));
        }
    }

    /**
     * Calculate all the points scored by each player and return the result
     * in a table-like form.
     *
     * @return A list of pair nickname - points, the result of the game
     */
    public ArrayList<Pair<String, Integer>> calculatePoints() {

        ArrayList<Pair<String, Integer>> tablePoints = new ArrayList<>();

        for (Player player : players) {
            int points = 0;
            //int numResources = 0;

            ArrayList<LeadCard> leadCards = player.getLeaders();
            ArrayList<Resource> resources = player.getAllResources();
            ArrayList<DevCard> devCards = player.getAllDevCards();
            ArrayList<PopeCard> popeCards = player.getPopeCards();

            points += leadCards.stream()
                    .filter(LeadCard::isActive)
                    .mapToInt(Card::getVictoryPoints).sum();

            for (DevCard devCard : devCards) points += devCard.getVictoryPoints();

            points += popeCards.stream()
                    .filter(popeCard -> popeCard.getStatus().equals(ACTIVE))
                    .mapToInt(Card::getVictoryPoints).sum();

            points += faithTrack.getVp(player.getFaithPosition());

            points += resources.stream().mapToInt(FinalResource::getAmount).sum() / 5;

            /*
            for (int i = 0; i < player.getLeaders().size(); i++)
                if (player.getLeaders().get(i).isActive())
                    points += player.getLeaders().get(i).getVictoryPoints();

            for (PopeCard popeCard : popeCards)
                if (popeCard.getStatus().equals(PopeCard.Status.ACTIVE))
                    points += popeCard.getVictoryPoints();

            for (Resource resource : resources) numResources += resource.getAmount();

            numResources = numResources / 5;

            points += numResources;
            */

            tablePoints.add(new Pair<>(player.getNickname(), points));
        }

        Comparator<Pair<String, Integer>> descendingOrder = reverseOrder(comparing(Pair::getValue));

        tablePoints.sort(descendingOrder);

        return tablePoints;
    }

    public void singlePlayer(boolean value) {
        singlePlayer = value;
    }

    public void endTurn(boolean value) {
        endTurn = value;
    }

    public void changeTurn() {
        if (singlePlayer)
            lorenzoAction();

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        endTurn = false;
    }

    public void endGame(boolean value) {
        endGame = value;
    }

    /**
     * Set every field with the one from a previously saved game
     * @param newGame The game loaded from save file
     */
    public void loadGame(Game newGame) {
        uuid = newGame.getUuid();
        currentPlayerIndex = newGame.getCurrentPlayerIndex();
        players = newGame.getPlayers();
        faithTrack = newGame.getFaithTrack();
        resourceMarket = newGame.getResourceMarket();
        cardMarket = newGame.getCardMarket();
        endTurn = newGame.isTurnEnded();
        endGame = newGame.isGameEnded();
        singlePlayer = newGame.isSinglePlayer();
        singlePlayerData = newGame.getSinglePlayerData();
    }

    /**
     * Execute Lorenzo the Magnificent action at the end of each single player turn
     * by taking a token and performing the token action.
     */
    private void lorenzoAction() {
        SinglePlayerData.Token token = singlePlayerData.getToken();

        switch (token) {

            case BLACK_CROSS:
                addLorenzoFaith(2);
                break;
            case SHUFFLE_CROSS:
                addLorenzoFaith(1);
                break;
            default:
                removeCards(token, 2);
                break;

        }
    }

    /**
     * Remove amount of cards from card market of the type specified by the token
     * @param token The token extracted
     * @param amount The amount of cards to remove
     */
    private void removeCards(SinglePlayerData.Token token, int amount) {

        int col;

        //Find correct column for type
        for (col = 0; col < cardMarket.cols; ++col) {
            if (token.toString().equals(cardMarket.getCard(0, col).getType().toString()))
                break;
        }

        int row = cardMarket.rows - 1;

        for (int i = 0; i < amount; ++i) {

            if (cardMarket.getCard(row, col) != null) {
                cardMarket.removeCard(row, col);

                if (cardMarket.getCard(0, col) == null)
                    endGame = true;
            } else {
                row--;
                i--;
            }
        }

    }

    /**
     * Add faith to Lorenzo
     * @param amount Faith to add
     */
    private void addLorenzoFaith(int amount) {

        int ftLength = faithTrack.getTrackLength();

        for (int i = 0; i < amount; ++i) {
            if (singlePlayerData.getLorenzoFaith() < ftLength) {
                singlePlayerData.addLorenzoFaith();
                faithTrack.checkStatus(this, singlePlayerData.getLorenzoFaith());
            }
        }
    }
}
