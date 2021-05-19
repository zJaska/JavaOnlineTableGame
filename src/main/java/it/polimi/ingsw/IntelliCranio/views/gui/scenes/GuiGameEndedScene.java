package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.ArrayList;

public class GuiGameEndedScene extends GuiScene {

    public GuiGameEndedScene(Parent parent) {
        super(parent);

        ArrayList<Pair<String, Integer>> table = MainClient.game.calculatePoints();
        Label wonlabel = (Label) GuiUtil.getNodeById(parent, "won");

        boolean won = false;

        if (MainClient.game.isSinglePlayer()) {
            if (MainClient.game.getPlayer(MainClient.nickname).getFaithPosition() >= MainClient.game.getFaithTrack().getTrackLength())
                won = true;
        } else if (table.get(0).getKey().equals(MainClient.nickname))
                won = true;

        if (won)
            wonlabel.setText("VICTORY");
        else
            wonlabel.setText("DEFEAT");

        ArrayList<Node> players = GuiUtil.getNodesStartingWithId(parent, "player");
        ArrayList<Node> results = GuiUtil.getNodesStartingWithId(parent, "result");
        for (int i=0; i<table.size(); i++) {
            ((Label) players.get(i)).setText(table.get(i).getKey());
            ((Label) results.get(i)).setText(String.valueOf(table.get(i).getValue()));
        }
    }

    public void setIdle(Gui gui) { }
    public void removeIdle(Gui gui) { }
}
