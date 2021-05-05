package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import javafx.util.Pair;

import java.util.ArrayList;

public class CliIdleScene implements CliScene {

    public static final String[] IDLE_COMMANDS = new String[] {
      "/help", "/showPlayer", "/showWarehouse", "/showStrongbox", "/showFaithTrack", "/display"
    };

    private static void help(ArrayList<String> input) {
        System.out.println("You can use the following commands: ");
        for (String x : IDLE_COMMANDS)
            System.out.println(x);
    }

    private static void display(ArrayList<String> input, CliScene scene) {
        scene.displayOptions();
    }

    private static void showPlayer(ArrayList<String> input) {

    }

    private static void showWarehouse(ArrayList<String> input) {

    }

    private static void showStrongbox(ArrayList<String> input) {

    }

    private static void showFaithTrack(ArrayList<String> input) {

    }

    public static void displayIdleCommand(ArrayList<String> input, CliScene scene) {
        switch (input.get(0)) {
            case "/help":
                help(input);
                break;
            case "/display":
                display(input,scene);
                break;
            case "/showPlayer":
                showPlayer(input);
                break;
            case "/showWarehouse":
                showWarehouse(input);
                break;
            case "/showStrongbox":
                showStrongbox(input);
                break;
            case "/showFaithTrack":
                showFaithTrack(input);
                break;
        }
    }

    public void displayOptions() { System.out.println("It's not your turn"); }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) { return null; }
}
