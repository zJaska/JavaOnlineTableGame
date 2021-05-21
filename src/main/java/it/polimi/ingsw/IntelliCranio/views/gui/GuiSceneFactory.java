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
    private GuiDefaultScene GuiDefaultScene;
    private GuiScene GuiEmptyScene;
    private GuiScene GuiInitScene;

    private ArrayList<GuiScene> scenes = new ArrayList<>();

    private Gui gui;

    public GuiSceneFactory(Gui gui) {
        this.gui = gui;
    }

    public Pair<GuiScene, GuiConfig> getScene(InstructionCode code) {
        switch (code) {
            case CHOOSE_NICKNAME:
                if (GuiEmptyScene == null) {
                    GuiEmptyScene = new GuiEmptyScene(GuiUtil.getParentFromFXML("/scene_builder/empty_scene.fxml"));
                    scenes.add(GuiEmptyScene);
                }

                return new Pair<>(GuiEmptyScene, new GuiNicknameConfig(gui));
            case WANNA_PLAY_ALONE:
                if (GuiEmptyScene == null) {
                    GuiEmptyScene = new GuiEmptyScene(GuiUtil.getParentFromFXML("/scene_builder/empty_scene.fxml"));
                    scenes.add(GuiEmptyScene);
                }

                return new Pair<>(GuiEmptyScene, new GuiWannaPlayAloneConfig(gui));
            case CHOOSE_NUMBER_PLAYERS:
                if (GuiEmptyScene == null) {
                    GuiEmptyScene = new GuiEmptyScene(GuiUtil.getParentFromFXML("/scene_builder/empty_scene.fxml"));
                    scenes.add(GuiEmptyScene);
                }

                return new Pair<>(GuiEmptyScene, new GuiChooseNumberPlayersConfig(gui));
            case DISCARD_INIT_LEAD:
                if (GuiInitScene == null) {
                    GuiInitScene = new GuiInitScene(GuiUtil.getParentFromFXML("/scene_builder/init_scene.fxml"));
                    scenes.add(GuiInitScene);
                }

                return new Pair<>(GuiInitScene, new GuiDiscardLeadConfig(gui));
            case CHOOSE_INIT_RES:
                if (GuiInitScene == null) {
                    GuiInitScene = new GuiInitScene(GuiUtil.getParentFromFXML("/scene_builder/init_scene.fxml"));
                    scenes.add(GuiInitScene);
                }

                return new Pair<>(GuiInitScene, new GuiChooseResourceConfig(gui));
            case DEFAULT:
                if (GuiDefaultScene == null) {
                    GuiDefaultScene = new GuiDefaultScene(GuiUtil.getParentFromFXML("/scene_builder/default_scene.fxml"));
                    scenes.add(GuiDefaultScene);
                }

                GuiDefaultScene.setMainPlayer();
                return new Pair<>(GuiDefaultScene, new GuiDefaultConfig(gui));
            case MNG_WARE:
                if (GuiDefaultScene == null) {
                    GuiDefaultScene = new GuiDefaultScene(GuiUtil.getParentFromFXML("/scene_builder/default_scene.fxml"));
                    scenes.add(GuiDefaultScene);
                }

                return new Pair<>(GuiDefaultScene, new GuiManageWarehouseConfig(gui));
            case RES_MARKET:
                if (GuiResourceMarketScene == null) {
                    GuiResourceMarketScene = new GuiResourceMarketScene(GuiUtil.getParentFromFXML("/scene_builder/resource_market_scene.fxml"));
                    scenes.add(GuiResourceMarketScene);
                    GuiUtil.fireDownEvent(GuiResourceMarketScene.getRoot(), new GameChangedEvent(GAME_CHANGED_EVENT_TYPE));
                }

                return new Pair<>(GuiResourceMarketScene, new GuiResourceMarketConfig(gui));
            case CARD_MARKET:
                if (GuiCardMarketScene == null) {
                    GuiCardMarketScene = new GuiCardMarketScene(GuiUtil.getParentFromFXML("/scene_builder/card_market_scene.fxml"));
                    scenes.add(GuiCardMarketScene);
                    GuiUtil.fireDownEvent(GuiCardMarketScene.getRoot(), new GameChangedEvent(GAME_CHANGED_EVENT_TYPE));
                }

                return new Pair<>(GuiCardMarketScene, new GuiCardMarketConfig(gui));
            case CARD_MARKET_AS:
                if (GuiDefaultScene == null) {
                    GuiDefaultScene = new GuiDefaultScene(GuiUtil.getParentFromFXML("/scene_builder/default_scene.fxml"));
                    scenes.add(GuiDefaultScene);
                }

                return new Pair<>(GuiDefaultScene, new GuiCardMarketAfterSelectionConfig(gui));
            case ACT_PROD:
                if (GuiDefaultScene == null) {
                    GuiDefaultScene = new GuiDefaultScene(GuiUtil.getParentFromFXML("/scene_builder/default_scene.fxml"));
                    scenes.add(GuiDefaultScene);
                }

                return new Pair<>(GuiDefaultScene, new GuiActivateProductionConfig(gui));
            case IDLE:
                if (GuiDefaultScene == null) {
                    GuiDefaultScene = new GuiDefaultScene(GuiUtil.getParentFromFXML("/scene_builder/default_scene.fxml"));
                    scenes.add(GuiDefaultScene);
                    GuiDefaultScene.setMainPlayer();
                }

                return new Pair<>(GuiDefaultScene, new GuiIdleConfig(gui));
            case GAME_ENDED:
                return new Pair<>(new GuiGameEndedScene(GuiUtil.getParentFromFXML("/scene_builder/game_ended_scene.fxml")), null);
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
