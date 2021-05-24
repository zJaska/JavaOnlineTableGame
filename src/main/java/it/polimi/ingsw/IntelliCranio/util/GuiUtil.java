package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiDefaultScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.GuiScene;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.SceneWithLeaders;
import it.polimi.ingsw.IntelliCranio.views.gui.scenes.SceneWithNickname;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DISCARD_LEAD;
import static it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent.GAME_CHANGED_EVENT_TYPE;
import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class GuiUtil {

    public static Node getNodeById(Node root, String id) {
        try {
            if (id.equals(root.getId()))
                return root;

            for (Node child : ((Parent) root).getChildrenUnmodifiable()) {
                Node result = getNodeById(child, id);
                if (result != null)
                    return result;
            }
        } catch (Exception e) { }

        return null;
    }

    public static ArrayList<Node> getNodesStartingWithId(Node root, String id) {
        ArrayList<Node> result = new ArrayList();

        try {
            if (root.getId() != null && root.getId().startsWith(id))
                return new ArrayList<>(Arrays.asList(root));

            for (Node child : ((Parent) root).getChildrenUnmodifiable()) {
                ArrayList<Node> tmp = getNodesStartingWithId(child, id);
                if (tmp != null)
                    result.addAll(tmp);
            }
        } catch (Exception e) { }

        return result;
    }

    public static Parent getParentFromFXML(String path) {
        Parent root = null;
        try {
            URL url = Gui.class.getResource(path);
            root = FXMLLoader.load(url); }
        catch (IOException e) { e.printStackTrace(); }

        return root;
    }

    public static void fireDownEvent(Node root, Event event) {
        root.fireEvent(event);
        try {
            ((Parent) root).getChildrenUnmodifiable().forEach(child -> fireDownEvent(child, event));
        } catch (Exception e) { }
    }

    public static void setLeadersUpdater(Parent root, SceneWithNickname scene) {
        GuiUtil.getNodesStartingWithId(root, "img_leader").forEach(image -> {
            image.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                ArrayList<LeadCard> cards = MainClient.getGame().getPlayer(scene.getNickname()).getLeaders();
                int index = parseInt(image.getId().split("_")[2]);

                if (index >= cards.size())
                    ((ImageView) image).setImage(null);
                else {
                    InputStream tmp = GuiScene.class.getResourceAsStream("/assets/leaders/" + cards.get(index).getID() + ".JPG");
                    ((ImageView) image).setImage(new Image(tmp));
                }
            });
        });
    }

    public static void setLeadersActions(SceneWithLeaders scene, InstructionCode action, Gui gui) {
        scene.getLeadersButtons().forEach(btn -> {
            LeadCard leader;
            try {
                leader = MainClient.getGame().getCurrentPlayer().getLeaders().get(parseInt(btn.getId().split("_")[2]));
            } catch (IndexOutOfBoundsException e) {
                // There are no more player cards to be added
                return;
            }

            if (leader.isActive())
                return;

            Control ctrl = ((Control) btn);
            ContextMenu menu = (ctrl.getContextMenu() != null) ? ctrl.getContextMenu() : new ContextMenu();
            MenuItem item = new MenuItem(action.toString());
            menu.getItems().add(item);
            ctrl.setContextMenu(menu);

            int index = parseInt(btn.getId().split("_")[2]);

            item.setOnAction(event -> {
                gui.setData(new Pair<>(
                        action,
                        new ArrayList<>(Arrays.asList(MainClient.getGame().getCurrentPlayer().getLeaders().get(index)))
                ));
            });
        });
    }

    public static void removeLeadersActions(SceneWithLeaders scene, InstructionCode action) {
        scene.getLeadersButtons().forEach(leader -> {
            Control ctrl = ((Control) leader);
            ContextMenu menu = ctrl.getContextMenu();
            menu.getItems().removeIf(item -> item.getText().equals(action.toString()));
        });
    }

    public static void spawnPointer(Node target) {
        Circle circle = createPointer(target, "");

        ((Pane) target.getParent()).getChildren().add(circle);

        circle.toFront();
        target.toFront();
    }

    public static void togglePointer(Node target, String id) {
        Pane parent = (Pane) target.getParent();
        if (parent.getChildren().removeIf(child -> ("_" + id).equals(child.getId())))
            return;

        Circle circle = createPointer(target, id);
        parent.getChildren().add(circle);

        circle.toFront();
        target.toFront();
    }

    private static Circle createPointer(Node target, String id) {
        Circle circle = new Circle();
        circle.setId("_" + id);
        circle.setCenterX(target.getLayoutX());
        circle.setCenterY(target.getLayoutY());
        circle.setRadius(10);
        circle.setStyle("-fx-fill: red;");

        circle.toFront();
        target.toFront();

        return circle;
    }

    public static void removePointers(GuiScene scene) {
        ArrayList<Node> pointers = getNodesStartingWithId(scene.getRoot(), "_");
        pointers.forEach(point -> {
            ((Pane) point.getParent()).getChildren().remove(point);
        });
    }
}
