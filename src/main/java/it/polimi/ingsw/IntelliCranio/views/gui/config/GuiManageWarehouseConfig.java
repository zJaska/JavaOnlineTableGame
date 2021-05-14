package it.polimi.ingsw.IntelliCranio.views.gui.config;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiDefaultScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;

public class GuiManageWarehouseConfig implements GuiConfig {

    Gui gui;

    Node source = null;

    public GuiManageWarehouseConfig(Gui gui) {
        this.gui = gui;
    }

    public void setConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.cancel.setVisible(true);
        realScene.confirm.setVisible(true);
        realScene.confirm.setText("CONFIRM");
        realScene.anchor_extra.setVisible(true);

        realScene.confirm.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(CONFIRM, null));
        });
        realScene.cancel.setOnMouseClicked(event -> {
            gui.setData(new Pair<>(CANCEL, null));
        });

        ArrayList<Node> listeners = new ArrayList<>();
        listeners.addAll(realScene.warehouse);
        listeners.addAll(realScene.extra_count);
        listeners.addAll(realScene.extra_type);
        listeners.addAll(realScene.getLeadersButtons());

        listeners.forEach(image -> {
            image.setOnMouseClicked(event -> {
                System.out.println(image.getId());
                if (source == null) {
                    source = image;
                    GuiUtil.spawnPointer(image);
                    return;
                }
                resolve(source, image);
                GuiUtil.removePointers(realScene);
            });
        });
    }

    public void removeConfig(GuiScene scene) {
        GuiDefaultScene realScene = (GuiDefaultScene) scene;

        realScene.cancel.setVisible(false);
        realScene.confirm.setVisible(false);
        realScene.anchor_extra.setVisible(false);

        realScene.confirm.setOnMouseClicked(null);
        realScene.cancel.setOnMouseClicked(null);

        realScene.warehouse.forEach(image -> {
            image.setOnDragDropped(null);
        });

        realScene.extra_type.forEach(image -> {
            image.setOnDragDropped(null);
        });
    }

    public void resolve(Node source, Node target) {
        if (source.getId().startsWith("warehouse")) {

            if (target.getId().startsWith("extra")) {
                gui.setData(new Pair<>(
                        REMOVE_FROM_DEPOT,
                        new ArrayList<>(Arrays.asList(parseInt(source.getId().split("_")[1])))
                ));
            }
            if (target.getId().startsWith("warehouse")) {
                gui.setData(new Pair<>(
                        SWAP_LINES,
                        new ArrayList<>(Arrays.asList(
                                parseInt(source.getId().split("_")[1]),
                                parseInt(target.getId().split("_")[1])))
                ));
            }
            if (target.getId().startsWith("leader")) {
                gui.setData(new Pair<>(
                        DEPOT_TO_CARD,
                        new ArrayList<>(Arrays.asList(
                                parseInt(source.getId().split("_")[1])))
                ));
            }
        }

        if (source.getId().startsWith("extra")) {
            if (target.getId().startsWith("leader"))
                gui.setData(new Pair<>(
                        EXTRA_TO_CARD,
                        new ArrayList<>(Arrays.asList(
                                new Resource(ResourceType.valueOf(source.getId().split("_")[2].toUpperCase()), 1)))
                ));
            if (target.getId().startsWith("warehouse"))
                gui.setData(new Pair<>(
                        ADD_FROM_EXTRA,
                        new ArrayList<>(Arrays.asList(
                                new Resource(ResourceType.valueOf(source.getId().split("_")[2].toUpperCase()), 1),
                                parseInt(target.getId().split("_")[1])))
                ));
        }

        this.source = null;
    }
}
