package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiDefaultScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.util.Pair;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class GuiDefaultConfig implements GuiConfig {

    Gui gui;

    public GuiDefaultConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.manage_warehouse.setVisible(true);
        realScene.activate_production.setVisible(true);
        realScene.confirm.setVisible(true);
        realScene.confirm.setText("END TURN");

        realScene.manage_warehouse.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(MNG_WARE, null));
        });
        realScene.activate_production.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(ACT_PROD, null));
        });
        realScene.confirm.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(END_TURN, null));
        });
        realScene.goto_market_card.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(CARD_MARKET, null));
        });
        realScene.goto_market_resource.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(RES_MARKET, null));
        });

        GuiUtil.setLeadersActions(realScene, PLAY_LEADER, gui);
        GuiUtil.setLeadersActions(realScene, DISCARD_LEAD, gui);
    }

    public void removeConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.manage_warehouse.setVisible(false);
        realScene.activate_production.setVisible(false);
        realScene.confirm.setVisible(false);

        realScene.manage_warehouse.setOnMouseClicked(null);
        realScene.activate_production.setOnMouseClicked(null);
        realScene.confirm.setOnMouseClicked(null);
        realScene.goto_market_card.setOnMouseClicked(null);
        realScene.goto_market_resource.setOnMouseClicked(null);

        GuiUtil.removeLeadersActions(realScene, PLAY_LEADER);
        GuiUtil.removeLeadersActions(realScene, DISCARD_LEAD);
    }
}
