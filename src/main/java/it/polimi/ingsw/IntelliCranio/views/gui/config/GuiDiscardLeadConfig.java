package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiInitScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.SceneWithLeaders;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_RES;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DISCARD_LEAD;
import static java.lang.Integer.parseInt;
import static javafx.application.Platform.runLater;

public class GuiDiscardLeadConfig implements GuiConfig {

    Gui gui;

    public GuiDiscardLeadConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        SceneWithLeaders realScene = (SceneWithLeaders) scene;

        try {
            GuiInitScene initScene = (GuiInitScene) scene;
            initScene.message.setText("Discard 2 leader cards:");
        } catch (Exception e) {}

        GuiUtil.setLeadersActions(realScene, DISCARD_LEAD, gui);
    }

    public void removeConfig(GuiScene scene) {
        SceneWithLeaders realScene = (SceneWithLeaders) scene;

        GuiUtil.removeLeadersActions(realScene, DISCARD_LEAD);
    }
}
