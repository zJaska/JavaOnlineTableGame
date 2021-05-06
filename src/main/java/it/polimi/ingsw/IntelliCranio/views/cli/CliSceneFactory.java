package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.*;

public class CliSceneFactory {

    private CliScene CliNicknameScene = new CliNicknameScene();
    private CliScene CliChooseNumberPlayersScene = new CliChooseNumberPlayersScene();
    private CliScene CliDiscardLeaderScene = new CliDiscardLeaderScene();
    private CliScene CliIdleScene = new CliIdleScene();
    private CliScene CliChooseInitResScene = new CliChooseInitResScene();
    private CliScene CliDefaultScene = new CliDefaultScene();
    private CliScene CliManageWarehouseScene = new CliManageWarehouseScene();
    private CliScene CliResourceMarketScene = new CliResourceMarketScene();
    private CliScene CliCardMarketScene = new CliCardMarketScene();

    public CliScene getScene(InstructionCode code) {
        switch (code) {
            case CHOOSE_NICKNAME:
                return CliNicknameScene;
            case CHOOSE_NUMBER_PLAYERS:
                return CliChooseNumberPlayersScene;
            case DISCARD_INIT_LEAD:
                return CliDiscardLeaderScene;
            case CHOOSE_INIT_RES:
                return CliChooseInitResScene;
            case MNG_WARE:
                return CliManageWarehouseScene;
            case DEFAULT:
                return CliDefaultScene;
            case DISCARD_LEAD:
                return CliDiscardLeaderScene;
            case RES_MARKET:
                return CliResourceMarketScene;
            case CARD_MARKET:
                return CliCardMarketScene;
            case IDLE:
                return CliIdleScene;
            default:
                return null;
        }
    }
}
