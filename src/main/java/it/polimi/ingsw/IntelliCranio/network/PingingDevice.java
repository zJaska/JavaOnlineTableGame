package it.polimi.ingsw.IntelliCranio.network;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.PING;

public class PingingDevice implements Runnable {

    SocketHandler socketHandler;

    public PingingDevice(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void run() {
        while (true) {
            try { Thread.sleep(SocketHandler.TIMEOUT/3); }
            catch (Exception e) {}

            try { socketHandler.send(new Packet(PING,null,null)); }
            catch (Exception e) { return; }
        }

    }
}
