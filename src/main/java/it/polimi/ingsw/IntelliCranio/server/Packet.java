package it.polimi.ingsw.IntelliCranio.server;

import java.util.ArrayList;

public class Packet {


    public enum InstructionCode {
        DISCARD_INIT_LEADERS,
        CHOOSE_INIT_RES,
        TAKE_RES,
        BUY_DEV_CARD,
        ACT_PROD,
        UPDATE_WAREHOUSE,
        PLAY_LEADER,
        DISCARD_LEADER
    }

    public enum ErrorCode {

    }

    private InstructionCode instructionCode;
    private ErrorCode errorCode;
    private ArrayList<String> jsonArgs;


    public InstructionCode getInstructionCode() {
        return instructionCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ArrayList<String> getArgs() {
        return jsonArgs;
    }
}
