package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.Response;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;

public class CliNicknameScene implements CliScene {

    public void displayOptions() {
        System.out.println("Select your nickname: ");
    }

    public void displayError(Response response) {
        if (response == null || response == ACK)
            return;

        switch (response) {
            case NICKNAME_TAKEN:
                System.out.println("Nickname already taken, choose another one: ");
                break;
            case BAD_ARGUMENTS_NUMBER:
                System.out.println("ERROR, you must input only one word: ");
                break;
            default:
                System.out.println("Select your nickname: (an error has occurred)");
                break;
        }
    }

    public Response isSyntaxCorrect(ArrayList<String> input) {
        if (input.size() != 1)
            return BAD_ARGUMENTS_NUMBER;

        return ACK;
    }

    public InstructionCode getInstructionCode(ArrayList<String> input) {
        return InstructionCode.CHOOSE_NICKNAME;
    }
}
