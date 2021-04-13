package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType;


public class AbilityFactory {

    public Ability getAbility(AbilityType at, FinalResource.ResourceType rt) {
        if(at == null) return null;
        if(at == AbilityType.SALE) return new SaleAbility(rt);
        if(at == AbilityType.DEPOT) return new DepotAbility(rt);
        if(at == AbilityType.RESOURCE) return new ResourceAbility(rt);
        if(at == AbilityType.PRODUCTION) return new ProductionAbility(rt);
        return null;
    }
}
