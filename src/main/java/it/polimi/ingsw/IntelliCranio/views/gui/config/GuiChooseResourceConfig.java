package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiInitScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.SceneWithResources;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_RES;
import static javafx.application.Platform.runLater;

public class GuiChooseResourceConfig implements GuiConfig {

    Gui gui;

    public GuiChooseResourceConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        SceneWithResources realScene = (SceneWithResources) scene;

        try {
            GuiInitScene initScene = (GuiInitScene) scene;
            initScene.message.setText("Choose 1 resource:");
        } catch (Exception e) {}

        realScene.getResources().forEach(resource -> {
            resource.setOnMouseClicked(event -> {
                gui.setData(new Pair<>(
                        CHOOSE_RES,
                        new ArrayList<>(Arrays.asList(new Resource(ResourceType.valueOf(resource.getId().split("_")[1].toUpperCase()), 1)))
                ));
            });
        });
    }

    public void removeConfig(GuiScene scene) {
        SceneWithResources realScene = (SceneWithResources) scene;

        realScene.getResources().forEach(resource -> {
            resource.setOnMouseClicked(null);
        });
    }
}
