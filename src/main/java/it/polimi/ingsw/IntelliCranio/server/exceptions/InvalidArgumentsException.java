package it.polimi.ingsw.IntelliCranio.server.exceptions;

import it.polimi.ingsw.IntelliCranio.network.Packet;

import static it.polimi.ingsw.IntelliCranio.network.Packet.*;

public class InvalidArgumentsException extends Exception{

    private ErrorCode code;

    public InvalidArgumentsException(ErrorCode code) {
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }

}
