package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.scene.Scene;

public interface GuiConfig {
    void setConfig(GuiScene scene);
    void removeConfig(GuiScene scene);
}
