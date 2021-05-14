package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiDefaultScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.SELECT_SLOT;
import static java.lang.Integer.parseInt;

public class GuiCardMarketAfterSelectionConfig implements GuiConfig {

    Gui gui;

    public GuiCardMarketAfterSelectionConfig(Gui gui) { this.gui = gui; }

    public void setConfig(GuiScene scene) {
        GuiDefaultScene realScene;
        try { realScene = (GuiDefaultScene) scene; }
        catch (Exception e) {
            scene.setIdle(gui);
            return;
        }

        realScene.dev_slots.forEach(slot -> {
            slot.setOnMouseClicked(event -> {
                gui.setData(new Pair<>(
                        SELECT_SLOT,
                        new ArrayList<>(Arrays.asList(parseInt(slot.getId().split("_")[2]) - 1))
                ));
            });
        });

        new GuiResFromConfig(gui).setConfig(scene);
    }

    public void removeConfig(GuiScene scene) {
        GuiDefaultScene realScene;
        try { realScene = (GuiDefaultScene) scene; }
        catch (Exception e) {
            scene.removeIdle(gui);
            return;
        }

        realScene.dev_slots.forEach(slot -> slot.setOnMouseClicked(null));

        new GuiResFromConfig(gui).removeConfig(scene);
    }
}
