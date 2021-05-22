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
    private CliScene CliActivateProductionScene = new CliActivateProductionScene();
    private CliScene CliWannaPlayAloneScene = new CliWannaPlayAloneScene();
    private CliScene CliGameEndedScene = new CliGameEndedScene();

    public CliScene getScene(InstructionCode code) {
        switch (code) {
            case CHOOSE_NICKNAME:
                return CliNicknameScene;
            case WANNA_PLAY_ALONE:
                return CliWannaPlayAloneScene;
            case CHOOSE_NUMBER_PLAYERS:
                return CliChooseNumberPlayersScene;
            case DISCARD_INIT_LEAD:
            case DISCARD_LEAD:
                return CliDiscardLeaderScene;
            case CHOOSE_INIT_RES:
                return CliChooseInitResScene;
            case MNG_WARE:
                return CliManageWarehouseScene;
            case DEFAULT:
                return CliDefaultScene;
            case RES_MARKET:
                return CliResourceMarketScene;
            case CARD_MARKET:
                case CARD_MARKET_AS://TODO JASKA (?)
                return CliCardMarketScene;
            case ACT_PROD:
                return CliActivateProductionScene;
            case IDLE:
                return CliIdleScene;
            case GAME_ENDED:
                return CliGameEndedScene;
            default:
                return null;
        }
    }
}
