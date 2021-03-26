package it.polimi.ingsw.IntelliCranio.server.market;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class CardMarket {

    private ArrayList<DevCard>[][] marketGrid = new ArrayList[3][4];


    /**
     * <summary>
     *     Creates all the development cards and place them on the market grid.
     *     Each group is then shuffled.
     * </summary>
     */
    public void setup() {
        //Creating the list for each group
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 4; ++col)
                marketGrid[row][col] = new ArrayList<>();

        generateGrid();
        shuffle();
    }

    /**
     * <summary>
     *     Generates the grid of cards
     * </summary>
     */
    private void generateGrid() {
        ArrayList<DevCard> cardList = new ArrayList<>();

        try {
            String text = new String(Files.readAllBytes(Paths.get("src/main/resources/devcards_config.json")), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            cardList = gson.fromJson(text, new TypeToken<ArrayList<DevCard>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardList.forEach(card -> {
            DevCard temp = (DevCard) card;
            int col = 0;
            switch (temp.getType()) {
                case GREEN:
                    col = 0;
                    break;
                case BLUE:
                    col = 1;
                    break;
                case YELLOW:
                    col = 2;
                    break;
                case PURPLE:
                    col = 3;
                    break;
            }
            marketGrid[3 - card.getLevel()][col].add(card);
        });

    }


    /**
     * <summary>
     *     Shuffle each group of cards in the grid
     * </summary>
     */
    private void shuffle() {

        ArrayList<DevCard> temp = new ArrayList<>();

        int index;
        Random random = new Random();

        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 4; ++col) {
                for(int i = 0; i < 4; ++i) {
                    //Get a random number between range
                    index = random.ints(0, 4).findFirst().getAsInt();

                    //If card at index has alreaby been choosed, try get a new random index
                    while (temp.contains(marketGrid[row][col].get(index))) {
                        index = random.ints(0, 4).findFirst().getAsInt();
                    }

                    //Finally add the card
                    temp.add(marketGrid[row][col].get(index));
                }

                marketGrid[row][col] = temp; //Set the shuffled array in the grid
                temp = new ArrayList<>();

            }
        }


    }

    public DevCard getCard(int row, int col) {
        return marketGrid[row][col].get(0);
    }

    public void removeCard(int row, int col) {
        marketGrid[row][col].remove(0);
    }

    public ArrayList<DevCard>[][] getMarketGrid() {
        return marketGrid;
    }
}
