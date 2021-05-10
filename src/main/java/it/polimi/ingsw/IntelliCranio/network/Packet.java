package it.polimi.ingsw.IntelliCranio.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {

    public enum InstructionCode {
        //Action Codes
        DEFAULT,
        DISCARD_INIT_LEAD,
        CHOOSE_INIT_RES,
        MNG_WARE,
        RES_MARKET,
        CARD_MARKET,
        ACT_PROD,
        IDLE,

        // Instruction codes
        CANCEL,
        CONFIRM,
        END_TURN,

        DISCARD_LEAD,

        PLAY_LEADER,

        CHOOSE_RES,

        SWAP_LINES,
        ADD_FROM_EXTRA,
        REMOVE_FROM_DEPOT,
        DEPOT_TO_CARD,
        EXTRA_TO_CARD,

        SELECT_ROW,
        SELECT_COLUMN,

        RES_FROM_DEPOT,
        RES_FROM_STRONG,
        RES_FROM_CARD,
        SELECT_SLOT, //0 = base, 1-3 = dev slot
        SELECT_CARD,


        // Setup codes
        CHOOSE_NICKNAME,
        CHOOSE_NUMBER_PLAYERS,
        WANNA_PLAY_ALONE,
        NICKNAME,
        ALONE,
        MULTIPLAYER,

        // Logic codes
        GAME,
        COMMUNICATION,
        DIE,
        PING
    }

    public enum Response {
        ACK,

        // Action errors
        PACKET_NULL,
        CODE_NULL,
        ARGS_NULL,
        CODE_NOT_ALLOWED,
        NOT_ENOUGH_ARGS,
        TOO_MANY_ARGS,
        TYPE_MISMATCH,
        NULL_ARG,
        SELECTION_INVALID,
        VALUE_INVALID,
        STATE_INVALID,

        // Setup network errors
        NICKNAME_TAKEN
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
