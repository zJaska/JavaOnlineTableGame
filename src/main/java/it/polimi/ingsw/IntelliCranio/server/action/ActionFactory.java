package it.polimi.ingsw.IntelliCranio.server.action;


import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import static it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode.*;

public class ActionFactory {

    /**
     * Generates different Action instances.
     *
     * @param packet
     * @return New action based on code received in packet.
     */
    public Action getAction(Packet packet) throws InvalidArgumentsException {
        Packet.InstructionCode code = packet.getInstructionCode();

        if (packet == null) return null;
        if (code == DISCARD_INIT_LEADERS) return new DiscardInitLeaders(packet.getArgs());
        if (code == CHOOSE_INIT_RES) return new ChooseInitResources(packet.getArgs());
        if (code == TAKE_RES) return new TakeResources(packet.getArgs());
        if (code == BUY_DEV_CARD) return new BuyDevCard(packet.getArgs());
        if (code == ACT_PROD) return new ActivateProduction(packet.getArgs());
        if (code == UPDATE_WAREHOUSE) return new UpdateWarehouse(packet.getArgs());
        if (code == PLAY_LEADER) return new PlayLeader(packet.getArgs());
        if (code == DISCARD_LEADER) return new DiscardLeader(packet.getArgs());

        return null;
    }

}
