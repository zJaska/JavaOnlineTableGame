package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard.CardType;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.BLANK;
import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.FAITH;

public class CliIdleScene implements CliScene {

    public static final String[] IDLE_COMMANDS = new String[]{
            "/help", "/showWarehouse", "/showStrongbox", "/showCardMarket", "/showResourceMarket",
            "/showFaithTrack", "/display", "/resourceTypes", "/showLeaders", "/showDevCards"
    };

    private static void help(ArrayList<String> input) {
        System.out.println("You can use the following commands: ");
        for (String x : IDLE_COMMANDS)
            System.out.println(x);
    }

    private static void display(ArrayList<String> input, CliScene scene) {
        scene.displayOptions();
    }

    /**
     * @param input The command the calls the method. If null, shows the player's warehouse.
     *              Otherwise, input.get(1) should be the nickname of a player.
     */
    public static void showWarehouse(ArrayList<String> input) throws InvalidArgumentsException {
        _showWarehouse(CliUtil.checkPlayerHelpCommands(input, MainClient.game));
    }

    public static void showWarehouse() {
        _showWarehouse(MainClient.game.getPlayer(MainClient.nickname));
    }

    private static void _showWarehouse(Player player) {
        System.out.println("Warehouse (" + player.getNickname() + "):");
        Resource[] resources = player.getWarehouse().getDepot();
        int cont = 1;
        for (Resource res : resources)
            System.out.println(cont++ + ") " + (res == null ? "empty" : res));

        System.out.print("Extra resources: ");
        player.getExtraRes().forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    /**
     * @param input The command that calls the method. input.get(1) should be the nickname of a player.
     */
    public static void showStrongbox(ArrayList<String> input) throws InvalidArgumentsException {
        _showStrongbox(CliUtil.checkPlayerHelpCommands(input, MainClient.game));
    }

    public static void showStrongbox() {
        _showStrongbox(MainClient.game.getPlayer(MainClient.nickname));
    }

    private static void _showStrongbox(Player player) {
        System.out.println("Strongbox (" + player.getNickname() + "): ");
        player.getStrongbox().getAllResources().forEach(System.out::println);
    }

    public static void showResourceMarket() {
        System.out.println("Resource market: ");

        ResourceMarket market = MainClient.game.getResourceMarket();
        FinalResource[][] grid = market.getGridCopy();

        for (int i = 0; i < market.COLUMNS; i++)
            System.out.print("\t" + (i + 1));
        System.out.println();

        for (int i = 0; i < market.ROWS; i++) {
            System.out.print((i + 1) + "\t");
            for (int j = 0; j < market.COLUMNS; j++)
                System.out.print(grid[i][j].getType().toString().substring(0, 2) + "\t");
            System.out.println();
        }
        System.out.println("Extra marble: " + market.getExtraMarbleCopy());
    }

    public static void showCardMarket() {
        Game game = MainClient.game;
        CardMarket market = game.getCardMarket();

        final int FIRST = 15;

        System.out.println("Card Market: ");
        System.out.print(format("", FIRST));

        Arrays.asList(CardType.values()).forEach(x -> System.out.print(format(x.toString())));
        System.out.println();

        for (int i = 0; i < market.rows; i++) {

            // r is the index that splits a level into 3 lines
            for (int r = 0; r < 3; r++) {
                if (r == 1)
                    System.out.print(format("Level " + (market.rows - i), FIRST));
                else
                    System.out.print(format("", FIRST));

                for (int j = 0; j < market.cols; j++) {
                    if (market.getCard(i, j) == null) {
                        System.out.print(format(""));
                        continue;
                    }

                    if (r == 0)
                        System.out.print(format(String.valueOf(market.getCard(i, j).getVictoryPoints())));
                    if (r == 1) {
                        String tmp = "";
                        for (FinalResource x : market.getCard(i, j).getCardCost())
                            tmp += x + " ";
                        System.out.print(format(tmp));
                    }

                    if (r == 2) {
                        String tmp = "";
                        for (FinalResource x : market.getCard(i, j).getProductionCost())
                            tmp += (x + " ");
                        tmp += " -> ";
                        for (FinalResource x : market.getCard(i, j).getProduct())
                            tmp += (x + " ");
                        System.out.print(format(tmp));
                    }
                    System.out.print("");
                }
                System.out.println();
            }

        }
    }

    private static String format(String x) {
        String space = "                                                       ";
        return x.concat(space).substring(0, 40);
    }

    private static String format(String x, int num) {
        String space = "                                                       ";
        return x.concat(space).substring(0, num);
    }

    public static void showFaithTrack() {
        Game game = MainClient.game;

        System.out.println("Faith track:");

        game.getPlayers().forEach(x -> {
            System.out.println(x.getNickname() + ": \t{" +
                    "index: " + x.getFaithPosition() + ",\t" +
                    "pope cards: [" + x.getPopeCards().stream().map(card -> card.getStatus().toString()).reduce("", (s1, s2) -> s1 + " " + s2) + " ]\t");
        });

        System.out.print("Pope spaces: ");
        game.getFaithTrack().getPopeSpacesCopy().forEach(x -> System.out.print(x + " "));
        System.out.println();

        System.out.print("Start of vatican sections: ");
        game.getFaithTrack().getStartOfVaticanSectionsCopy().forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    /**
     * @param input The command the calls the method. If null, shows the player's warehouse.
     *              Otherwise, input.get(1) should be the nickname of a player.
     */
    public static void showLeaders(ArrayList<String> input) throws InvalidArgumentsException {
        _showLeaders(CliUtil.checkPlayerHelpCommands(input, MainClient.game));
    }

    public static void showLeaders() {
        _showLeaders(MainClient.game.getPlayer(MainClient.nickname));
    }

    private static void _showLeaders(Player player) {
        ArrayList<LeadCard> leaders = player.getLeaders();
        System.out.println("Leader cards (" + player.getNickname() + "): (requirements: {type, amount, level})");
        leaders.forEach(x -> {
            if (player.getNickname().equals(MainClient.nickname) || x.isActive())
                System.out.println((leaders.indexOf(x) + 1) + ") " + x);
        });
    }

    public static void showDevCards(ArrayList<String> input) throws InvalidArgumentsException {
        _showDevCards(CliUtil.checkPlayerHelpCommands(input, MainClient.game));
    }

    public static void showDevCards() {
        _showDevCards(MainClient.game.getPlayer(MainClient.nickname));
    }

    private static void _showDevCards(Player player) {
        System.out.println("Development cards (" + player.getNickname() + "):");
        int i = 1;
        for (DevCard card : player.getFirstDevCards()) {
            System.out.println(i++ + ") " + card);
        }
    }

    public static void showResourceTypes() {
        System.out.println("Resource types:");
        Arrays.stream(FinalResource.ResourceType.values())
                .filter(x -> !Arrays.asList(FAITH, BLANK).contains(x))
                .forEach(x -> System.out.print(x + "  "));
        System.out.println();
    }

    public static void displayIdleCommand(ArrayList<String> input, CliScene scene) throws InvalidArgumentsException {
        switch (input.get(0)) {
            case "/help":
                help(input);
                break;
            case "/display":
                display(input, scene);
                break;
            case "/showWarehouse":
                showWarehouse(input);
                break;
            case "/showStrongbox":
                showStrongbox(input);
                break;
            case "/showResourceMarket":
                showResourceMarket();
                break;
            case "/showCardMarket":
                showCardMarket();
                break;
            case "/showFaithTrack":
                showFaithTrack();
                break;
            case "/resourceTypes":
                showResourceTypes();
                break;
            case "/showLeaders":
                showLeaders(input);
                break;
            case "/showDevCards":
                showDevCards(input);
                break;
        }
    }

    public void displayOptions() {
        System.out.println("It's not your turn");
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) {
        return null;
    }
}
