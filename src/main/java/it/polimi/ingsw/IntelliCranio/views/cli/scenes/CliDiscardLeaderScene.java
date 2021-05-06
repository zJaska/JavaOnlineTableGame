package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DISCARD_INIT_LEAD;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DISCARD_LEAD;
import static java.lang.Integer.parseInt;

public class CliDiscardLeaderScene implements CliScene {

    public void displayOptions() {
        System.out.println("Discard one of your Leader cards (numbered from 1): ");
        CliIdleScene.showLeaders();
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        Game game = MainClient.game;

        if (input.size() > 1)
            throw new InvalidArgumentsException("ERROR: you must input only one argument");

        int num = CliUtil.checkInt(input.get(0),1, game.getCurrentPlayer().getLeaders().size());

        return new Pair<>(
                DISCARD_LEAD,
                new ArrayList<>(Arrays.asList(game.getCurrentPlayer().getLeaders().get(num - 1))));
    }
}
