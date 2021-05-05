package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DISCARD_INIT_LEAD;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DISCARD_LEAD;
import static java.lang.Integer.parseInt;

public class CliDiscardInitLeadersScene implements CliScene {

    public void displayOptions() {
        Game game = MainClient.game;

        System.out.println("Discard one of your Leader cards: ");
        ArrayList<LeadCard> leaders = game.getCurrentPlayer().getLeaders();
        leaders.forEach(x -> {
            System.out.println((leaders.indexOf(x) + 1) + ") " + x.getID());
        });
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        Game game = MainClient.game;

        if (input.size() > 1)
            throw new InvalidArgumentsException("ERROR: you must input only one argument");

        int max = game.getCurrentPlayer().getLeaders().size();
        int number = 0;
        try {
            number = parseInt(input.get(0));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException("ERROR: you must input a number");
        }

        if (number < 1 || number > max)
            throw new InvalidArgumentsException("ERROR: you must input a number between 1 and " + max);

        return new Pair<>(
                DISCARD_LEAD,
                new ArrayList<>(Arrays.asList(game.getCurrentPlayer().getLeaders().get(parseInt(input.get(0)) - 1))));
    }
}
