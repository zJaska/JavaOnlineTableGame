package it.polimi.ingsw.IntelliCranio.server.exceptions;

import static it.polimi.ingsw.IntelliCranio.network.Packet.*;

public class InvalidArgumentsException extends Exception{

    private Response code;
    private String errorMessage;

    public InvalidArgumentsException(Response code) {
        this.code = code;
    }

    public Response getCode() {
        return code;
    }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

}
