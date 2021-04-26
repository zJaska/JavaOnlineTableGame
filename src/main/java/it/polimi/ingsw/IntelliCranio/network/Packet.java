package it.polimi.ingsw.IntelliCranio.network;

import java.util.ArrayList;

public class Packet {

    public enum InstructionCode {
        // Action codes
        DISCARD_LEAD,
        CHOOSE_RES,

        // Setup codes
        CHOOSE_NICKNAME,
        CHOOSE_NUMBER_PLAYERS,
        COMMUNICATION,

        // Logic codes
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
        NOT_IN_HAND,
        SELECTION_INVALID,
        VALUE_INVALID,

        // Setup network errors
        NICKNAME_TAKEN,

        // Setup syntax errors
        BAD_ARGUMENTS_NUMBER,
        OUT_OF_BOUNDS,
        NOT_A_NUMBER
    }

    private InstructionCode instructionCode;
    private Response response;
    private ArrayList<String> jsonArgs;

    public Packet(InstructionCode instructionCode, Response response, ArrayList<String> jsonArgs) {
        this.instructionCode = instructionCode;
        this.response = response;
        this.jsonArgs = jsonArgs;
    }

    public InstructionCode getInstructionCode() {
        return instructionCode;
    }

    public Response getResponse() {
        return response;
    }

    public ArrayList<String> getArgs() {
        return jsonArgs;
    }
}
