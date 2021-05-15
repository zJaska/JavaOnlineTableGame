package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.player.Strongbox;
import it.polimi.ingsw.IntelliCranio.models.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;
import it.polimi.ingsw.IntelliCranio.util.Conversions;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType.*;

public class CardMarket_ActionState extends ActionState{

    private Game game;

    private DevCard selected;
    private ArrayList<Resource> costResources = new ArrayList<>();
    private boolean confirmed;

    public CardMarket_ActionState(Action action){
        super(action);
    }

    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        Checks.packetCheck(packet);

        switch (packet.getInstructionCode()) {
            case SELECT_CARD: selectCard(packet.getArgs()); return;
            case RES_FROM_DEPOT: resFromDepot(packet.getArgs()); return;
            case RES_FROM_STRONG: resFromStrongbox(packet.getArgs()); return;
            case RES_FROM_CARD: resFromCard(packet.getArgs()); return;
            case CANCEL: cancel(); return;
            case CONFIRM: confirm(); return;
            case SELECT_SLOT: selectSlot(packet.getArgs()); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state
        }
    }


    private void selectCard(ArrayList<Object> args) throws InvalidArgumentsException {

        int row, col; //Expected args

        //Arg amount check
        Checks.argsAmount(args, 2);

        row = Conversions.getInteger(args, 0);
        col = Conversions.getInteger(args, 1);


        Player player = game.getCurrentPlayer();
        CardMarket cm = game.getCardMarket();
        DevCard card;

        //region Argument Validation

        //Card already selected
        Checks.cardSelected(selected);

        //Negative Condition
        Checks.negativeValue(row);
        Checks.negativeValue(col);

        //Over Size Condition
        Checks.overSizeLine(row, cm.rows);
        Checks.overSizeLine(col, cm.cols);

        //Empty condition
        Checks.cardMarketEmpty(cm, row, col);

        //Invalid Level Condition
        card = cm.getCard(row, col);
        Checks.invalidLevel(player, card);

        //endregion


        selected = card;

        // TODO: questa riga l'ha messa STE
        action.setActionState(this, CARD_MARKET_AS);
    }

    private void resFromDepot(ArrayList<Object> args) throws InvalidArgumentsException {

        int depotLine; //Expected arg for action

        //Check amount
        Checks.argsAmount(args, 1);

        depotLine = Conversions.getInteger(args, 0);

        Warehouse wh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Already confirmed
        Checks.alreadyConfirmed(confirmed);

        //No card selected
        Checks.cardNotSelected(selected);

        //Negative Line
        Checks.negativeValue(depotLine);

        //Over size Condition
        Checks.overSizeLine(depotLine, wh.getDepot().length);

        //Empty condition
        Checks.depotEmpty(wh, depotLine);

        //endregion

        //region execute

        //Add the resource to cost selection
        costResources.add(new Resource(wh.getType(depotLine), 1));

        //Remove resource from depot
        wh.remove(depotLine);

        //endregion

    }

    private void resFromStrongbox(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected arg

        //Args check
        Checks.argsAmount(args, 1);

        resource = Conversions.getResource(args, 0);

        Strongbox sb = game.getCurrentPlayer().getStrongbox();

        //region Argument Validation

        //Already confirmed
        Checks.alreadyConfirmed(confirmed);

        //No card selected
        Checks.cardNotSelected(selected);

        //Null Condition
        Checks.nullElement(resource, "Resource");
        Checks.nullElement(resource.getType(), "A Resource Type");

        //Strongbox empty
        Checks.strongboxEmpty(sb, resource.getType());

        //endregion

        //region execute

        //Add the resource
        costResources.add(new Resource(resource.getType(), 1));

        //Remove from strongbox
        sb.removeResources(resource.getType(), 1);

        //endregion
    }

    private void resFromCard(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //Expected arg

        //Arg amount check
        Checks.argsAmount(args, 1);

        card = Conversions.getLeader(args, 0);


        Player player = game.getCurrentPlayer();
        LeadCard serverCard = player.getLeader(card);

        //region Argument Validation

        //Already confirmed
        Checks.alreadyConfirmed(confirmed);

        //No card selected
        Checks.cardNotSelected(selected);

        //Null Condition
        Checks.nullElement(card, "Leader Card");

        //Not in hand
        Checks.notInHand(player, card);

        //Invalid Ability
        Checks.invalidAbility(serverCard, DEPOT);

        //Inactive
        Checks.cardInactive(serverCard);

        //Card Depot empty
        Checks.cardDepotEmpty(serverCard);

        //endregion

        //region execute

        //Add a unit resource of the same type as the given card
        costResources.add(new Resource(serverCard.getResourceType(), 1));

        //Remove one unit from card
        ((DepotAbility)serverCard.getSpecialAbility()).removeResource();

        //endregion
    }

    private void cancel() throws InvalidArgumentsException {

        Checks.alreadyConfirmed(confirmed);

        game.loadGame(Save.loadGame(game.getUuid()));

        if(selected != null)
            action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        else
            action.setActionState(new Default_ActionState(action), DEFAULT); //Go back to default if nothing was selected

    }

    private void confirm() throws InvalidArgumentsException {

        //Already Confirmed
        Checks.alreadyConfirmed(confirmed);

        //No card selected
        Checks.cardNotSelected(selected);


        Player player = game.getCurrentPlayer();
        ArrayList<Resource> cardCost = new ArrayList<>();

        //Change from FinalResources to Resources List
        selected.getCardCost().forEach(res -> cardCost.add(new Resource(res.getType(), res.getAmount())));

        //Check for sale ability leader and if active decrease the card cost
        if(player.getLeaders().stream().anyMatch(lead -> (lead.getAbilityType() == SALE && lead.isActive()))) {

            ArrayList<LeadCard> activeSaleLeaders = player.getLeaders().stream()
                    .filter(lead -> (lead.getAbilityType() == SALE && lead.isActive()))
                    .collect(Collectors.toCollection(ArrayList::new));

            activeSaleLeaders.forEach(lead -> {
                        if (cardCost.stream().anyMatch(res -> res.getType() == lead.getResourceType()))

                            //Remove 1 unit of same resource type from costs list
                            cardCost.stream()
                                    .filter(res -> res.getType() == lead.getResourceType())
                                    .findFirst().get().removeAmount(1);
                    }
            );
        }

        //Check for cost match
        //Checks.invalidCardCostResources(costResources, cardCost);
        Checks.invalidCardCostResources(costResources, cardCost);

        //Get here if cost resources selected are the same as card resources
        confirmed = true;
    }

    private void selectSlot(ArrayList<Object> args) throws InvalidArgumentsException {

        Checks.notConfirmed(confirmed);

        int slot; //Expected arg

        //Arg amount check
        Checks.argsAmount(args, 1);

        slot = Conversions.getInteger(args, 0);

        Player player = game.getCurrentPlayer();

        //region Arguments Validation

        //Negative condition
        Checks.negativeValue(slot);

        //Over size
        Checks.overSizeLine(slot, player.getFirstDevCards().length);

        //Invalid slot
        Checks.invalidSlot(player.getFirstDevCards()[slot], selected);

        //endregion


        //Add the card to player slot
        player.addDevCard(selected, slot);

        //Check for game ended
        if(player.getAllDevCards().size() == 7)
            game.endGame(true);

        // TODO: ste
        action.setActionState(new Default_ActionState(action), DEFAULT);
        game.getCurrentPlayer().setLastAction(DEFAULT);
        Save.saveGame(game);
    }

}
