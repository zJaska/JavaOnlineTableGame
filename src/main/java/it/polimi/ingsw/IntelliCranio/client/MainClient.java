package it.polimi.ingsw.IntelliCranio.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.util.Save;
import it.polimi.ingsw.IntelliCranio.views.DummyView;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.Cli;
import it.polimi.ingsw.IntelliCranio.views.cli.scenes.CliIdleScene;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import it.polimi.ingsw.IntelliCranio.views.gui.GuiLauncher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static it.polimi.ingsw.IntelliCranio.util.Net.createPacketFromInput;
import static java.lang.Integer.parseInt;

public class MainClient {

    private static Game game;
    private static Object game_lock = new Object();
    public static Game getGame() {
        synchronized (game_lock) {
            return game;
        }
    }
    public static void setGame(Game newGame) {
        synchronized (game_lock) {
            game = newGame;
        }
    }

    private static String nickname;
    private static Object nick_lock = new Object();
    public static String getNickname() {
        synchronized (nick_lock) {
            return nickname;
        }
    }
    public static void setNickname(String newNickname) {
        synchronized (nick_lock) {
            nickname = newNickname;
        }
    }

    private static SocketHandler socketHandler;
    private static View view;

    public static void main(String[] args) {
        view = askView();
        //view = getDummyView(parseInt(args[0]));

        HashMap<String, String> config = Save.getDatabase("network_config.json", Save.netConfigType);

        if(args.length == 2) {
            config.put("ip", args[0]);
            config.put("port", args[1]);
        } else {
            System.out.println("Using default configuration.");
        }

        System.out.println("Connecting to " + config.get("ip") + " on port " + config.get("port"));

        try { socketHandler = new SocketHandler(config.get("ip"), parseInt(config.get("port")), parseInt(config.get("timeout"))); }
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
                new Thread(new GuiLauncher()).start();
                GuiLauncher.waitForReady();
                return GuiLauncher.getGui();
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
