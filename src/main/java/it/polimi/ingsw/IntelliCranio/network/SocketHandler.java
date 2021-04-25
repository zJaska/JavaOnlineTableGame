package it.polimi.ingsw.IntelliCranio.network;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

import static it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode.ACK;

public class SocketHandler {
    private BufferedReader in;
    private PrintWriter out;

    public SocketHandler(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            setupReaderWriter(socket);
        } catch (IOException e) {
            System.out.println("Unable to setup connection");
        }
    }

    public SocketHandler(Socket socket) {
        setupReaderWriter(socket);
    }

    private void setupReaderWriter(Socket socket) {
        try {
            // Creating reader
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(isr);

            // Creating writer
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bw = new BufferedWriter(osw);
            out = new PrintWriter(bw, true);
        } catch (IOException e) {
            System.out.println("Unable to setup reader or writer");
            return;
        }
    }

    public void send(Packet packet) {
        String tmp = new Gson().toJson(packet,Packet.class);
        out.println(tmp);
    }

    public Packet receive() {
        String tmp = null;
        try {
            tmp = in.readLine();
        } catch (IOException e) {
            System.out.println("Unable to read packet from server");
        }

        return (new Gson().fromJson(tmp,Packet.class));
    }
}
