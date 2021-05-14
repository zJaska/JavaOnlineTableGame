package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiCardMarketScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;

public class GuiCardMarketConfig implements GuiConfig {

    Gui gui;

    public GuiCardMarketConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiCardMarketScene realScene = (GuiCardMarketScene) scene;

        realScene.goto_default.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(CANCEL, null));
        });

        realScene.dev_cards.forEach(card -> {
            card.setOnMouseClicked(event -> {
                gui.setData(new Pair<>(
                        SELECT_CARD,
                        new ArrayList<>(Arrays.asList(
                                parseInt(card.getId().split("_")[1]),
                                parseInt(card.getId().split("_")[2])
                        ))
                ));
            });
        });
    }

    public void removeConfig(GuiScene scene) {
        GuiCardMarketScene realScene = (GuiCardMarketScene) scene;

        realScene.goto_default.setOnMouseClicked(null);

        realScene.dev_cards.forEach(card -> {
            card.setOnMouseClicked(null);
        });
    }
}
