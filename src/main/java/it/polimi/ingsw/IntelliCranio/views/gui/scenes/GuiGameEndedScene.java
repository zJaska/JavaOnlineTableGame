package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

public class GuiGameEndedScene extends GuiScene {

    public GuiGameEndedScene(Parent parent) {
        super(parent);

        ArrayList<Pair<String, Integer>> table = MainClient.getGame().calculatePoints();
        Label wonlabel = (Label) GuiUtil.getNodeById(parent, "won");

        boolean won = false;

        if (MainClient.getGame().isSinglePlayer()) {
            if (MainClient.getGame().getPlayer(MainClient.getNickname()).getFaithPosition() >= MainClient.getGame().getFaithTrack().getTrackLength())
                won = true;
        } else if (table.get(0).getKey().equals(MainClient.getNickname()))
                won = true;

        if (won)
            wonlabel.setText("VICTORY");
        else
            wonlabel.setText("DEFEAT");

        ArrayList<Node> players = GuiUtil.getNodesStartingWithId(parent, "player");
        players.sort(Comparator.comparing(Node::getId));
        ArrayList<Node> results = GuiUtil.getNodesStartingWithId(parent, "result");
        results.sort(Comparator.comparing(Node::getId));
        for (int i=0; i<table.size(); i++) {
            ((Label) players.get(i)).setText(table.get(i).getKey());
            ((Label) results.get(i)).setText(String.valueOf(table.get(i).getValue()));
        }
    }

    public void setIdle(Gui gui) { }
    public void removeIdle(Gui gui) { }
}
