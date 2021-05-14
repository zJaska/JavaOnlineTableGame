package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DEFAULT;
import static it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent.GAME_CHANGED_EVENT_TYPE;
import static java.lang.Integer.parseInt;

public class GuiResourceMarketScene extends GuiScene implements SceneWithResources {

    public ArrayList<Node> buttons;
    public ArrayList<Node> resources;
    public ArrayList<Node> marbles;
    public Button confirm;
    public Node gotoDefault;

    public GuiResourceMarketScene(Parent parent) {
        super(parent);

        buttons = GuiUtil.getNodesStartingWithId(parent,"select");
        confirm = (Button) GuiUtil.getNodeById(parent,"confirm");
        gotoDefault = GuiUtil.getNodeById(parent,"goto_default");

        buttons.forEach(btn -> btn.setOpacity(0));

        marbles = GuiUtil.getNodesStartingWithId(parent, "marble");
        marbles.forEach(marble -> {
            marble.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                FinalResource[][] grid = MainClient.game.getResourceMarket().getGridCopy();
                FinalResource extra = MainClient.game.getResourceMarket().getExtraMarbleCopy();

                if (marble.getId().equals("marble_extra")) {
                    InputStream tmp = getClass().getResourceAsStream("/assets/Materials/marble_" + extra.getType().toString().toLowerCase() + ".png");
                    ((ImageView) marble).setImage(new Image(tmp));
                    return;
                }

                int r = parseInt(marble.getId().split("_")[1]);
                int c = parseInt(marble.getId().split("_")[2]);

                InputStream tmp = getClass().getResourceAsStream("/assets/Materials/marble_" + grid[r][c].getType().toString().toLowerCase() + ".png");
                ((ImageView) marble).setImage(new Image(tmp));
            });
        });

        resources = GuiUtil.getNodesStartingWithId(parent, "resource");
    }

    public ArrayList<Node> getResources() { return resources; }

    public void setIdle(Gui gui) {
        gotoDefault.setOnMouseClicked(event -> gui.setIdleScene(DEFAULT));
    }
    public void removeIdle(Gui gui) {
        gotoDefault.setOnMouseClicked(null);
    }
}
