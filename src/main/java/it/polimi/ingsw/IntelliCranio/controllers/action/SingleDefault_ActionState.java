package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.SinglePlayerData;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;

import static it.polimi.ingsw.IntelliCranio.models.SinglePlayerData.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class SingleDefault_ActionState extends Default_ActionState{

    public SingleDefault_ActionState(Action action) {
        super(action);
    }

    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {

        this.game = game;

        Checks.packetCheck(packet);

        if(packet.getInstructionCode().equals(END_TURN)) {
            endTurn();
            return;
        }

        super.execute(game, packet);
    }

    @Override
    protected void endTurn() {

        //Azioni di Lorenzo il Magnifico

        Token token = game.getSinglePlayerData().getToken();

        switch (token) {

            case BLACK_CROSS: addLorenzoFaith(2); break;
            case SHUFFLE_CROSS: addLorenzoFaith(1); break;
            default: removeCards(token, 2); break;

        }

        super.endTurn();
    }

    private void removeCards(Token token, int amount) {

        CardMarket cm = game.getCardMarket();

        int col;

        //Find correct column for type
        for(col = 0; col < cm.cols; ++col) {
            if(token.toString().equals(cm.getCard(0, col).getType().toString()))
                break;
        }

        int row = cm.rows - 1;

        for(int i = 0; i < amount; ++i) {

            if (cm.getCard(row, col) != null) {
                cm.removeCard(row, col);

                if(cm.getCard(0, col) == null)
                    game.endGame(true);
            }
            else {
                row--;
                i--;
            }
        }

    }

    private void addLorenzoFaith(int amount) {

        SinglePlayerData data = game.getSinglePlayerData();
        int ftLength = game.getFaithTrack().getTrackLength();

        for(int i = 0; i < amount; ++i) {
            if(data.getLorenzoFaith() < ftLength) {
                data.addLorenzoFaith();
                game.getFaithTrack().checkStatus(game, data.getLorenzoFaith());
            }
        }
    }


}
