package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.Response;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliScene;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Scanner;

import static it.polimi.ingsw.IntelliCranio.Utility.toList;

public class Cli implements View {

    private Scanner scanner = new Scanner(System.in);
    private CliScene scene;

    CliSceneFactory sceneFactory = new CliSceneFactory();

    public Pair<InstructionCode,ArrayList<String>> getInput() {
        ArrayList<String> input = null;
        Response err = null;

        do {
            scene.displayError(err);
            input = toList(scanner.nextLine().split(" "));

            if (input.get(0).equals("/help"))
                scene.displayOptions();

        } while ((err = scene.isSyntaxCorrect(input)) != Response.ACK);

        return new Pair<InstructionCode,ArrayList<String>>(scene.getInstructionCode(input),input);
    }

    public void setScene(InstructionCode code) {
        scene = sceneFactory.getScene(code);
        scene.displayOptions();
    }

    public void displayError(Response response) { scene.displayError(response); }

    public void showCommunication(String msg) {
        System.out.println(msg);
    }
}
