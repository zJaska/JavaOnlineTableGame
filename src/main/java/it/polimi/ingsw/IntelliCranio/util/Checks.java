package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType;
import it.polimi.ingsw.IntelliCranio.server.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.BLANK;
import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.FAITH;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_INIT_RES;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DEFAULT;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType.DEPOT;

public class Checks {

    public static void argsAmount(ArrayList<Object> args, int expectedAmount) throws InvalidArgumentsException {

        if(args.size() < expectedAmount) {
            InvalidArgumentsException e = new InvalidArgumentsException(NOT_ENOUGH_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received less arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: " + expectedAmount;

            e.setErrorMessage(errorMessage);

            throw e;
        }
        if(args.size() > expectedAmount) {
            InvalidArgumentsException e = new InvalidArgumentsException(TOO_MANY_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received more arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: " + expectedAmount;

            e.setErrorMessage(errorMessage);

            throw e;
        }

    }

    public static void sameLine(int first, int second) throws InvalidArgumentsException {
        if(first == second) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected lines are the same";
            errorMessage += "\nFirst line: " + (first + 1);
            errorMessage += "\nSecond line: " + (second + 1);

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void negativeLine(int line) throws InvalidArgumentsException {
        if(line < 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! One or more values selected are negative";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void overSizeLine(int line, int size) throws InvalidArgumentsException{
        if(line >= size) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! One or more values selected exceed maximum size";
            errorMessage += "\nSelected value: " + (line + 1);
            errorMessage += "\nMax size: " + size;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void nullElement(Object element, String expected) throws InvalidArgumentsException {
        if(element == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(NULL_ARG);

            String errorMessage = "OOOPS, something went wrong! Server received a null element";
            errorMessage += "\nElement expected: " + expected;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void extraEmpty(Player player) throws InvalidArgumentsException {
        if(!player.hasExtra()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Your extra resources slot is empty";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void notInExtra(Player player, ResourceType type) throws InvalidArgumentsException{
        if(!player.hasExtra(type)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The type selected is not present in your extra resources slot";
            errorMessage += "\nType selected: " + type;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void notSameResource(ResourceType first, ResourceType second, boolean ignoreNull) throws InvalidArgumentsException {

        if(ignoreNull)
            if(first == null || second == null)
                return;

        if(first != second) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The types does not match";
            errorMessage += "\nFirst Type: " + first;
            errorMessage += "\nSecond Type: " + second;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void resInDifferentLine(Warehouse wh, int line, Resource res) throws InvalidArgumentsException {
        if(wh.isPresent(line, res)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The depot already has this resource in another line";
            errorMessage += "\nSelected line: " + (line + 1);
            errorMessage += "\nSelected resource: " + res.getType();

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void depotFull(Warehouse wh, int line) throws InvalidArgumentsException {
        if(wh.isFull(line)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The selected depot line is already full";
            errorMessage += "\nSelected line: " + (line + 1);

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void depotEmpty(Warehouse wh, int line) throws InvalidArgumentsException {
        if(wh.isEmpty(line)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected depot line is empty";
            errorMessage += "\nSelected line: " + (line + 1);

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void notInHand(Player player, LeadCard card) throws InvalidArgumentsException {
        if(!player.hasLeader(card)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected card is not in you hand";
            errorMessage += "\nSelected card ID: " + card.getID();

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }
    public static void notInHand(Player player, AbilityType at) throws InvalidArgumentsException {
        if(!player.hasLeader(at)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! None of your cards meet the ability selected";
            errorMessage += "\nSelected ability: " + at;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }
    public static void notInHand(Player player, ResourceType rt) throws InvalidArgumentsException {
        if(!player.hasLeader(rt)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! None of your cards meet the resource selected";
            errorMessage += "\nSelected resource: " + rt;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }
    public static void notInHand(Player player, AbilityType at, ResourceType rt) throws InvalidArgumentsException {
        if(!player.hasLeader(at, rt)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! None of your cards meet the resource and ability";
            errorMessage += "\nSelected ability: " + at;
            errorMessage += "\nSelected resource: " + rt;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void inactiveCard(LeadCard card) throws InvalidArgumentsException {
        if(!card.isActive()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Your card is not active";
            errorMessage += "\nSelected card ID: " + card.getID();

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void cardDepotFull(LeadCard card) throws InvalidArgumentsException {
        if(((DepotAbility)card.getSpecialAbility()).isFull()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The depot of the card is already full";
            errorMessage += "\nSelected card ID: " + card.getID();

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void invalidResource(ResourceType selected, ResourceType invalid) throws InvalidArgumentsException{
        if(selected == invalid) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Received an invalid type for the resource";
            errorMessage += "\nType Received: " + selected;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void invalidAmount(int selected, int expected) throws InvalidArgumentsException {
        if(selected != expected) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected amount is invalid";
            errorMessage += "\nSelected amount: " + selected;
            errorMessage += "\nExpected amount: " + expected;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void invalidState(InstructionCode state, InstructionCode invalid) throws InvalidArgumentsException {
        if(state == invalid) {
            InvalidArgumentsException e = new InvalidArgumentsException(STATE_INVALID);

            String errorMessage = "OOOPS, something went wrong! Operation not allowed in current state";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void resourceRequirements(Player player, LeadCard card) throws InvalidArgumentsException {

        AtomicBoolean error = new AtomicBoolean(false);

        ArrayList<Resource> playerResources = player.getAllResources();
        ArrayList<FinalResource> resourceRequirements = player.getLeader(card).getResourceRequirements();

        if(resourceRequirements != null)
            resourceRequirements.forEach(resReq -> {

                //No match of type and amount
                if(playerResources.stream().noneMatch(pRes ->
                        (pRes.getType() == resReq.getType() && pRes.getAmount() >= resReq.getAmount())))
                    error.set(true);

            });

        if(error.get()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You don't have enough resources to activate this card";
            errorMessage += "\nSelected card ID: " + card.getID();
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static void cardRequirements(Player player, LeadCard card) throws InvalidArgumentsException {

        AtomicBoolean error = new AtomicBoolean(false);

        ArrayList<CardResource> playerDevCards = Lists.toCardResource(player.getAllDevCards());
        ArrayList<CardResource> cardRequirements = player.getLeader(card).getCardRequirements();

        cardRequirements.forEach(cReq -> {
            //If not 0, the lead card has a specific level requirement
            if(cReq.getLevel() != 0) {
                //No specific match of type and level
                if(playerDevCards.stream().noneMatch(pDev ->
                        (pDev.getType() == cReq.getType() && pDev.getLevel() == cReq.getLevel())))
                    error.set(true);
            }
            else {
                int typeAmount = playerDevCards.stream().filter(pDev -> pDev.getType() == cReq.getType())
                        .map(CardResource::getAmount).reduce(Integer::sum).get();

                if(typeAmount < cReq.getAmount())
                    error.set(true);
            }
        });

        if(error.get()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You don't have enough development cards to activate this card";
            errorMessage += "\nSelected card ID: " + card.getID();
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static void cardActive(Player player, LeadCard card) throws InvalidArgumentsException {

        if(player.getLeader(card).isActive()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The card seems to be active";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void hasPlayed(Player player) throws InvalidArgumentsException {
        if(player.hasPlayed) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! You can only perform one action per turn";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void hasSelected(boolean hasSelected) throws InvalidArgumentsException {

        if(hasSelected) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You have already selected something";
            e.setErrorMessage(errorMessage);

            throw e;
        }

    }

    public static void notSelected(boolean hasSelected) throws InvalidArgumentsException {

        if(!hasSelected) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! Nothing was selected";
            e.setErrorMessage(errorMessage);

            throw e;
        }

    }

    public static void noBlanks(Resource blanks) throws InvalidArgumentsException {

        if(blanks.getAmount() == 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! No blank resources to choose from";
            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void hasBlanks(Resource blanks) throws InvalidArgumentsException {

        if(blanks.getAmount() != 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! There are some blank resources left";
            errorMessage += "\nAmount left: " + blanks.getAmount();
            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    public static void blankOrFaith(Resource resource) throws InvalidArgumentsException {

        if(resource.getType() == BLANK || resource.getType() == FAITH) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! Selection contains a BLANK or FAITH resource";
            errorMessage += "\nSelected type: " + resource.getType();
            e.setErrorMessage(errorMessage);

            throw e;
        }

    }

}
