package it.polimi.ingsw.IntelliCranio.client;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.View;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class RunningView implements Runnable {

    View view;

    private static Semaphore dataReady = new Semaphore(0);

    private static Pair<InstructionCode, ArrayList<Object>> input;

    public RunningView(View view) {
        this.view = view;
    }

    public void run() {
        while (true) {
            input = view.getInput();

            if (input != null && dataReady.availablePermits() <= 0) {
                dataReady.release();
            }

            if (input == null) {
                try { dataReady.acquire(dataReady.availablePermits()); }
                catch (InterruptedException e) {}
            }

        }
    }

    public static Pair<InstructionCode, ArrayList<Object>> getData() {
        try { dataReady.acquire(); }
        catch (InterruptedException e) {}

        return input;
    }
}
