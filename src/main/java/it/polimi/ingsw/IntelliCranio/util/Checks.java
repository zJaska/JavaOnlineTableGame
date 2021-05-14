package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.player.Strongbox;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.BLANK;
import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.FAITH;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_INIT_RES;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DEFAULT;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType.DEPOT;

public class Checks {

    public static void packetCheck(Packet packet) throws InvalidArgumentsException {
        if(packet == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(PACKET_NULL);
            String errorMessage = "OOOPS, something went wrong! Server received no packet";
            e.setErrorMessage(errorMessage);
            throw e;
        }

        if(packet.getInstructionCode() == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(CODE_NULL);
            String errorMessage = "OOOPS, something went wrong! Server received no code";
            e.setErrorMessage(errorMessage);
            throw e;
        }

        if(packet.getArgs() == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(ARGS_NULL);
            String errorMessage = "OOOPS, something went wrong! Server received null args";
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

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

    public static void negativeValue(int value) throws InvalidArgumentsException {
        if(value < 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! One or more values selected are negative";

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    /**
     * Used for single values checks
     * @param value
     * @param max
     * @throws InvalidArgumentsException
     */
    public static void overMaxValue(int value, int max) throws InvalidArgumentsException {
        if(value > max) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! One or more values selected exceed maximum size";
            errorMessage += "\nSelected value: " + value;
            errorMessage += "\nMax value: " + max;

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    /**
     * Used for depots and arrays checks
     * @param line
     * @param size
     * @throws InvalidArgumentsException
     */
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

    /**
     *
     * @param card The SERVER copy of provided card
     * @param at
     * @throws InvalidArgumentsException
     */
    public static void invalidAbility(LeadCard card, AbilityType at) throws InvalidArgumentsException {

        if(card.getAbilityType() != at) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! Selected card doesn't have a valid ability";
            errorMessage += "\nExpected ability: " + at;

            e.setErrorMessage(errorMessage);
            throw e;
        }

    }

    /**
     *
     * @param serverCard The SERVER copy of given card
     * @throws InvalidArgumentsException
     */
    public static void cardInactive(LeadCard serverCard) throws InvalidArgumentsException {
        if(!serverCard.isActive()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Your card is not active";
            errorMessage += "\nSelected card ID: " + serverCard.getID();

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    /**
     *
     * @param serverCard The SERVER copy of given card
     * @throws InvalidArgumentsException
     */
    public static void cardDepotFull(LeadCard serverCard) throws InvalidArgumentsException {
        if(((DepotAbility)serverCard.getSpecialAbility()).isFull()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The depot of the card is already full";
            errorMessage += "\nSelected card ID: " + serverCard.getID();

            e.setErrorMessage(errorMessage);

            throw e;
        }
    }

    /**
     *
     * @param serverCard The SERVER copy of given card
     * @throws InvalidArgumentsException
     */
    public static void cardDepotEmpty(LeadCard serverCard) throws InvalidArgumentsException {

        if(((DepotAbility)serverCard.getSpecialAbility()).isEmpty()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! Selected card has empty depot";
            errorMessage += "\nSelected card ID: " + serverCard.getID();
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
                int typeAmount;

                try {
                    typeAmount = playerDevCards.stream()
                            .filter(pDev -> pDev.getType() == cReq.getType())
                            .map(CardResource::getAmount).reduce(Integer::sum).get();
                } catch (NoSuchElementException e) {
                    typeAmount = 0;
                }

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

    /**
     *
     * @param serverCard The SERVER copy of given card
     * @throws InvalidArgumentsException
     */
    public static void cardActive(LeadCard serverCard) throws InvalidArgumentsException {

        if(serverCard.isActive()) {
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

    /**
     * If slot == 0, don't throw (base is always possible)
     * @param player
     * @param slot
     */
    public static void slotEmpty(Player player, int slot) throws InvalidArgumentsException {

        if(slot == 0)
            return;

        if(player.getFirstDevCards()[slot - 1] == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! There are no cards in selected slot";
            errorMessage += "\nSelected slot: " + slot;
            e.setErrorMessage(errorMessage);

            throw e;
        }

    }

    public static void strongboxEmpty(Strongbox sb, ResourceType rt) throws InvalidArgumentsException {

        if(sb.getAmount(rt) == 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! Selected resource is empty in strongbox";
            errorMessage += "\nSelected resource: " + rt;
            e.setErrorMessage(errorMessage);
            throw e;
        }

    }

    public static <T extends FinalResource> void invalidProdCostResources(ArrayList<T> actual, ArrayList<T> expected) throws InvalidArgumentsException {

        AtomicBoolean error = new AtomicBoolean(false);

        expected.forEach(ex -> {
            if(actual.stream().noneMatch(ac -> (ac.getType() == ex.getType() && ac.getAmount() < ex.getAmount())))
                error.set(true);
        });

        if(error.get()){
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You don't have enough resources selected";
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static <T extends FinalResource> void invalidCardCostResources(ArrayList<T> actual, ArrayList<T> expected) throws InvalidArgumentsException {

        AtomicBoolean error = new AtomicBoolean(false);

        expected.forEach(ex -> {
            if(actual.stream().noneMatch(ac -> (ac.getType() == ex.getType() && ac.getAmount() != ex.getAmount())))
                error.set(true);
        });

        if(error.get()){
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You have selected an invalid amount of resources";
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static void cardMarketEmpty(CardMarket cm, int row, int col) throws InvalidArgumentsException {

        if(cm.getCard(row, col) == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! There are no cards left in this slot";
            errorMessage += "\nSelected row: " + (row + 1);
            errorMessage += "\nSelected column: " + (col + 1);
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static void invalidLevel(Player player, DevCard card) throws InvalidArgumentsException {

        DevCard[] topCards = player.getFirstDevCards();
        AtomicBoolean error = new AtomicBoolean(false);

        if(card.getLevel() == 1) {
            if(Arrays.stream(topCards).noneMatch(Objects::isNull))
                error.set(true);

            if(error.get()) {
                InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
                String errorMessage = "OOOPs, something went wrong! You need an empty slot to put this card";
                errorMessage += "\nSelected card level: " + card.getLevel();
                e.setErrorMessage(errorMessage);
                throw e;
            }
        }else {
            if(Arrays.stream(topCards).filter(Objects::nonNull).noneMatch(topC -> topC.getLevel() == card.getLevel() - 1))
                error.set(true);

            if(error.get()) {
                InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
                String errorMessage = "OOOPS, something went wrong! You don't have a card a level below selected one";
                errorMessage += "\nSelected card level: " + card.getLevel();
                e.setErrorMessage(errorMessage);
                throw e;
            }
        }




    }

    public static void alreadyConfirmed(boolean confirmed) throws InvalidArgumentsException {
        if(confirmed) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You have confirmed the card, please select a slot";
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static void notConfirmed(boolean confirmed) throws InvalidArgumentsException {
        if(!confirmed) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You have to confirm before selecting a slot";
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static void cardSelected(DevCard card) throws InvalidArgumentsException {

        if(card != null) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! A card is already selected";
            errorMessage += "\nSelected card ID: " + card.getID();
            e.setErrorMessage(errorMessage);
            throw e;
        }

    }

    public static void cardNotSelected(DevCard card) throws InvalidArgumentsException {
        if(card == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You need to select a card first";
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }

    public static void invalidSlot(DevCard slotCard, DevCard selected) throws InvalidArgumentsException {

        if(selected.getLevel() == 1)
            if(slotCard != null) {
                InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
                String errorMessage = "OOOPS, something went wrong! Can't add selected card in this slot";
                e.setErrorMessage(errorMessage);
                throw e;
            }

        if(slotCard.getLevel() != selected.getLevel() - 1) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! Can't add selected card in this slot";
            e.setErrorMessage(errorMessage);
            throw e;
        }
    }
}
