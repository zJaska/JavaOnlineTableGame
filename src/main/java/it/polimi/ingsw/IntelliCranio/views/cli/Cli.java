package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.Response;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliIdle;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliScene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.ACK;

public class Cli implements View {

    private Scanner scanner = new Scanner(System.in);
    private CliScene scene;

    CliSceneFactory sceneFactory = new CliSceneFactory();

    public Pair<InstructionCode,ArrayList<Object>> getInput() {
        ArrayList<String> input = null;
        Response err = null;

        do {
            input = new ArrayList<String>(Arrays.asList(scanner.nextLine().split(" ")));

            String firstWord = input.get(0);
            if (Arrays.stream(CliIdle.IDLE_COMMANDS).anyMatch(x -> x.equals(firstWord))) {
                CliIdle.displayIdleCommand(input);
                return null;
            }

            err = scene.isSyntaxCorrect(input);
            scene.displayError(err);

        } while (err != ACK);

        return scene.createData(input);
    }

    public void setScene(InstructionCode code) {
        if (sceneFactory.getScene(code) == scene)
            return;
        scene = sceneFactory.getScene(code);
        scene.displayOptions();
    }

    public void displayError(Response response) {
        if (scene == null)
            return;
        if (response == ACK) {
            System.out.println("Operation done!");
            return;
        }
        scene.displayError(response);
    }

    public void showCommunication(String msg) {
        System.out.println(msg);
    }
}
