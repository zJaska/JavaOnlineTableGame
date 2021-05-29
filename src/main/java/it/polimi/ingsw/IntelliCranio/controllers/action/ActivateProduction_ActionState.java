package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.player.Strongbox;
import it.polimi.ingsw.IntelliCranio.models.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.models.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;
import it.polimi.ingsw.IntelliCranio.util.Conversions;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.models.ability.Ability.AbilityType.*;

public class ActivateProduction_ActionState extends ActionState{

    private boolean baseProduction = false;
    private ArrayList<DevCard> slotCards = new ArrayList<>();
    private ArrayList<LeadCard> leadCards = new ArrayList<>();

    private ArrayList<FinalResource> costResources = new ArrayList<>();
    private ArrayList<Resource> selectedResources = new ArrayList<>();


    public ActivateProduction_ActionState(Action action) {
        super(action);
    }

    /**
     * Execute the action relative to the code received with packet.
     * Actions for this state:
     * SELECT_SLOT: Choose a production slot to enable/disable (0 = base, 1 to 3 for slots)
     * SELECT_CARD: Choose a leader PRODUCTION card to enable/disable
     * RES_FROM_DEPOT: Pick a resource from warehouse
     * RES_FROM_STRONG: Pick a resource from strongbox
     * RES_FROM_CARD: Pick a resource from a leader DEPOT card
     * CHOOSE_RES: Select a resource to obtain from production
     * CANCEL: Cancel all the changes and go back to initial config.
     * CONFIRM: Check for data consistency and give all the production results
     * @param game The game to update
     * @param packet Packet received from client
     * @throws InvalidArgumentsException
     */
    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        Checks.packetCheck(packet);

