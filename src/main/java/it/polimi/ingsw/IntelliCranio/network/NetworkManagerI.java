package it.polimi.ingsw.IntelliCranio.network;

public interface NetworkManagerI {
    void send(String name, Packet packet);
    Packet receive(String name);
    void clear();
}
