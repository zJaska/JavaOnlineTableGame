package it.polimi.ingsw.IntelliCranio.server.exceptions;

import it.polimi.ingsw.IntelliCranio.server.Packet;

public class InvalidArgumentsException extends Exception{

    private Packet.InstructionCode code;

    public InvalidArgumentsException(Packet.InstructionCode code) {
        this.code = code;
    }

    public Packet.InstructionCode getCode() {
        return code;
    }

}
