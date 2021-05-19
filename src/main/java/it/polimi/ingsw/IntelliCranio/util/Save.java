package it.polimi.ingsw.IntelliCranio.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.Game;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Save {

    public static final String pathPrefix = "src/main/resources/saved_games/";
    public static final String databasePath = "src/main/resources/";

    public static final Type netConfigType = new TypeToken<HashMap<String, String>>() {}.getType();
    public static final Type playerDatabaseType = new TypeToken<HashMap<String, UUID>>() {}.getType();

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
    public static Game loadGame(UUID gameUuid) {

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

    /**
     * Get the database of all players in different games.
     *
     * @return The hasmap string-uuid of all the nicknames connected to a specific game
     */
    public static <T> T getDatabase(String filename, Type tClass) {
        Gson gson = new Gson();
        String fileData = "";

        try {
            fileData = new String(Files.readAllBytes(Paths.get(databasePath + filename)));
        } catch (Exception e) {
            return null;
        }

        return gson.fromJson(fileData, tClass);
    }

    /**
     * Save the updated database in its file.
     * @param database The hashmap to store in a file.
     */
    public static void saveDatabase(HashMap<Object, Object> database, String filename) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            FileWriter out = new FileWriter(databasePath + filename);

            out.write(gson.toJson(database));
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Remove the file of the game from disk.
     * Remove every player in database provided its game uuid value
     * <p>
     *     WARNING! Deleted data can't be restored.
     * </p>
     *
     * @param uuid The uuid to remove
     */
    public static void deleteGameData(UUID uuid, ConcurrentHashMap<String, UUID> nickname_uuid) {

        //Delete File from disk
        try { new File(Save.pathPrefix + uuid.toString()).delete(); }
        catch (Exception e) {}

        saveDatabase(new HashMap<>(nickname_uuid), "database.json");
    }

}
