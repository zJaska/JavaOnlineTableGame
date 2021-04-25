package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode;

public interface CliScene {
    void displayOptions(ErrorCode option);
    ErrorCode isSintaxCorrect(String input);
}
