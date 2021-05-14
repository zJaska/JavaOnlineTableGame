package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiResourceMarketScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.scene.Scene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;
import static javafx.application.Platform.runLater;

public class GuiResourceMarketConfig implements GuiConfig {

    Gui gui;

    public GuiResourceMarketConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiResourceMarketScene realScene = (GuiResourceMarketScene) scene;

        realScene.buttons.forEach(btn -> {
            btn.setOnMouseClicked(event -> {
                if (btn.getId().contains("row")) {
                    gui.setData(new Pair<>(
                            SELECT_ROW,
                            new ArrayList<>(Arrays.asList(parseInt(btn.getId().split("row_")[1])))
                    ));
                } else {
                    gui.setData(new Pair<>(
                            SELECT_COLUMN,
                            new ArrayList<>(Arrays.asList(parseInt(btn.getId().split("col_")[1])))
                    ));
                }
            });
        });

        realScene.confirm.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(CONFIRM, null));
        });
        realScene.gotoDefault.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(CANCEL, null));
        });

        new GuiChooseResourceConfig(gui).setConfig(realScene);
    }

    public void removeConfig(GuiScene scene) {
        GuiResourceMarketScene realScene = (GuiResourceMarketScene) scene;

        realScene.buttons.forEach(btn -> btn.setOnMouseClicked(null));
        realScene.confirm.setOnMouseClicked(null);
        realScene.gotoDefault.setOnMouseClicked(null);
        new GuiChooseResourceConfig(gui).removeConfig(scene);
    }
}
