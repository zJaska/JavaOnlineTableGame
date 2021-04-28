package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.CODE_NOT_ALLOWED;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.CODE_NULL;

public class CardMarket_ActionState extends ActionState{

    private Game game;

    public CardMarket_ActionState(Action action){
        super(action);
    }

    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        if(packet == null || packet.getInstructionCode() == null) throw new InvalidArgumentsException(CODE_NULL);


        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state


    }
}
