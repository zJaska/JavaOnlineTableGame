package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiEmptyScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiInitScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiChooseNumberPlayersConfig implements GuiConfig {

    Gui gui;

    public GuiChooseNumberPlayersConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiEmptyScene realScene = (GuiEmptyScene) scene;

        realScene.message.setText("Choose the number of players:");
        realScene.box.setVisible(true);
        realScene.box.setItems(FXCollections.observableArrayList(2,3,4));

        realScene.confirm_button.setOnMouseClicked(click -> {
            if (realScene.box.getValue() != null)
                gui.setData(new Pair<>(
                        Packet.InstructionCode.CHOOSE_NUMBER_PLAYERS,
                        new ArrayList<>(Arrays.asList((Integer)realScene.box.getValue()))
                ));
        });
    }

    public void removeConfig(GuiScene scene) {
        GuiEmptyScene realScene = (GuiEmptyScene) scene;

        realScene.message.setText("");
        realScene.box.setVisible(false);
        realScene.confirm_button.setOnMouseClicked(null);
    }
}
