package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.models.Game;

import java.io.*;
import java.util.UUID;

public class Save {

    private static final String pathPrefix = "src/main/resources/saved_games/";

    /**
     * Serialize the game to file using unique IDs.
     * If file does not exists, is created and written.
     *
     * @param game The game to save
     */
    public static void saveGame(Game game) {

        //Path from game unique ID
        String path = pathPrefix + game.getUuid().toString();

        File file = new File(path);

        try {

            file.createNewFile(); //Try create a new file. Does nothing if file already exists

            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            objOut.writeObject(game); //Serialize the object
            objOut.close();
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new Game from file data.
     * Requires the file to already exist and must be already initialized.
     *
     * @param gameUuid Unique ID of the game to load
     * @return A new instance of Game, the deserialized object from file
     */
    public static Game loadSave(UUID gameUuid) {

        //Path from game unique ID
        String path = pathPrefix + gameUuid.toString();

        File file = new File(path);

        try {

            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            Game game = (Game) objIn.readObject(); //Deserialize the game from file

            fileIn.close();
            objIn.close();

            return game;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
