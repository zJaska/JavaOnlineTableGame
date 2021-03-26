package it.polimi.ingsw.IntelliCranio.server.cards;

import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.util.ArrayList;

public class LeadCard extends Card{

    private boolean isActive;
    private ArrayList<FinalResource> requirements;
    private Ability specialAbility;


    public boolean isActive() {
        return isActive;
    }

    public ArrayList<FinalResource> getRequirements() {
        return requirements;
    }

    public void useCard() {
        throw new UnsupportedOperationException();
    }
}
