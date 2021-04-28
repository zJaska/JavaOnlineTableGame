package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliChooseNumberPlayersScene;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliDiscardInitLeadersScene;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliNicknameScene;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliScene;

public class CliSceneFactory {

    private CliScene CliNicknameScene = new CliNicknameScene();
    private CliScene CliChooseNumberPlayersScene = new CliChooseNumberPlayersScene();
    private CliScene CliDiscardInitLeadersScene = new CliDiscardInitLeadersScene();

    public CliScene getScene(InstructionCode code) {
        switch (code) {
            case CHOOSE_NICKNAME:
                return CliNicknameScene;
            case CHOOSE_NUMBER_PLAYERS:
                return CliChooseNumberPlayersScene;
            case DISCARD_LEAD:
                return CliDiscardInitLeadersScene;
            default:
                return null;
        }
    }
}
