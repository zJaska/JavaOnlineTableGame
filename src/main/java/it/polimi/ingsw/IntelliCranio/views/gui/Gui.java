package it.polimi.ingsw.IntelliCranio.views.gui;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.gui.config.GuiConfig;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent.GAME_CHANGED_EVENT_TYPE;
import static javafx.application.Platform.runLater;

public class Gui implements View {

    private Stage stage;
    private GuiConfig config;
    private GuiScene scene;
    private GuiSceneFactory sceneFactory;

    private Semaphore waitForInput = new Semaphore(0);
    private Pair<InstructionCode, ArrayList<Object>> data;

    public void setup() {
        sceneFactory = new GuiSceneFactory(this);
    }

    public Pair<InstructionCode,ArrayList<Object>> getInput() {
        try { waitForInput.acquire(); } catch (Exception e) { e.printStackTrace(); }
        if (data.getKey() == null)
            data = new Pair<>(data.getKey(), new ArrayList<>());
        return data;
    }

    public void setScene(InstructionCode code) {
        runLater(() -> {
            try { config.removeConfig(scene); }
            catch (Exception ignored) { }

            Pair<GuiScene, GuiConfig> pair = sceneFactory.getScene(code);
            if (pair.getKey() != null)
                scene = pair.getKey();
            config = pair.getValue();

            GuiUtil.fireDownEvent(scene.getRoot(), new GameChangedEvent(GAME_CHANGED_EVENT_TYPE));
            stage.setScene(scene);
            config.setConfig(scene);
        });
    }

    public void setIdleScene(InstructionCode code) {
        runLater(() -> {
            try { config.removeConfig(scene); }
            catch (Exception ignored) { }

            GuiScene tmpscene = sceneFactory.getScene(code).getKey();
            if (tmpscene != null)  scene = tmpscene;

            stage.setScene(scene);
            config.setConfig(scene);
        });
    }

    public void showCommunication(String msg) {
        runLater(() -> scene.showCommunication(msg));
    }

    public void setStage(Stage stage) { this.stage = stage; }

    public void setData(Pair<InstructionCode, ArrayList<Object>> dataIn) {
        data = dataIn;
        waitForInput.release();
    }

    public void gameChanged() {
        runLater(() -> sceneFactory.updateScenes());
    }
}
