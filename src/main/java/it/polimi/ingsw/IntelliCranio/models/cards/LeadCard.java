package it.polimi.ingsw.IntelliCranio.models.cards;

import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType;
import it.polimi.ingsw.IntelliCranio.server.ability.AbilityFactory;
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


    public void setupAbility() {
        specialAbility = new AbilityFactory().getAbility(abilityType, resourceType);
    }

    public boolean isActive() {
        return isActive;
    }


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

    public void activateCard() {
        isActive = true;
    }
}
