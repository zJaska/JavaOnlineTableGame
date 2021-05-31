package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.cards.PopeCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.models.ability.Ability;
import it.polimi.ingsw.IntelliCranio.models.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.util.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.models.ability.Ability.AbilityType.DEPOT;

public class Player implements Serializable {

    private String nickname;
    private int faithPosition;

    private Warehouse warehouse;
    private Strongbox strongbox;

    //not null
    private ArrayList<Resource> extraRes;

    private ArrayList<DevCard>[] devSlots;

    private ArrayList<LeadCard> leaders;
    private ArrayList<PopeCard> popeCards;

    public boolean hasPlayed;

    private InstructionCode lastAction;

    public Player(String nickname, int devSlotsAmount, int sectionsAmount) {

        this.nickname = nickname;
        faithPosition = 0;
        warehouse = new Warehouse(3);
        strongbox = new Strongbox();
        extraRes = new ArrayList<>();

        devSlots = new ArrayList[devSlotsAmount];
        for (int i = 0; i < devSlotsAmount; i++)
            devSlots[i] = new ArrayList<>();

        popeCards = new ArrayList<>();
        for (int i = 0; i < sectionsAmount; ++i)
            popeCards.add(new PopeCard());

        hasPlayed = false;
        lastAction = InstructionCode.DISCARD_INIT_LEAD;
    }

    //region Getters
    public InstructionCode getLastAction() {
        return lastAction;
    }

    public String getNickname() {
        return nickname;
    }

