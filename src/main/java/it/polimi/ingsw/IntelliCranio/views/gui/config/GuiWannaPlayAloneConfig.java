package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiEmptyScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static javafx.application.Platform.runLater;

public class GuiWannaPlayAloneConfig implements GuiConfig {

    Gui gui;

    public GuiWannaPlayAloneConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiEmptyScene realScene = (GuiEmptyScene) scene;

        realScene.message.setText("Do you want to play alone?");
        realScene.yes_btn.setVisible(true);
        realScene.no_btn.setVisible(true);

        realScene.confirm_button.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(
                    (realScene.yes_btn.isSelected()) ? ALONE : MULTIPLAYER,
                    null
            ));
        });
    }

    public void removeConfig(GuiScene scene) {
        GuiEmptyScene realScene = (GuiEmptyScene) scene;

        realScene.yes_btn.setVisible(false);
        realScene.no_btn.setVisible(false);
        realScene.message.setText("");
        realScene.confirm_button.setOnMouseClicked(e -> {});
    }
}
