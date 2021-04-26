package it.polimi.ingsw.IntelliCranio.network;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketHandler {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public static final int TIMEOUT = 100*1000;

    public SocketHandler(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("Unable to setup connection");
            throw new IOException();
        }

        setup(socket);
    }

    public SocketHandler(Socket socket) throws IOException {
        setup(socket);
    }

    private void setup(Socket socket) throws IOException {
        try {
            socket.setSoTimeout(TIMEOUT);

            // Creating reader
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(isr);

            // Creating writer
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bw = new BufferedWriter(osw);
            out = new PrintWriter(bw, true);
        } catch (IOException e) {
            System.out.println("Unable to setup connection");
            throw new IOException();
        }
    }

    public void send(Packet packet) {
        synchronized (this) {
            String tmp = new Gson().toJson(packet);
            out.println(tmp);
        }
    }

    public Packet receive() throws IOException {
        synchronized (this) {
            String tmp;
            try {
                tmp = in.readLine();
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout elapsed");
                throw new SocketTimeoutException();
            } catch (IOException e) {
                System.out.println("Unable to read packet, probably the other end disconnected");
                throw new IOException();
            }

            return (new Gson().fromJson(tmp,Packet.class));
        }
    }
}
