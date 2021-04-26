package it.polimi.ingsw.IntelliCranio.client;

import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.views.DummyView;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.Cli;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;

import java.io.IOException;
import java.util.Scanner;

import static it.polimi.ingsw.IntelliCranio.Utility.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static java.lang.Integer.parseInt;

public class MainClient {

    public static void main(String[] args) {
        //View view = askView();
        View view = getDummyView(parseInt(args[0]));

        SocketHandler socketHandler;
        try { socketHandler = new SocketHandler("localhost",1051); }
        catch (IOException e) { return ; }

        Packet pack;

        while (true) {

            // Client waiting for the instruction given by the server

            do {
                try { pack = socketHandler.receive(); }
                catch (IOException e) { return; }
            } while (pack.getInstructionCode() == PING);

            if (pack.getInstructionCode() == COMMUNICATION) {
                view.showCommunication(pack.getArgs().get(0));
                continue;
            }

            view.setScene(pack.getInstructionCode());

            // Client inside a specific scene where an input is required

            while (pack.getResponse() != ACK) {
                socketHandler.send(createPacketFromInput(view.getInput()));

                do {
                    try { pack = socketHandler.receive(); }
                    catch (IOException e) { return; }
                } while (pack.getInstructionCode() == PING);

                view.displayError(pack.getResponse());
            }
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
