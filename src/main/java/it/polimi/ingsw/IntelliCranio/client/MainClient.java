package it.polimi.ingsw.IntelliCranio.client;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.views.DummyView;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.Cli;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;

import java.io.IOException;
import java.util.Scanner;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.ACK;
import static it.polimi.ingsw.IntelliCranio.util.Net.createPacketFromInput;

public class MainClient {

    static SocketHandler socketHandler;
    static View view;

    public static void main(String[] args) {
        view = askView();
        // view = getDummyView(parseInt(args[0]));

        try { socketHandler = new SocketHandler("localhost",1051); }
        catch (IOException e) { return ; }

        Packet pack;

        while (true) {

            // Client waiting for the instruction given by the server

            pack = receivePacket();

            // Client inside a specific scene where an input is required

            view.setScene(pack.getInstructionCode());

            while (pack.getResponse() != ACK) {
                socketHandler.send(createPacketFromInput(view.getInput()));

                pack = receivePacket();

                view.displayError(pack.getResponse());
            }
        }
    }

    private static Packet receivePacket() {
        Packet pack = null;

        try { pack = socketHandler.receive(); }
        catch (IOException e) {
            System.exit(-100);
        }

        switch (pack.getInstructionCode()) {
            case PING:
                return receivePacket();
            case COMMUNICATION:
                view.showCommunication(pack.getArgs().get(0));
                return receivePacket();
            case DIE:
                System.exit(-101);
            default:
                return pack;
        }
    }

    private static View askView() {
        String tmp;
        do {
            System.out.println("Select the view you want for the game:\n1) CLI\n2) GUI");
            Scanner in = new Scanner(System.in);
            tmp = in.nextLine();
        } while (tmp.equals("1") && tmp.equals("2"));

        switch (tmp) {
            case "1":
                return new Cli();
            case "2":
                return new Gui();
        }

        return null;
    }

    private static View getDummyView(int count) {
        String[][] inputs = null;

        switch (count) {
            case 0:
                inputs = new String[][] {
                        {"ste"},
                        {"2"}
                };
                break;
            case 1:
                inputs = new String[][] {
                        {"ste"},
                        {"anna"},
                        {"3"}
                };
                break;

        }

        return new DummyView(inputs);
    }
}
