package it.polimi.ingsw.IntelliCranio.network;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.PING;

public class PingingDevice implements Runnable {

    private static ConcurrentHashMap<SocketHandler, Boolean> disconnectedSockets = new ConcurrentHashMap<>();
    public static boolean isDisconnected(SocketHandler sh) {
        return disconnectedSockets.get(sh);
    }


    SocketHandler socketHandler;

    public PingingDevice(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void run() {
        disconnectedSockets.put(socketHandler, false);

        while (true) {
            try { Thread.sleep(socketHandler.getTimeout()/3); }
            catch (Exception e) {}

            try { socketHandler.sendThrow(new Packet(PING,null,null)); }
            catch (Exception e) {
                System.err.println("Shutting down pinging device");
                disconnectedSockets.put(socketHandler, true);
                return;
            }
        }

    }
}
