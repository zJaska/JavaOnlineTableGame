package it.polimi.ingsw.IntelliCranio.server.cards;

import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.ability.AbilityFactory;
import it.polimi.ingsw.IntelliCranio.server.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.util.ArrayList;

public class LeadCard extends Card{

    public enum AbilityType{SALE, DEPOT, RESOURCE, PRODUCTION}

    private boolean isActive;
    private ArrayList<CardResource> cardRequirements;
    private ArrayList<FinalResource> resourceRequirements;

    private AbilityType abilityType;
    private FinalResource.ResourceType resourceType;
    private Ability specialAbility;

    public LeadCard(String ID, int vp, ArrayList<CardResource> cardRequirements,
                    ArrayList<FinalResource> resourceRequirements, AbilityType at, FinalResource.ResourceType rt, boolean isActive) {
        this.ID = ID;
        this.vp = vp;
        this.cardRequirements = cardRequirements;
        this.resourceRequirements = resourceRequirements;
        this.abilityType = at;
        this.resourceType = rt;
        this.isActive = isActive;
    }


    public void setupAbility(AbilityType at, FinalResource.ResourceType rt) {
        specialAbility = new AbilityFactory().getAbility(at, rt);
    }

    public boolean isActive() {
        return isActive;
    }


    public AbilityType getAbilityType() {
        return abilityType;
    }

    public FinalResource.ResourceType getResourceType() {
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

    public void useCard() {
        throw new UnsupportedOperationException();
    }
}
