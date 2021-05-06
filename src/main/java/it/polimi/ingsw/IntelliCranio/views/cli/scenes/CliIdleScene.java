package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard.CardType;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Strongbox;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.BLANK;
import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.FAITH;

public class CliIdleScene implements CliScene {

    public static final String[] IDLE_COMMANDS = new String[] {
      "/help", "/showWarehouse", "/showStrongbox", "/showCardMarket", "/showResourceMarket",
            "/showFaithTrack", "/display", "/resourceTypes", "/showLeaders"
    };

    private static void help(ArrayList<String> input) {
        System.out.println("You can use the following commands: ");
        for (String x : IDLE_COMMANDS)
            System.out.println(x);
    }

    private static void display(ArrayList<String> input, CliScene scene) {
        scene.displayOptions();
    }

    public static void showWarehouse() {
        Game game = MainClient.game;

        System.out.println("Warehouse:");
        Resource[] resources = game.getPlayer(MainClient.nickname).getWarehouse().getDepot();
        int cont = 1;
        for (Resource res : resources)
            System.out.println(cont++ + ") " + (res == null ? "empty" : res));
    }

    public static void showStrongbox() {
        Game game = MainClient.game;
        Strongbox box = game.getPlayer(MainClient.nickname).getStrongbox();

        System.out.println("Strongbox: ");
        box.getAll().forEach(System.out::println);
    }

    public static void showResourceMarket() {
        System.out.println("Resource market: ");

        ResourceMarket market = MainClient.game.getResourceMarket();
        FinalResource[][] grid = market.getGridCopy();

        for (int i=0; i<market.COLUMNS; i++)
            System.out.print("\t" + (i+1));
        System.out.println();

        for (int i=0; i<market.ROWS; i++) {
            ArrayList<FinalResource> resources = market.selectRow(i);
            System.out.print((i+1) + "\t");
            for (int j=0; j<market.COLUMNS; j++)
                System.out.print(grid[i][j].getType().toString().substring(0,2) + "\t");
            System.out.println();
        }
        System.out.println("Extra marble: " + market.getExtraMarbleCopy());
    }

    public static void showCardMarket() {
        Game game = MainClient.game;
        CardMarket market = game.getCardMarket();

        final int FIRST = 15;

        System.out.println("Card Market: ");
        System.out.print(format("",FIRST));

        Arrays.asList(CardType.values()).forEach(x -> System.out.print(format(x.toString())));
        System.out.println();

        for (int i=0; i<market.rows; i++) {

            // r is the index that splits a level into 3 lines
            for (int r=0; r<3; r++) {
                if (r == 1)
                    System.out.print(format("Level " + (market.rows-i), FIRST));
                else
                    System.out.print(format("",FIRST));

                for (int j=0; j<market.cols; j++) {
                    if (market.getCard(i,j) == null) {
                        System.out.print(format(""));
                        continue;
                    }

                    if (r == 0)
                        System.out.print(format(String.valueOf(market.getCard(i,j).getVictoryPoints())));
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

    private static String format (String x) {
        String space = "                                                       ";
        return x.concat(space).substring(0,40);
    }
    private static String format (String x, int num) {
        String space = "                                                       ";
        return x.concat(space).substring(0,num);
    }

    public static void showFaithTrack() {
        Game game = MainClient.game;


    }

    public static void showLeaders() {
        Game game = MainClient.game;

        System.out.println("Leader cards: ");
        ArrayList<LeadCard> leaders = game.getPlayer(MainClient.nickname).getLeaders();
        leaders.forEach(x -> {
            System.out.println((leaders.indexOf(x) + 1) + ") " + x);
        });
    }

    public static void showResourceTypes() {
        System.out.println("Resource types:");
        Arrays.stream(FinalResource.ResourceType.values())
                .filter(x -> !Arrays.asList(FAITH, BLANK).contains(x))
                .forEach(x -> System.out.print(x + "  "));
        System.out.println();
    }

    public static void displayIdleCommand(ArrayList<String> input, CliScene scene) {
        switch (input.get(0)) {
            case "/help":
                help(input);
                break;
            case "/display":
                display(input,scene);
                break;
            case "/showWarehouse":
                showWarehouse();
                break;
            case "/showStrongbox":
                showStrongbox();
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
                showLeaders();
                break;
        }
    }

    public void displayOptions() { System.out.println("It's not your turn"); }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) { return null; }
}
