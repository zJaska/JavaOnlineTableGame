package it.polimi.ingsw.IntelliCranio.server.exceptions;

import it.polimi.ingsw.IntelliCranio.network.Packet;

import static it.polimi.ingsw.IntelliCranio.network.Packet.*;

public class InvalidArgumentsException extends Exception{

    private Response code;

    public InvalidArgumentsException(Response code) {
        this.code = code;
    }

    public Response getCode() {
        return code;
    }

}
