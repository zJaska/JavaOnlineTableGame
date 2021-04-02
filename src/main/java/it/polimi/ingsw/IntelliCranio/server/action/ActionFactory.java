package it.polimi.ingsw.IntelliCranio.server.action;


import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

public class ActionFactory {

    /**
     * Generates different Action instances.
     *
     * @param packet
     * @return New action based on code received in packet.
     */
    public Action getAction(Packet packet) throws InvalidArgumentsException {
        Packet.InstructionCode code = packet.getCode();

        if (packet == null) return null;
        if (code == Packet.InstructionCode.DISCARD_INIT_LEADERS) return new DiscardInitLeaders(packet.getArgs());
        if (code == Packet.InstructionCode.CHOOSE_INIT_RES) return new ChooseInitResources(packet.getArgs());
        if (code == Packet.InstructionCode.TAKE_RES) return new TakeResources(packet.getArgs());
        if (code == Packet.InstructionCode.BUY_DEV_CARD) return new BuyDevCard(packet.getArgs());
        if (code == Packet.InstructionCode.ACT_PROD) return new ActivateProduction(packet.getArgs());
        if (code == Packet.InstructionCode.UPDATE_WAREHOUSE) return new UpdateWarehouse(packet.getArgs());
        if (code == Packet.InstructionCode.PLAY_LEADER) return new PlayLeader(packet.getArgs());
        if (code == Packet.InstructionCode.DISCARD_LEADER) return new DiscardLeader(packet.getArgs());

        return null;
    }

}
