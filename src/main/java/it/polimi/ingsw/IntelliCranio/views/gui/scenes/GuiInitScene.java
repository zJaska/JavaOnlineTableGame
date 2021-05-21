package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;

import it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;
import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent.GAME_CHANGED_EVENT_TYPE;
import static java.lang.Integer.parseInt;

public class GuiInitScene extends GuiScene implements SceneWithResources, SceneWithLeaders, SceneWithNickname {

    public Label message;
    public AnchorPane anchor_resources;
    public AnchorPane anchor_leaders;

    public GuiInitScene(Parent parent) {
        super(parent);

        message = (Label) GuiUtil.getNodeById(parent,"message");
        anchor_resources = (AnchorPane) GuiUtil.getNodeById(parent,"anchor_resources");
        anchor_leaders = (AnchorPane) GuiUtil.getNodeById(parent,"anchor_leaders");

        message.setText("");

        GuiUtil.setLeadersUpdater(anchor_leaders, this);
    }

    public void setIdle(Gui gui) { }

    public void removeIdle(Gui gui) { }

    public ArrayList<Node> getResources() {
        return GuiUtil.getNodesStartingWithId(anchor_resources, "resource");
    }

    public ArrayList<Node> getLeadersButtons() {
        return GuiUtil.getNodesStartingWithId(anchor_leaders, "leader");
    }

    public String getNickname() { return MainClient.getNickname(); }
}