        switch (packet.getInstructionCode()) {
            case SELECT_SLOT: selectSlot(packet.getArgs()); return;
            case SELECT_CARD: selectCard(packet.getArgs()); return;
            case RES_FROM_DEPOT: resFromDepot(packet.getArgs()); return;
            case RES_FROM_STRONG: resFromStrongbox(packet.getArgs()); return;
            case RES_FROM_CARD: resFromCard(packet.getArgs()); return;
            case CHOOSE_RES: chooseResource(packet.getArgs()); return;
            case CANCEL: cancel(); return;
            case CONFIRM: confirm(); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state

        }
    }


    private void selectSlot(ArrayList<Object> args) throws InvalidArgumentsException {

        int slot; //Expected arg for action (0 to 3)

        Checks.argsAmount(args, 1);

        slot = Conversions.getInteger(args, 0);


        Player player = game.getCurrentPlayer();

        //region Args validation

        //Negative Condition
        Checks.negativeValue(slot);

        //OverSize Condition
        Checks.overMaxValue(slot, 3);

        //Check for dev slot empty
        Checks.slotEmpty(player, slot);

        //endregion


        //region execute

        if(slot == 0)
            //toggle base production
            baseProduction = !baseProduction;
        else {
            DevCard selectedCard = player.getFirstDevCards()[slot - 1];

            //toggle card present in slot
            if(slotCards.contains(selectedCard))
                slotCards.remove(selectedCard);
            else
                slotCards.add(selectedCard);
        }

        //endregion

    }

    private void selectCard(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //Expected arg for this

        //Check amount
        Checks.argsAmount(args, 1);

        card = Conversions.getLeader(args, 0);


        Player player = game.getCurrentPlayer();
        LeadCard serverCard = player.getLeader(card);

        //region Argument Validation

        //Null condition
        Checks.nullElement(card, "Leader Card");

        //Not in hand
        Checks.notInHand(player, card);

        //Invalid ability
        Checks.invalidAbility(serverCard, PRODUCTION);

        //Inactive card
        Checks.cardInactive(serverCard);

        //endregion

        //region execute

        if(leadCards.contains(serverCard))
            leadCards.remove(serverCard);
        else
            leadCards.add(serverCard);

        //endregion


    }

    private void resFromDepot(ArrayList<Object> args) throws InvalidArgumentsException {

        int depotLine; //Expected arg for action

        //Check amount
        Checks.argsAmount(args, 1);

        depotLine = Conversions.getInteger(args, 0);

        Warehouse wh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

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

    private void chooseResource(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected arg

        //Arg amount check
        Checks.argsAmount(args, 1);

        resource = Conversions.getResource(args, 0);


        //region Argument Validation

        //Null Condition
        Checks.nullElement(resource, "Resource");
        Checks.nullElement(resource.getType(), "A Type of Resource");

        //Invalid Type (BLANK, FAITH)
        Checks.blankOrFaith(resource);

        //endregion


        //Add a unit of selected resource type
        selectedResources.add(new Resource(resource.getType(), 1));
    }

    private void cancel() {

        //Reset game status
        game.loadGame(Save.loadGame(game.getUuid()));

        //Reset action status
        action.setActionState(new Default_ActionState(action), DEFAULT);

    }

    private void confirm() throws InvalidArgumentsException {

        ArrayList<FinalResource> devCardCosts = new ArrayList<>(); //All expected costs from dev cards

        //region Cost Amount Condition
        int expectedCostAmount = 0;
        int actualCostAmount = 0;

        //region Expected Costs sum

        //Add base production costs
        if(baseProduction)
            expectedCostAmount += 2;

        //Add leader cards cost (1 for each card)
        expectedCostAmount += leadCards.size();

        //Add dev card costs
        slotCards.stream()
                .map(DevCard::getProductionCost) //Stream of ArrayList<FinalResource>
                .forEach(devCardCosts::addAll);

        devCardCosts = Lists.unifyResourceAmounts(devCardCosts);

        expectedCostAmount += devCardCosts.stream().mapToInt(FinalResource::getAmount).sum();

        //endregion

        actualCostAmount += costResources.stream().mapToInt(FinalResource::getAmount).sum();

        Checks.invalidAmount(actualCostAmount, expectedCostAmount);
        //endregion

        //region Selection Amount Condition

        int expectedSelectionAmount = 0;
        int actualSelectionAmount = 0;

        if(baseProduction)
            expectedSelectionAmount += 1;

        expectedSelectionAmount += leadCards.size();

        actualSelectionAmount += selectedResources.stream().mapToInt(FinalResource::getAmount).sum();

        Checks.invalidAmount(actualSelectionAmount, expectedSelectionAmount);

        //endregion

        //region Invalid Cost Resources Condition

        ArrayList<FinalResource> allCosts = new ArrayList<>();
        allCosts.addAll(devCardCosts);

        //Add resource cost from lead cards
        for (LeadCard lead : leadCards) {
            allCosts.add(new FinalResource(lead.getResourceType(), 1));
        }

        allCosts = Lists.unifyResourceAmounts(allCosts);
        costResources = Lists.unifyResourceAmounts(costResources);

        Checks.invalidProdCostResources(costResources, allCosts);

        //endregion

        //region Add resources to player

        ArrayList<FinalResource> allProduct = new ArrayList<>();

        //Dev Card
        slotCards.stream().map(DevCard::getProduct).forEach(allProduct::addAll);
        allProduct.addAll(selectedResources);
        allProduct = Lists.unifyResourceAmounts(allProduct);

        Player player = game.getCurrentPlayer();

        //Add faith from devcards
        allProduct.stream().filter(res -> res.getType().equals(FAITH)).forEach(faith -> {
            for(int i = 0; i < faith.getAmount(); ++i)
                player.addFaith();
        });

        //Add resources to player strongbox
        Strongbox sb = game.getCurrentPlayer().getStrongbox();
        allProduct.stream()
                .filter(res -> !res.getType().equals(FAITH))
                .forEach(res -> sb.addResources(res.getType(), res.getAmount()));

        //Add faith for each leader used
        leadCards.forEach(lead -> game.addCurrentPlayerFaith());

        //endregion

        Save.saveGame(game);
        game.getCurrentPlayer().hasPlayed = true;
        action.setActionState(new Default_ActionState(action), DEFAULT);

    }

}
