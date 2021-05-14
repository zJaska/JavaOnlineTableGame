package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static javafx.application.Platform.runLater;

public class GuiEmptyScene extends GuiScene {

    public Label message;
    public TextField textInput;
    public RadioButton yes_btn;
    public RadioButton no_btn;
    public ComboBox box;
    public Button confirm_button;

    public GuiEmptyScene(Parent parent) {
        super(parent);

        message = (Label)GuiUtil.getNodeById(parent,"message");
        textInput = (TextField)GuiUtil.getNodeById(parent,"input");
        yes_btn = (RadioButton) GuiUtil.getNodeById(parent,"yes_btn");
        no_btn = (RadioButton)GuiUtil.getNodeById(parent,"no_btn");
        box = (ComboBox)GuiUtil.getNodeById(parent,"number_box");
        confirm_button = (Button)GuiUtil.getNodeById(parent,"confirm_button");

        no_btn.setSelected(true);
        
        yes_btn.setOnMouseClicked(btn -> {
            no_btn.setSelected(false);
        });
        no_btn.setOnMouseClicked(btn -> {
            yes_btn.setSelected(false);
        });

        Arrays.asList(textInput, yes_btn, no_btn, box).forEach(elem -> elem.setVisible(false));
    }

    public void setIdle(Gui gui) { }
    public void removeIdle(Gui gui) { }
}
