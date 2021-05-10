package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.TYPE_MISMATCH;

public class Conversions {

    public static int getInteger(ArrayList<Object> args, int index) throws InvalidArgumentsException {
        try {
            return (int)args.get(index);
        } catch (Exception ex) {

            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement index: " + index;
            errorMessage += "\nElement expected: Integer Number";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static Resource getResource(ArrayList<Object> args, int index) throws InvalidArgumentsException {
        try {
            return (Resource)args.get(index);
        } catch (Exception ex) {

            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement index: " + index;
            errorMessage += "\nElement expected: Resource";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static LeadCard getLeader(ArrayList<Object> args, int index) throws InvalidArgumentsException {
        try {
            return (LeadCard) args.get(index);
        } catch (Exception ex) {

            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement index: " + index;
            errorMessage += "\nElement expected: Leader Card";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }
}
