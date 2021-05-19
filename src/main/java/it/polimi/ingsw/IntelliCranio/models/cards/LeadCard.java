package it.polimi.ingsw.IntelliCranio.models.cards;

import it.polimi.ingsw.IntelliCranio.models.ability.Ability;
import it.polimi.ingsw.IntelliCranio.models.ability.Ability.AbilityType;
import it.polimi.ingsw.IntelliCranio.models.ability.AbilityFactory;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;

import java.util.ArrayList;

public class LeadCard extends Card{

    private boolean isActive;
    private ArrayList<CardResource> cardRequirements;
    private ArrayList<FinalResource> resourceRequirements;

    private AbilityType abilityType;
    private ResourceType resourceType;
    private Ability specialAbility;

    public LeadCard(String ID, int vp, ArrayList<CardResource> cardRequirements,
                    ArrayList<FinalResource> resourceRequirements, AbilityType at, ResourceType rt, boolean isActive) {
        this.ID = ID;
        this.vp = vp;
        this.cardRequirements = cardRequirements;
        this.resourceRequirements = resourceRequirements;
        this.abilityType = at;
        this.resourceType = rt;
        this.isActive = isActive;
    }


    /**
     * Creates a specific ability using a factory from ability and resource types of the card
     */
    public void setupAbility() {
        specialAbility = new AbilityFactory().getAbility(abilityType, resourceType);
    }

    //region Getters

    public boolean isActive() {
        return isActive;
    }

    public String getID() { return ID; }

    public Ability.AbilityType getAbilityType() {
        return abilityType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Ability getSpecialAbility() {
        return specialAbility;
    }


    public ArrayList<CardResource> getCardRequirements() {
        return cardRequirements;
    }

    public ArrayList<FinalResource> getResourceRequirements() {
        return resourceRequirements;
    }

    //endregion

    public void activateCard() {
        isActive = true;
    }
    
    /**
     * Creates a single string of the card from its fields
     * @return A string with all the data of the card
     */
    @Override
    public String toString() {
        return "LeadCard {" +
                "vp=" + vp +
                ",\tactive=" + isActive +
                ",\tability: {" + abilityType +
                ", " + resourceType + "}, " +
                ((cardRequirements != null) ? "\tcard requirements: [" + cardRequirements.stream().map(CardResource::toString).reduce("", (x,y) -> x + " " + y) + " ]" : "") +
                ((resourceRequirements != null) ? "\tresource requirements: [" + resourceRequirements.stream().map(FinalResource::toString).reduce("", (x, y) -> x + " " + y) + " ]" : "") +
                " }";
    }
}
