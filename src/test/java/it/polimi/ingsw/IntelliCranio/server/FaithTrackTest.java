package it.polimi.ingsw.IntelliCranio.server;

import it.polimi.ingsw.IntelliCranio.models.FaithTrack;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {

    @Test
    public void isXSectionOrHigher() {
        FaithTrack faith = new FaithTrack("src/main/resources/faithtrack_config.json");
        assertFalse(faith.isXSectionOrHigher(0,4));
        assertTrue(faith.isXSectionOrHigher(0,5));
        assertTrue(faith.isXSectionOrHigher(0,8));
        assertTrue(faith.isXSectionOrHigher(1,12));
        assertTrue(faith.isXSectionOrHigher(1,15));
        assertTrue(faith.isXSectionOrHigher(2,19));
        assertFalse(faith.isXSectionOrHigher(-1,-1));
        assertFalse(faith.isXSectionOrHigher(-2,6));
        assertFalse(faith.isXSectionOrHigher(-23,-6));
    }

    //Controlla posizione e attiva varie cose
}