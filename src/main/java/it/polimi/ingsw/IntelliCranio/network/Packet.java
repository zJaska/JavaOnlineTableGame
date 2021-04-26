package it.polimi.ingsw.IntelliCranio.network;

import java.util.ArrayList;

public class Packet {

    public enum InstructionCode {
        DISCARD_LEAD,
        CHOOSE_RES,

        CHOOSE_NICKNAME,
        CHOOSE_NUMBER_PLAYERS
    }

    public enum ErrorCode {
        ACK,

        CODE_NULL,
        CODE_NOT_ALLOWED,
        NOT_ENOUGH_ARGS,
        TOO_MANY_ARGS,
        TYPE_MISMATCH,
        NULL_ARG,
        NOT_IN_HAND,
        SELECTION_INVALID,
        VALUE_INVALID,

        NICKNAME_TAKEN,

        NOT_ONE_WORD,
        OUT_OF_BOUNDS,
        NOT_A_NUMBER
    }

    private InstructionCode instructionCode;
    private ErrorCode errorCode;
    private ArrayList<String> jsonArgs;

    public Packet(InstructionCode instructionCode, ErrorCode errorCode, ArrayList<String> jsonArgs) {
        this.instructionCode = instructionCode;
        this.errorCode = errorCode;
        this.jsonArgs = jsonArgs;
    }

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
