package it.polimi.ingsw.IntelliCranio.client;

import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.views.View;
import it.polimi.ingsw.IntelliCranio.views.cli.Cli;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;

import java.util.ArrayList;
import java.util.Scanner;

import static it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode.ACK;

public class MainClient {

    public static void main(String[] args) {
        View view = askView();

        SocketHandler socketHandler = new SocketHandler("localhost",1051);

        ErrorCode err = null;
        InstructionCode IC = socketHandler.receive().getInstructionCode();
        do {
            socketHandler.send(view.setScene(IC, true, err));
        } while ((err = socketHandler.receive().getErrorCode()) != ACK);

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
}
