package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.models.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainServer {
    private static final int PORT = 1051;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            try {
                new Thread(new ClientHandler(serverSocket.accept())).start();
            }
            catch (IOException e) {
                System.err.println("A client failed to be accepted");
            }
        }
    }
}
