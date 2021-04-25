package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode;

import static it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode.ACK;
import static it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode.NOT_ONE_WORD;

public class CliNicknameScene implements CliScene {

    public void displayOptions(ErrorCode option) {
        switch ((option != null) ? option : ACK) {
            case NICKNAME_TAKEN:
                System.out.println("Nickname already taken, choose another one: ");
                break;
            case NOT_ONE_WORD:
                System.out.println("ERROR, you must input only one word: ");
                break;
            default:
                System.out.println("Select your nickname: ");
                break;
        }
    }

    public ErrorCode isSintaxCorrect(String input) {
        if (input.contains(" ") || input.contains("\n"))
            return NOT_ONE_WORD;
        return ACK;
    }
}
