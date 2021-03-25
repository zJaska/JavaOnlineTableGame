package it.polimi.ingsw.IntelliCranio.server;

import java.util.ArrayList;
import java.util.Dictionary;

public class FaithTrack {

    private final int length;
    private final Dictionary<Integer, Integer> vpTiles;
    private final ArrayList<Integer> popeSpaces;
    private final ArrayList<Integer> vaticanSections;

    public FaithTrack(int length, Dictionary<Integer, Integer> vpTiles,
                      ArrayList<Integer> popeSpaces, ArrayList<Integer> vaticanSections) {

        this.length = length;
        this. vpTiles = vpTiles;
        this.popeSpaces = popeSpaces;
        this.vaticanSections = vaticanSections;
    }

    public int getTrackLength() {
        return length;
    }

    //Detect if a player is in a pope space and return the vatican section index
    public int popeSpace(int space) {
        throw new UnsupportedOperationException();
    }

    public boolean isXSectionOrHigher(int sectionX, int space) {
        throw new UnsupportedOperationException();
    }

    public int getVp(int space) {
        throw new UnsupportedOperationException();
    }

}
