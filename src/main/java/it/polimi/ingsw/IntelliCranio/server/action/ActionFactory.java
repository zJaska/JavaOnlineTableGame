package it.polimi.ingsw.IntelliCranio.server.action;


import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

public class ActionFactory {

    /**
     * Generates different Action instances.
     *
     * @param packet
     * @return New action based on code received in packet.
     */
    public Action getAction(Packet packet) throws InvalidArgumentsException {
        InstructionCode code = packet.getCode();

        if (packet == null) return null;
        if (code == InstructionCode.DISCARD_INIT_LEADERS) return new DiscardInitLeaders(packet.getArgs());
        if (code == InstructionCode.CHOOSE_INIT_RES) return new ChooseInitResources(packet.getArgs());
        if (code == InstructionCode.TAKE_RES) return new TakeResources(packet.getArgs());
        if (code == InstructionCode.BUY_DEV_CARD) return new BuyDevCard(packet.getArgs());
        if (code == InstructionCode.ACT_PROD) return new ActivateProduction(packet.getArgs());
        if (code == InstructionCode.UPDATE_WAREHOUSE) return new UpdateWarehouse(packet.getArgs());
        if (code == InstructionCode.PLAY_LEADER) return new PlayLeader(packet.getArgs());
        if (code == InstructionCode.DISCARD_LEADER) return new DiscardLeader(packet.getArgs());

        return null;
    }

}
