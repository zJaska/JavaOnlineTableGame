package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.*;

public class CliSceneFactory {

    private CliScene CliNicknameScene = new CliNicknameScene();
    private CliScene CliChooseNumberPlayersScene = new CliChooseNumberPlayersScene();
    private CliScene CliDiscardInitLeadersScene = new CliDiscardInitLeadersScene();
    private CliScene CliIdleScene = new CliIdleScene();

    public CliScene getScene(InstructionCode code) {
        switch (code) {
            case CHOOSE_NICKNAME:
                return CliNicknameScene;
            case CHOOSE_NUMBER_PLAYERS:
                return CliChooseNumberPlayersScene;
            case DISCARD_LEAD:
                return CliDiscardInitLeadersScene;
            case IDLE:
                return CliIdleScene;
            default:
                return null;
        }
    }
}
