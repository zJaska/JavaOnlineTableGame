package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;

public class CliGameEndedScene implements CliScene {

    public void displayOptions() {
        System.out.println("GAME FINISHED!");
        ArrayList<Pair<String, Integer>> table = MainClient.game.calculatePoints();

        for (int i=0; i<table.size(); i++)
            System.out.println((i+1) + ") " + table.get(i).getKey() + " with " + table.get(i).getValue() + " points!");
    }

    public Pair<Packet.InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        return null;
    }
}
