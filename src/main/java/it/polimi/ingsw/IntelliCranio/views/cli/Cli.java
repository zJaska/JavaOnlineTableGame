package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.Response;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliIdleScene;
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
        ArrayList<String> input;
        Pair<InstructionCode,ArrayList<Object>> data;

        do {
            do {
                input = new ArrayList<String>(Arrays.asList(scanner.nextLine().split(" ")));
            } while (input.size() == 0 || input.get(0).equals(""));

            String firstWord = input.get(0);
            if (Arrays.stream(CliIdleScene.IDLE_COMMANDS).anyMatch(x -> x.equals(firstWord))) {
                try { CliIdleScene.displayIdleCommand(input, scene); }
                catch (InvalidArgumentsException e) {
                    System.out.println(e.getErrorMessage());
                }
                return null;
            }

            data = null;
            try { data = scene.createData(input); }
            catch (InvalidArgumentsException e) {
                System.out.println(e.getErrorMessage());
            }

        } while (data == null);

        return data;
    }

    public void setScene(InstructionCode code) {
        if (sceneFactory.getScene(code) == scene)
            return;
        scene = sceneFactory.getScene(code);

        if (code != InstructionCode.IDLE)
            scene.displayOptions();
    }

    public void showCommunication(String msg) {
        System.out.println("SERVER: " + msg);
    }

    public void gameChanged() { }
}
