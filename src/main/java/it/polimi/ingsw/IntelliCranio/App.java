package it.polimi.ingsw.IntelliCranio;

import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.player.Player;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        GameManager manager = new GameManager();
        manager.addPlayer(new Player(), 0);
        manager.addPlayer(new Player(), 1);

/*
        try {
            CardGenerator.generateLeadCardFile(manager.generateCards());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        manager.createLeaderCards("src/main/resources/leadcards_config.json", true);
    }
}
