package it.polimi.ingsw.IntelliCranio.server;

public class Packet {

    public enum InstructionCode {
        CHOOSE_LEADERS,
        CHOOSE_INIT_RES,
        TAKE_RES,
        BUY_DEV_CARD,
        ACT_PROD,
        MNG_WAREHOUSE,
        PLAY_LEADER,
        DISCARD_LEADER
    }

    private InstructionCode code;
    private String[] args;


    public InstructionCode getCode() {
        return code;
    }

    public String[] getArgs() {
        return args;
    }
}
