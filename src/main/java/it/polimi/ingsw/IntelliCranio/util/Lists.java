package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static <T> ArrayList<T> toList(T var) {
        ArrayList<T> result = new ArrayList<T>();
        result.add(var);
        return result;
    }

    public static <T> ArrayList<Object> toObjectList(ArrayList<T> arr) {
        ArrayList<Object> result = new ArrayList<>();
        arr.forEach( x -> result.add(x));
        return result;
    }

    public static <T> ArrayList<Object> toObjectList(String[] arr) {
        ArrayList<Object> result = new ArrayList<>();
        Arrays.stream(arr).forEach( x -> result.add(x));
        return result;
    }

    public static <T> ArrayList<Object> toObjectList(String var) {
        ArrayList<Object> result = new ArrayList<>();
        result.add(var);
        return result;
    }
}
