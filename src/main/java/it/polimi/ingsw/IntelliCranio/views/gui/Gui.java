package it.polimi.ingsw.IntelliCranio.views.gui;

import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;

public class Gui implements View {

    private GuiScene scene;
    private GuiSceneFactory sceneFactory = new GuiSceneFactory();

    @Override
    public String getInput() {
        return null;
    }

    @Override
    public void setScene(InstructionCode code) {
        scene = sceneFactory.getScene(code);
    }
}
