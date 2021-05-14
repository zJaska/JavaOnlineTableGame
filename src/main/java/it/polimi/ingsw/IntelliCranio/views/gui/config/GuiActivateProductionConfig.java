package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiDefaultScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.scene.Node;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;

public class GuiActivateProductionConfig implements GuiConfig {

    Gui gui;

    public GuiActivateProductionConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.confirm.setText("CONFIRM");
        realScene.confirm.setVisible(true);
        realScene.cancel.setVisible(true);
        realScene.anchor_resources.setVisible(true);

        realScene.dev_slots.forEach(slot -> {
            slot.setOnMouseClicked(event -> {
                gui.setData(new Pair<>(
                        SELECT_SLOT,
                        new ArrayList<>(Arrays.asList(parseInt(slot.getId().split("_")[2])))
                ));
            });
        });

        realScene.getLeadersButtons().forEach(btn -> {
            btn.setOnMouseClicked(event -> {
                LeadCard lead = MainClient.game.getCurrentPlayer().getLeaders().get(parseInt(btn.getId().split("_")[1]));
                gui.setData(new Pair<>(
                        SELECT_CARD,
                        new ArrayList<>(Arrays.asList(lead))
                ));
            });
        });

        new GuiResFromConfig(gui).setConfig(scene);

        new GuiChooseResourceConfig(gui).setConfig(realScene);
    }

    public void removeConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.confirm.setVisible(false);
        realScene.cancel.setVisible(false);
        realScene.anchor_resources.setVisible(false);

        realScene.dev_slots.forEach(slot -> {
            slot.setOnMouseClicked(null);
        });

        realScene.getLeadersButtons().forEach(btn -> {
            btn.setOnMouseClicked(null);
        });

        new GuiResFromConfig(gui).removeConfig(scene);

        new GuiChooseResourceConfig(gui).removeConfig(realScene);
    }
}
