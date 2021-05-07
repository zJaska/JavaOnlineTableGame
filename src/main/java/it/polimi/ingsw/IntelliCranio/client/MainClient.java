package it.polimi.ingsw.IntelliCranio.client;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.views.DummyView;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.Cli;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliIdleScene;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static it.polimi.ingsw.IntelliCranio.util.Net.createPacketFromInput;

public class MainClient {

    public static Game game;
    public static String nickname;

    private static SocketHandler socketHandler;
    private static View view;

    public static void main(String[] args) {
        /*
        game = new Game(new ArrayList<>(Arrays.asList("icci","pippi")));
        nickname = "icci";
        CliIdleScene.showDevCards();*/

        view = askView();
        //view = getDummyView(parseInt(args[0]));

        try { socketHandler = new SocketHandler("localhost",1051); }
        catch (IOException e) { return ; }

        new Thread(new RunningView(view)).start();
        new Thread(new RunningNetwork(socketHandler, view)).start();

        Packet pack;

        while (true) {
            pack = RunningNetwork.getData();

            view.setScene(pack.getInstructionCode());

            socketHandler.send(createPacketFromInput(RunningView.getData()));
        }
    }


    private static View askView() {
        String tmp;
        do {
            System.out.println("Select the view you want for the game:\n1) CLI\n2) GUI");
            Scanner in = new Scanner(System.in);
            tmp = in.nextLine();
        } while (!tmp.equals("1") && !tmp.equals("2"));

        switch (tmp) {
            case "1":
                return new Cli();
            case "2":
                return new Gui();
        }

        return null;
    }

    private static View getDummyView(int count) {
        String[] inputs = null;

        switch (count) {
            case 0:
                inputs = new String [] {
                        "ste",
                        "2"
                };
                break;
            case 1:
                inputs = new String[] {
                        "ste",
                        "anna",
                        "3"
                };
                break;

        }

        return new DummyView(inputs);
    }
}
