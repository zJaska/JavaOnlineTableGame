package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;

public class GuiIdleConfig implements GuiConfig {

    Gui gui;

    public GuiIdleConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        scene.setIdle(gui);
    }

    public void removeConfig(GuiScene scene) {
        scene.removeIdle(gui);
    }
}
