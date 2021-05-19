package it.polimi.ingsw.IntelliCranio.views.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.Semaphore;

public class GuiLauncher extends Application implements Runnable {

    private static Gui gui = null;

    private static Semaphore sem = new Semaphore(0);

    public void start(Stage stage) {
        gui.setStage(stage);
        gui.setup();

        stage.setTitle("Masters of puppets!");
        stage.show();

        // Close the program when closing the window
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        sem.release();
    }

    public void run() {
        if (gui == null) {
            gui = new Gui();
            launch(new String[0]);
        }
    }

    public static Gui getGui() { return gui; }

    public static void waitForReady() {
        try { sem.acquire(); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
