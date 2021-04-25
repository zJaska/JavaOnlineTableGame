package it.polimi.ingsw.IntelliCranio.views.gui;

import it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;

public class Gui implements View {

    private GuiScene scene;
    private GuiSceneFactory sceneFactory = new GuiSceneFactory();

    public String getInput() {
        return null;
    }

    public Packet setScene(InstructionCode code, boolean display, ErrorCode option) {
        scene = sceneFactory.getScene(code);
        return null;
    }
}
