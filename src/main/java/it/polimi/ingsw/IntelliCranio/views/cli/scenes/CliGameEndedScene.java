package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;

public class CliGameEndedScene implements CliScene {

    public void displayOptions() {
        boolean won = false;
        ArrayList<Pair<String, Integer>> table = MainClient.getGame().calculatePoints();

        if (MainClient.getGame().isSinglePlayer()) {
            if (MainClient.getGame().getPlayer(MainClient.getNickname()).getFaithPosition() >= MainClient.getGame().getFaithTrack().getTrackLength())
                won = true;
        } else if (table.get(0).getKey().equals(MainClient.getNickname()))
            won = true;

        if (won)
            System.out.println("VICTORY");
        else
            System.out.println("DEFEAT");

        for (int i=0; i<table.size(); i++)
            System.out.println((i+1) + ") " + table.get(i).getKey() + " with " + table.get(i).getValue() + " points!");
    }

    public Pair<Packet.InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        return null;
    }
}
