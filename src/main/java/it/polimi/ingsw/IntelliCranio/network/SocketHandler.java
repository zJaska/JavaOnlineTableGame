package it.polimi.ingsw.IntelliCranio.network;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketHandler {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    public static final int TIMEOUT = 1000*1000;

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
        this.socket = socket;
        setup(socket);
    }

    private void setup(Socket socket) throws IOException {
        try {

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            socket.setSoTimeout(TIMEOUT);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to setup input and output streams");
            throw new IOException();
        }
    }

    public void send (Packet packet) {
        try { out.writeObject(packet); out.reset(); }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to send the object for network problems");
        } catch (Exception e) {
            System.err.println("Unable to send packet, problems with object serialization");
        }
    }

    public Packet receive() throws IOException {
        Packet tmp = null;
        try {
            tmp = (Packet) in.readObject();
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout elapsed");
            throw new SocketTimeoutException();
        } catch (IOException e) {
            System.out.println("Unable to read packet, probably the other end disconnected");
            throw new IOException();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to read the packet, there are problem with serialization");
        }

        return tmp;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public void close() {
        try {
            out.close();
            in.close();
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to close the socket");
        }
    }
}
