package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.Response;

import java.util.ArrayList;

public interface CliScene {

    void displayOptions();

    void displayError(Response error);

    Response isSyntaxCorrect(ArrayList<String> input);

    InstructionCode getInstructionCode(ArrayList<String> input);
}
