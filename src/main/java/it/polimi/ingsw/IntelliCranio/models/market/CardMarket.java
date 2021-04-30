package it.polimi.ingsw.IntelliCranio.models.market;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class CardMarket {

    private final int rows, cols;
    private ArrayList<DevCard>[][] marketGrid;

    public CardMarket(String path, int rows, int cols, boolean shuffle) {
        this.rows = rows;
        this.cols = cols;
        marketGrid = new ArrayList[rows][cols];

        setup(path, shuffle);
    }

    /**
     * <summary>
     *     Creates all the development cards and place them on the market grid.
     *     Each group is then shuffled if shuffle is enabled.
     * </summary>
     * @param path The path of the json config file to create the cards
     * @param shuffle Flag if grid has to be shuffled
     */
    private void setup(String path, boolean shuffle) {
        //Creating the list for each group
        for (int row = 0; row < rows; ++row)
            for (int col = 0; col < cols; ++col)
                marketGrid[row][col] = new ArrayList<>();

        generateGrid(path);
        if(shuffle)
            shuffle();
    }

    /**
     * <summary>
     *     Generates the grid of cards from a json file.
     * </summary>
     */
    private void generateGrid(String path) {
        ArrayList<DevCard> cardList = new ArrayList<>(); //Temporary list to create all the cards

        try {
            //Get the json file as String
            String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            //Generate the list of cards
            cardList = gson.fromJson(text, new TypeToken<ArrayList<DevCard>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Divide all the cards into each slot
        cardList.forEach(card -> {
            DevCard temp = (DevCard) card;
            int col;

            //Assign each type to corresponding column
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
                default:
                    col = -1;
                    break;
            }
            //Add the card to the slot
            marketGrid[rows - card.getLevel()][col].add(card);
        });

    }


    /**
     * <summary>
     *     Shuffle each group of cards in the grid.
     * </summary>
     */
    private void shuffle() {

        ArrayList<DevCard> temp = new ArrayList<>(); //Temporary list for shuffled cards

        int index; //Index to pick the random card
        Random random = new Random();

        for(int row = 0; row < rows; ++row) {
            for(int col = 0; col < cols; ++col) {
                for(int i = 0; i < marketGrid[row][col].size(); ++i) {
                    //Get a random number between range
                    index = random.ints(0, marketGrid[row][col].size()).findFirst().getAsInt();

                    //If card at index has already been choosed, try get a new random index
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

    /**
     * <summary>
     *     Get the first card given the grid coordinates.
     *     Null if slot is empty.
     * </summary>
     * @param row
     * @param col
     * @return First DevCard in slot or null if empty
     */
    public DevCard getCard(int row, int col) {
        if(marketGrid[row][col].isEmpty())
            return null;

        return marketGrid[row][col].get(0);
    }

    /**
     * <summary>
     *     Remove the first card given the grid coordinates.
     *     Remaining cards get shifted in the list.
     * </summary>
     * @param row
     * @param col
     */
    public void removeCard(int row, int col) {
        if(!marketGrid[row][col].isEmpty())
            marketGrid[row][col].remove(0);
    }

    //Test
    public ArrayList<DevCard>[][] getMarketGrid() {
        return marketGrid;
    }
}
