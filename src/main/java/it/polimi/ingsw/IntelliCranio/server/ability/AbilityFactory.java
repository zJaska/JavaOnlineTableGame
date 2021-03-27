package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

public class AbilityFactory {

    public Ability getAbility(LeadCard.AbilityType at, FinalResource.ResourceType rt) {
        if(at == null) return null;
        if(at == LeadCard.AbilityType.SALE) return new SaleAbility(rt);
        if(at == LeadCard.AbilityType.DEPOT) return new DepotAbility(rt);
        if(at == LeadCard.AbilityType.RESOURCE) return new ResourceAbility(rt);
        if(at == LeadCard.AbilityType.PRODUCTION) return new ProductionAbility(rt);
        return null;
    }
}
