package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.controllers.GameManager;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public class Lists {

    /**
     * Takes a List of resources with repeated type and return a new list with a single type for
     * each element in original list with its multiplicity.
     *
     * @param list
     * @param <T> A generic of type FinalResource or Resource
     * @return A new list with only distinct types.
     */
    public static <T extends FinalResource> ArrayList<T> unifyResourceAmounts(List<T> list) {
        ArrayList<T> result = new ArrayList<>();

        if (list == null)
            return result;

        list.stream().filter(x -> x != null).map(FinalResource::getType).distinct().forEach(resType -> {
            Resource temp = new Resource(resType,
                    list.stream().filter(res -> res != null && res.getType() == resType)
                            .map(FinalResource::getAmount).reduce(Integer::sum).get());
            result.add((T)temp);
        });

        return result;
    }

    public static ArrayList<CardResource> toCardResource(ArrayList<DevCard> cards) {

        ArrayList<CardResource> temp = new ArrayList<>();

        cards.forEach(devC -> {
            //If a card of same type and level is already present, update its value
            if(temp.stream().anyMatch(cardRes ->
                    (cardRes.getType() == devC.getType() && cardRes.getLevel() == devC.getLevel())))
                temp.stream()
                        .filter(cardRes -> (cardRes.getType() == devC.getType() &&
                                        cardRes.getLevel() == devC.getLevel()))
                        .findFirst().get().addAmount(1);
            else
                //Add a new resource with that type and level
                temp.add(new CardResource(devC.getType(), 1, devC.getLevel()));
        });

        return temp;
    }
}
