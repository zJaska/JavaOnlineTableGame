package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class CliDefaultScene implements CliScene {

    // WARNING: the order of codes in the array must be equal to the order of the actions in the menu
    ArrayList<InstructionCode> instructionCodes = new ArrayList<>(Arrays.asList(
            MNG_WARE, CARD_MARKET, RES_MARKET, ACT_PROD, END_TURN
    ));

    public void displayOptions() {
        System.out.println("Choose an action to take: ");
        System.out.println("-) playLeader <number>");
        System.out.println("-) discardLeader <number>");
        System.out.println("1) Manage your WAREHOUSE");
        System.out.println("2) Go to the CARD MARKET");
        System.out.println("3) Go to the RESOURCE MARKET");
        System.out.println("4) Activate the PRODUCTION");
        System.out.println("5) Terminate your turn");
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        Game game = MainClient.getGame();

        switch (input.get(0)) {
            case "playLeader":
                if (input.size() == 1)
                    System.out.println("Choose the leader card to play (numbered from 1)");
                else {
                    int num = CliUtil.checkInt(input.get(1), 1, game.getCurrentPlayer().getLeaders().size());
                    return new Pair<>(
                            PLAY_LEADER,
                            new ArrayList<>(Arrays.asList(game.getCurrentPlayer().getLeaders().get(num - 1))));
                }
                break;

            case "discardLeader":
                if (input.size() == 1)
                    new CliDiscardLeaderScene().displayOptions();
                else
                    return new CliDiscardLeaderScene().createData(new ArrayList<>(Arrays.asList(input.get(1))));
                break;

            default:
                if (input.size() > 1)
                    throw new InvalidArgumentsException("ERROR: you must input only 1 argument");

                int num = CliUtil.checkInt(input.get(0),1, instructionCodes.size());
                return new Pair<>(instructionCodes.get(num - 1), new ArrayList<>());
        }

        return null;
    }
}
