package it.polimi.ingsw.IntelliCranio.server;

import java.util.ArrayList;

public class Packet {

    public enum InstructionCode {
        DISCARD_INIT_LEADERS,
        CHOOSE_INIT_RES,
        TAKE_RES,
        BUY_DEV_CARD,
        ACT_PROD,
        MNG_WAREHOUSE,
        PLAY_LEADER,
        DISCARD_LEADER
    }

    private InstructionCode code;
    private ArrayList<String> jsonArgs;


    public InstructionCode getCode() {
        return code;
    }

    public ArrayList<String> getArgs() {
        return jsonArgs;
    }
}
