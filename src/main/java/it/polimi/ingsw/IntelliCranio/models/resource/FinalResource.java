package it.polimi.ingsw.IntelliCranio.models.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;

public class FinalResource implements Serializable {

    public enum ResourceType {STONE, SHIELD, SERVANT, COIN, FAITH, BLANK}
    public static final ArrayList<ResourceType> EXCLUDED = new ArrayList<>(Arrays.asList(FAITH, BLANK));

    protected final ResourceType type;
    protected int amount;

    public FinalResource(ResourceType type, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException();
        this.type = type;
        this.amount = amount;
    }

    public ResourceType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "{" + getType().toString().substring(0,2) + "," + getAmount() + "}";
    }
}
