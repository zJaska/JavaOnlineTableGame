package it.polimi.ingsw.IntelliCranio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CardGenerator {

    public static void generateLeadCardFile(ArrayList<LeadCard> cards) throws IOException {
        FileWriter cardFile = new FileWriter("src/main/resources/leadcards_config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        cardFile.write(gson.toJson(cards));
        cardFile.close();
    }

    public static void generateDevCardFile(ArrayList<DevCard>[][] grid) throws IOException {
        FileWriter cardFile = new FileWriter("src/main/resources/devcards_config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ArrayList<DevCard> devs = new ArrayList<>();

        for(int row = 2; row >=0; --row)
            for(int col = 0; col < 4; ++col)
                for(int i = 0; i < 4; ++i)
                    devs.add(grid[row][col].get(i));

        // Semplificazione matematica del ciclo for
        //for (int i=0; i<48; ++i)
        //    devs.add(grid[i/16][(i/4)%4].get(i%4));

        cardFile.write(gson.toJson(devs));
        cardFile.close();
    }

}
