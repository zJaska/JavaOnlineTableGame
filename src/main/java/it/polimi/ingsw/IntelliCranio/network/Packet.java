package it.polimi.ingsw.IntelliCranio.network;

import java.util.ArrayList;

public class Packet {

    public enum InstructionCode {
        //Action Codes
        DEFAULT,
        DISCARD_INIT_LEAD,
        CHOOSE_INIT_RES,
        MNG_WARE,
        RES_MARKET,
        CARD_MARKET,
        ACT_PROD,

        // Instruction codes
        CANCEL,
        CONFIRM,
        DISCARD_LEAD,
        CHOOSE_RES,
        SWAP_LINES,
        ADD_FROM_EXTRA,
        REMOVE_FROM_DEPOT,
        DEPOT_TO_CARD,
        EXTRA_TO_CARD,


        // Setup codes
        CHOOSE_NICKNAME,
        CHOOSE_NUMBER_PLAYERS,

        // Logic codes
        COMMUNICATION,
        DIE,
        PING
    }

    public enum Response {
        ACK,

        // Action errors
        CODE_NULL,
        CODE_NOT_ALLOWED,
        NOT_ENOUGH_ARGS,
        TOO_MANY_ARGS,
        TYPE_MISMATCH,
        NULL_ARG,
        SELECTION_INVALID,
        VALUE_INVALID,
        STATE_INVALID,

        // Setup network errors
        NICKNAME_TAKEN,

        // Setup syntax errors
        BAD_ARGUMENTS_NUMBER,
        OUT_OF_BOUNDS,
        NOT_A_NUMBER
    }

    private InstructionCode instructionCode;
    private Response response;
    private ArrayList<Object> args;

    public Packet(InstructionCode instructionCode, Response response, ArrayList<Object> args) {
        this.instructionCode = instructionCode;
        this.response = response;
        this.args = args;
    }

    public InstructionCode getInstructionCode() {
        return instructionCode;
    }

    public Response getResponse() {
        return response;
    }

    public ArrayList<Object> getArgs() {
        return args;
    }
}
