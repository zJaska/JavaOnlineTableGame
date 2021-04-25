package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode;

import static java.lang.Integer.parseInt;

public class CliChooseNumberPlayersScene implements CliScene {
    public void displayOptions(ErrorCode option) {
        switch (option) {
            case OUT_OF_BOUNDS:
                System.out.println("ERROR, choose between 2 and 4: ");
                break;
            case NOT_A_NUMBER:
                System.out.println("ERROR, you must input a number: ");
                break;
            default:
                System.out.println("Choose the number of players of the party (2 to 4): ");
                break;
        }
    }

    public ErrorCode isSintaxCorrect(String input) {
        try {
            int num = parseInt(input);
            if (num >= 2 && num <= 4)
                return ErrorCode.ACK;
            else
                return ErrorCode.OUT_OF_BOUNDS;
        } catch (NumberFormatException e) { }
        return ErrorCode.NOT_A_NUMBER;
    }
}