    public ArrayList<LeadCard> getLeaders() {
        return leaders;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public ArrayList<Resource> getExtraRes() {
        return extraRes;
    }

    public int getFaithPosition() {
        return faithPosition;
    }

    /**
     * Return the pope card given its section in faith track
     *
     * @param section the section in faith track
     * @return The pope card of that section, or null if error
     */
    public PopeCard getPopeCard(int section) {
        if (!(section < 0 || section >= popeCards.size()))
            return popeCards.get(section);
        else
            return null;
    }

    public ArrayList<PopeCard> getPopeCards() {
        return popeCards;
    }

    /**
     * @return All the dev cards bought by player
     */
    public ArrayList<DevCard> getAllDevCards() {
        ArrayList<DevCard> temp = new ArrayList<>();

        Arrays.stream(devSlots).filter(Objects::nonNull).forEach(temp::addAll);

        return temp;
    }

    public ArrayList<DevCard>[] getDevCardsCopy() {
        ArrayList<DevCard>[] local = new ArrayList[3];
        for (int i=0; i<devSlots.length; i++) {
            if (devSlots[i] == null)
                local[i] = null;
            else {
                local[i] = new ArrayList<>();
                for (DevCard card : devSlots[i])
                    local[i].add(card.getCopy());
            }
        }

        return local;
    }

    /**
     * @return The top dev card of each slot
     */
    public DevCard[] getFirstDevCards() {
        DevCard[] cards = new DevCard[devSlots.length];
        int i = 0;
        for (ArrayList<DevCard> devSlot : devSlots)
            cards[i++] = (devSlot.size() > 0) ? devSlot.get(0) : null;
        return cards;
    }

    //endregion


    public void setLastAction(InstructionCode action) {
        lastAction = action;
    }

    public void setLeaders(ArrayList<LeadCard> cards) {
        leaders = cards;
    }

    /**
     * Add the specified amount of selected type.
     * A new resource is added to the list, the list is then unified
     *
     * @param rt     Type to add
     * @param amount Amount to add
     */
    public void addExtra(FinalResource.ResourceType rt, int amount) {
        extraRes.add(new Resource(rt, amount));
        extraRes = Lists.unifyResourceAmounts(extraRes);
    }

    /**
     * Try remove the specified amount from selected type of resource.
     * <p>
     * If amount is bigger than actual resource amount, the difference is IGNORED
     * and the resource gets completely removed from list.
     * Resource gets removed if amount reaches 0
     * <p>
     * If there is no such type "rt" in the list, this method does nothing
     * </p>
     * </p>
     *
     * @param rt     Type of resource to remove
     * @param amount Amount to remove from resource
     */
    public void removeExtra(FinalResource.ResourceType rt, int amount) {
        if (hasExtra(rt)) {
            Resource temp = getExtra(rt);

            if (temp.removeAmount(amount) == -1) //Selected amount is bigger than actual amount
                extraRes.remove(temp); //Remove the resource
            else if (temp.getAmount() == 0) //Resource is now empty
                extraRes.remove(temp); //Remove the resource
        }

    }

    /**
     * Increment by 1 the position in the faithtrack
     */
    public void addFaith() {
        faithPosition++;
    }

    /**
     * Add a new card at the top of selected slot
     *
     * @param card The card to place
     * @param slot The selected slot
     */
    public void addDevCard(DevCard card, int slot) {
        devSlots[slot].add(0, card);
    }


    //region Utility methods

    //region hasLeader

    /**
     * The player has the selected card, matched by ID
     *
     * @param card The card to look for
     * @return True if player has the card, false otherwise
     */
    public boolean hasLeader(LeadCard card) {
        return leaders.stream().anyMatch(lead -> lead.getID().equals(card.getID()));
    }

    /**
     * The player has at least one card of selected ability type
     *
     * @param at The ability type to look for
     * @return True if player has any card, false otherwise
     */
    public boolean hasLeader(Ability.AbilityType at) {
        if (leaders == null)
            return false;
        return leaders.stream().anyMatch(lead -> lead.getAbilityType() == at);
    }

    /**
     * The player has at least one card of selected resource type
     *
     * @param rt The resource type to look for
     * @return True if player has any card, false otherwise
     */
    public boolean hasLeader(FinalResource.ResourceType rt) {
        if (leaders == null)
            return false;
        return leaders.stream().anyMatch(lead -> lead.getResourceType() == rt);
    }

    /**
     * The player has the selected card, matched by both ability and resource type
     *
     * @param at The ability type to look for
     * @param rt The resource type to look for
     * @return True if player has the card, false otherwise
     */
    public boolean hasLeader(Ability.AbilityType at, FinalResource.ResourceType rt) {
        if (leaders == null)
            return false;
        return leaders.stream()
                .anyMatch(lead -> (lead.getAbilityType() == at && lead.getResourceType() == rt));
    }
    //endregion

    //region getLeader

    /**
     * Get a card stored on server providing a matching object.
     *
     * @param card The card to get as original
     * @return The server card matched by ID, null if player doesn't have card
     */
    public LeadCard getLeader(LeadCard card) {
        if (!hasLeader(card))
            return null;

        return leaders.stream().filter(lead -> lead.getID().equals(card.getID())).findFirst().get();
    }

    /**
     * Return a specific card given its ability and resource type.
     * Better used after "hasLeader" has been called,
     * if there is no such card, return null.
     *
     * @param at The ability type to check
     * @param rt The resource type to check
     * @return The card matching both ability and resource type if present, null otherwise
     */
    public LeadCard getLeader(Ability.AbilityType at, FinalResource.ResourceType rt) {
        if (!hasLeader(at, rt)) //Double check to prevent exception throwing
            return null;

        return leaders.stream()
                .filter(lead -> (lead.getAbilityType() == at && lead.getResourceType() == rt))
                .findFirst().get();
    }

    //endregion

    /**
     * Remove a leader matched by id
     *
     * @param card The card to remove by ID
     */
    public void removeLeader(LeadCard card) {
        if (leaders == null)
            return;
        leaders.removeIf(lead -> lead.getID().equals(card.getID()));
    }

    /**
     * Check if player extra resources is empty or not
     *
     * @return True if there is at least 1 element in the list, false otherwise
     */
    public boolean hasExtra() {
        return extraRes.size() > 0;
    }

    /**
     * Check if selected resource type is present in extra resources
     *
     * @param rt The type to check
     * @return True if a resource of type rt is in the list, false otherwise
     */
    public boolean hasExtra(FinalResource.ResourceType rt) {
        if (extraRes == null)
            return false;
        return extraRes.stream().anyMatch(res -> res.getType() == rt);
    }

    /**
     * Try get a resource from extra resources given a specific type.
     * Better if called after "hasExtra"
     *
     * @param rt The type to get from list
     * @return The resource that match the type if present, null otherwise
     */
    public Resource getExtra(FinalResource.ResourceType rt) {
        if (hasExtra(rt))
            return extraRes.stream().filter(res -> res.getType() == rt).findFirst().get();
        else
            return null;
    }

    /**
     * @return The amount of resources in extra resources list, 0 if empty
     */
    public int extraAmount() {
        if (!hasExtra())
            return 0;

        return extraRes.stream().map(Resource::getAmount).reduce(Integer::sum).get();
    }

    /**
     * Set extraRes with a new ArrayList
     */
    public void resetExtra() {
        extraRes = new ArrayList<>();
    }

    /**
     * Get all the resources of the player from Warehouse, Strongbox and Depot Leader Cards
     * @return A list with all the resources of the player
     */
    public ArrayList<Resource> getAllResources() {

        ArrayList<Resource> temp = new ArrayList<>();

        //Warehouse
        temp.addAll(warehouse.getAllResources());

        //Strongbox
        temp.addAll(strongbox.getAllResources());

        //Leaders
        temp.addAll(leaders.stream()
                .filter(lead -> lead.getAbilityType() == DEPOT)
                .map(LeadCard::getSpecialAbility)
                .map(ability -> (DepotAbility) ability)
                .map(DepotAbility::getDepot)
                .collect(Collectors.toList()));

        return Lists.unifyResourceAmounts(temp);
    }

    //endregion
}
