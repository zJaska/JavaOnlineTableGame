package it.polimi.ingsw.IntelliCranio.views.gui;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.config.*;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.*;
import javafx.scene.Scene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent.GAME_CHANGED_EVENT_TYPE;

public class GuiSceneFactory {

    private GuiScene GuiCardMarketScene;
    private GuiScene GuiResourceMarketScene;
    private GuiScene GuiDefaultScene;
    private GuiScene GuiEmptyScene;
    private GuiScene GuiInitScene;

    private ArrayList<GuiScene> scenes = new ArrayList<>();

    private Gui gui;

    public GuiSceneFactory(Gui gui) {
        this.gui = gui;
        GuiEmptyScene = new GuiEmptyScene(GuiUtil.getParentFromFXML("/scene_builder/empty_scene.fxml"));
        GuiInitScene = new GuiInitScene(GuiUtil.getParentFromFXML("/scene_builder/init_scene.fxml"));
        GuiDefaultScene = new GuiDefaultScene(GuiUtil.getParentFromFXML("/scene_builder/default_scene.fxml"));
        GuiResourceMarketScene = new GuiResourceMarketScene(GuiUtil.getParentFromFXML("/scene_builder/resource_market_scene.fxml"));
        GuiCardMarketScene = new GuiCardMarketScene(GuiUtil.getParentFromFXML("/scene_builder/card_market_scene.fxml"));
        scenes.addAll(Arrays.asList(GuiEmptyScene, GuiInitScene, GuiDefaultScene, GuiResourceMarketScene, GuiCardMarketScene));
    }

    public Pair<GuiScene, GuiConfig> getScene(InstructionCode code) {
        switch (code) {
            case CHOOSE_NICKNAME:
                return new Pair<>(GuiEmptyScene, new GuiNicknameConfig(gui));
            case WANNA_PLAY_ALONE:
                return new Pair<>(GuiEmptyScene, new GuiWannaPlayAloneConfig(gui));
            case CHOOSE_NUMBER_PLAYERS:
                return new Pair<>(GuiEmptyScene, new GuiChooseNumberPlayersConfig(gui));
            case DISCARD_INIT_LEAD:
                return new Pair<>(GuiInitScene, new GuiDiscardLeadConfig(gui));
            case CHOOSE_INIT_RES:
                return new Pair<>(GuiInitScene, new GuiChooseResourceConfig(gui));
            case DEFAULT:
                return new Pair<>(GuiDefaultScene, new GuiDefaultConfig(gui));
            case MNG_WARE:
                return new Pair<>(GuiDefaultScene, new GuiManageWarehouseConfig(gui));
            case RES_MARKET:
                return new Pair<>(GuiResourceMarketScene, new GuiResourceMarketConfig(gui));
            case CARD_MARKET:
                return new Pair<>(GuiCardMarketScene, new GuiCardMarketConfig(gui));
            case CARD_MARKET_AS:
                return new Pair<>(GuiDefaultScene, new GuiCardMarketAfterSelectionConfig(gui));
            case ACT_PROD:
                return new Pair<>(GuiDefaultScene, new GuiActivateProductionConfig(gui));
            case IDLE:
                return new Pair<>(null, new GuiIdleConfig(gui));
            default:
                return null;
        }
    }

    public void updateScenes() {
        scenes.forEach(scene -> {
            GuiUtil.fireDownEvent(scene.getRoot(), new GameChangedEvent(GAME_CHANGED_EVENT_TYPE));
        });
    }
}
