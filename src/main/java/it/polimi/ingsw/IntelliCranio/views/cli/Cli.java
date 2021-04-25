package it.polimi.ingsw.IntelliCranio.views.cli;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliScene;

import java.util.Scanner;

import static it.polimi.ingsw.IntelliCranio.Utility.toList;

public class Cli implements View {

    private Scanner scanner = new Scanner(System.in);
    private CliScene scene;

    CliSceneFactory sceneFactory = new CliSceneFactory();

    private String getInput() {
        return scanner.nextLine();
    }

    public Packet setScene(InstructionCode code, boolean display, ErrorCode option) {
        String input = "";
        ErrorCode err = option;

        scene = sceneFactory.getScene(code);

        do {
            if (display)    scene.displayOptions(err);
            input = getInput();
            display = true;
        } while ((err = scene.isSintaxCorrect(input)) != ErrorCode.ACK);

        return new Packet(code,null, toList(new String[] {input}));
    }
}
