package it.polimi.ingsw.IntelliCranio;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static <T> ArrayList<T> toList(T[] arr) {
        ArrayList<T> result = new ArrayList<T>();
        Arrays.stream(arr).forEach(x -> { result.add(x); });
        return result;
    }
}
