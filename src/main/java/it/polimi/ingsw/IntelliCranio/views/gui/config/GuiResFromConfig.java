package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.ability.Ability;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiDefaultScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.RES_FROM_CARD;
import static java.lang.Integer.parseInt;

public class GuiResFromConfig implements GuiConfig {

    Gui gui;

    public GuiResFromConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.strongbox.forEach(item -> {
            item.setOnMouseClicked(event -> {
                gui.setData(new Pair<>(
                        RES_FROM_STRONG,
                        new ArrayList<>(Arrays.asList(
                                new Resource(FinalResource.ResourceType.valueOf(item.getId().split("_")[2].toUpperCase()),1)))
                ));
            });
        });

        realScene.warehouse.forEach(item -> {
            item.setOnMouseClicked(event -> {
                gui.setData(new Pair<>(
                        RES_FROM_DEPOT,
                        new ArrayList<>(Arrays.asList(parseInt(item.getId().split("_")[1])))
                ));
            });
        });

        realScene.getLeadersButtons().forEach(btn -> {
            LeadCard lead = MainClient.getGame().getCurrentPlayer().getLeaders().get(parseInt(btn.getId().split("_")[2]));
            if (lead.getAbilityType() != Ability.AbilityType.DEPOT)
                return;

            btn.setOnMouseClicked(event -> {
                gui.setData(new Pair<>(
                        RES_FROM_CARD,
                        new ArrayList<>(Arrays.asList(lead))
                ));
            });
        });
    }

    public void removeConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.strongbox.forEach(item -> {
            item.setOnMouseClicked(null);
        });

        realScene.warehouse.forEach(item -> {
            item.setOnMouseClicked(null);
        });

        realScene.getLeadersButtons().forEach(btn -> {
            btn.setOnMouseClicked(null);
        });
    }
}
