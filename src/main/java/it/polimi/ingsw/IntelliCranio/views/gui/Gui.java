package it.polimi.ingsw.IntelliCranio.views.gui;

import it.polimi.ingsw.IntelliCranio.network.Packet.Response;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.util.Pair;

import java.util.ArrayList;

public class Gui implements View {

    private GuiScene scene;
    private GuiSceneFactory sceneFactory = new GuiSceneFactory();

    public Pair<InstructionCode,ArrayList<Object>> getInput() {
        return null;
    }

    public void setScene(InstructionCode code) {
        scene = sceneFactory.getScene(code);
    }

    public void displayError(Response response) {}

    public void showCommunication(String msg) {}
}
