package it.polimi.ingsw.IntelliCranio.network;

import java.io.IOException;

public interface NetworkManagerI {
    void send (String name, Packet packet);
    void sendAll (Packet packet);
    Packet receive (String name) throws IOException;
    void disconnect(String name);
    void connect(String name, SocketHandler socketHandler);
    void clear();
}
