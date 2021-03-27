package it.polimi.ingsw.IntelliCranio.server.cards;

import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.util.ArrayList;

public class LeadCard extends Card{

    private boolean isActive;
    private ArrayList<CardResource> cardRequirements;
    private ArrayList<FinalResource> resourceRequirements;

    public Ability getSpecialAbility() {
        return specialAbility;
    }

    private Ability specialAbility;

    public LeadCard(String ID, int vp,ArrayList<CardResource> cardRequirements,
                    ArrayList<FinalResource> resourceRequirements, Ability specialAbility, boolean isActive) {
        this.ID = ID;
        this.vp = vp;
        this.cardRequirements = cardRequirements;
        this.resourceRequirements = resourceRequirements;
        this.specialAbility = specialAbility;
        this.isActive = isActive;
    }


    public boolean isActive() {
        return isActive;
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
