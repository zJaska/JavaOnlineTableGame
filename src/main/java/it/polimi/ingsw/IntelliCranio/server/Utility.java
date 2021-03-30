package it.polimi.ingsw.IntelliCranio.server;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utility {

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

        list.stream().map(FinalResource::getType).distinct().forEach(resType -> {
            Resource temp = new Resource(resType, list.stream().filter(res -> res.getType() == resType).map(FinalResource::getAmount).reduce(Integer::sum).get());
            result.add((T)temp);
        });

        return result;
    }

}
