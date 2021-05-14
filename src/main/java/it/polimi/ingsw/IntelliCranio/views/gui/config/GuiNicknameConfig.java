package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiEmptyScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static javafx.application.Platform.runLater;

public class GuiNicknameConfig implements GuiConfig {

    Gui gui;

    public GuiNicknameConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiEmptyScene realScene = (GuiEmptyScene) scene;

        realScene.message.setText("Choose your nickname");
        realScene.textInput.setVisible(true);
        realScene.confirm_button.setOnMouseClicked(event -> {
            if (realScene.textInput.getText().trim() != "" && !(realScene.textInput.getText().split(" ").length > 1))
                gui.setData(new Pair<>(
                        CHOOSE_NICKNAME,
                        new ArrayList<>(Arrays.asList(realScene.textInput.getText().trim()))));
            realScene.textInput.setText("");
        });
    }

    public void removeConfig(GuiScene scene) {
        GuiEmptyScene realScene = (GuiEmptyScene) scene;

        realScene.textInput.setText("");
        realScene.textInput.setVisible(false);
        realScene.textInput.setOnMouseClicked(e -> {});
    }
}
