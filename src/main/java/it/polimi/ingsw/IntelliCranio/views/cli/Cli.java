package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliScene;

public class Cli implements View {

    private CliScene scene;
    CliSceneFactory sceneFactory = new CliSceneFactory();

    @Override
    public String getInput() {
        return null;
    }

    @Override
    public void setScene(InstructionCode code) {
        scene = sceneFactory.getScene(code);
    }
}
