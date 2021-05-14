package it.polimi.ingsw.IntelliCranio.models;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.controllers.GameManager;
import it.polimi.ingsw.IntelliCranio.models.cards.PopeCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

import static it.polimi.ingsw.IntelliCranio.models.cards.PopeCard.Status.*;

public class FaithTrack implements Serializable {

    private int length;
    private TreeMap<Integer, Integer> vpTiles;
    private ArrayList<Integer> popeSpaces;
    private ArrayList<Integer> startOfVaticanSections;

    /**
     * Initializes the FaithTrack object from a json file
     *
     * @param filePath The path of the file that contains the configuration of the FaithTrack
     */

    public FaithTrack(String filePath) {
        try {
            //Get the json file as String
            String text = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            //Generate the list of cards

            FaithTrack temp = gson.fromJson(text, FaithTrack.class);
            vpTiles = temp.vpTiles;
            length = temp.length;
            popeSpaces = temp.popeSpaces;
            startOfVaticanSections = temp.startOfVaticanSections;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkStatus(ArrayList<Player> players, Game game) {

        //Check if: pope space
        players.forEach(player -> {
            int section = popeSpace(player.getFaithPosition()); //Get the section from player position

            if(section != -1) {
                //The player reached a pope space

                //Update players pope card status
                players.forEach(player1 -> {
                    PopeCard popeCard = player1.getPopeCard(section); //Get the pope card of this section

                    if(popeCard != null)
                        //Set the status based on player current position
                        if(isXSectionOrHigher(section, player1.getFaithPosition()))
                            player1.getPopeCard(section).setStatus(ACTIVE);
                        else
                            player1.getPopeCard(section).setStatus(REMOVED);
                });

                //Check for game ending
                if(player.getFaithPosition() == length)
                    game.endGame(true);
            }
        });

    }

    public int getTrackLength() {
        return length;
    }

    /**
     * @param space The number of the cell
     * @return The number of the vatican section the pope space is in, otherwise -1 if the cell isn't a pope space
     */

    public int popeSpace(int space) {
        return popeSpaces.indexOf(space);
    }

    /**
     * This method will be used when player reaches a pope space, and other players
     * will have to set the state of a pope card based on the section they're in.
     *
     * @param sectionX The vatican section starting from 0
     * @param space    The number of the cell to be checked
     * @return The number of the last section that the player has passed, or -1 if he hasn't still passed a section
     */

    public boolean isXSectionOrHigher(int sectionX, int space) {
        if (sectionX < 0 || sectionX >= startOfVaticanSections.size())
            return false;
        return startOfVaticanSections.get(sectionX) <= space;
    }

    public int getVp(int space) {
        return vpTiles.get(space);
    }


    // View purposes only

    public ArrayList<Integer> getPopeSpacesCopy() {
        ArrayList<Integer> copy = new ArrayList<>();
        popeSpaces.forEach(x -> copy.add(Integer.valueOf(x)));
        return copy;
    }

    public ArrayList<Integer> getStartOfVaticanSectionsCopy() {
        ArrayList<Integer> copy = new ArrayList<>();
        startOfVaticanSections.forEach(x -> copy.add(Integer.valueOf(x)));
        return copy;
    }


}
