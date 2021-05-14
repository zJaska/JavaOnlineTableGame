package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import static it.polimi.ingsw.IntelliCranio.util.Net.ACK_MSG;
import static javafx.application.Platform.runLater;

public abstract class GuiScene extends Scene {

    Label communication = null;

    public GuiScene(Parent parent) {
        super(parent);
    }

    public void showCommunication(String msg) {

        if (communication == null) {
            communication = new Label();
            setOnMouseReleased(event -> communication.setVisible(false));
            communication.setStyle("-fx-background-color: white;\n" +
                    "    -fx-padding: 5;\n" +
                    "    -fx-border-color: black; \n" +
                    "    -fx-border-width: 2;\n" +
                    "    -fx-font-size: 16;");

            AnchorPane parent = (AnchorPane) getRoot().getChildrenUnmodifiable().get(0);

            communication.setLayoutX(50);
            communication.setLayoutY(50);
            parent.getChildren().add(communication);
        }

        communication.setText(msg);
        communication.setVisible(true);
    }

    public abstract void setIdle(Gui gui);
    public abstract void removeIdle(Gui gui);
}
