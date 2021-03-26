package it.polimi.ingsw.IntelliCranio.server.market;

import com.google.gson.Gson;
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
        //shuffle();
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
            cardList = gson.fromJson(text,ArrayList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int a;


        /*

        String prefix = "developmentcard_front";
        String color;
        String idLevel;
        String cardID;

        int vp;
        DevCard.CardType type;
        int level;
        ArrayList<FinalResource> cardCost;
        ArrayList<FinalResource> productionCost;
        ArrayList<FinalResource> product;

        int row, col;

        //region GREEN -> col = 0
        col = 0;

        color = "g";
        type = DevCard.CardType.GREEN;

        //region LEVEL 1 -> row = 2
        row = 2;

        idLevel = "1";
        level = 1;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 1;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 2;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 3;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 4;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 2 -> row = 1
        row = 1;

        idLevel = "2";
        level = 2;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 5;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 6;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 7;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 5));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 8;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 3 -> row = 0
        row = 0;

        idLevel = "3";
        level = 3;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 9;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 6));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 3));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 10;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 5));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 11;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 7));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 12;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 4));
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 3));
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //endregion

        //region BLUE -> col = 1
        col = 1;

        color = "b";
        type = DevCard.CardType.BLUE;

        //region LEVEL 1 -> row = 2
        row = 2;

        idLevel = "1";
        level = 1;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 1;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 2;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 3;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 4;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 2 -> row = 1
        row = 1;

        idLevel = "2";
        level = 2;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 5;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 6;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 7;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 5));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 8;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 3 -> row = 0
        row = 0;

        idLevel = "3";
        level = 3;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 9;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 6));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 10;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 5));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 11;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 7));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 12;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 4));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //endregion

        //region YELLOW -> col = 2
        col = 2;

        color = "y";
        type = DevCard.CardType.YELLOW;

        //region LEVEL 1 -> row = 2
        row = 2;

        idLevel = "1";
        level = 1;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 1;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 2;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 3;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 4;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 2 -> row = 1
        row = 1;

        idLevel = "2";
        level = 2;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 5;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 6;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 7;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 5));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 8;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 3 -> row = 0
        row = 0;

        idLevel = "3";
        level = 3;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 9;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 6));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 3));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 10;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 5));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 2));
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 11;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 7));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 12;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 4));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //endregion

        //region PURPLE -> col = 3
        col = 3;

        color = "p";
        type = DevCard.CardType.PURPLE;

        //region LEVEL 1 -> row = 2
        row = 2;

        idLevel = "1";
        level = 1;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 1;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 2;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 3;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 4;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 2 -> row = 1
        row = 1;

        idLevel = "2";
        level = 2;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 5;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 6;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 7;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 5));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 8;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 3));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //region LEVEL 3 -> row = 0
        row = 0;

        idLevel = "3";
        level = 3;

        //region CARD 1
        cardID = prefix + "_" + color + "_" + idLevel + "_1";

        vp = 9;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 6));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 2));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 3));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 2));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 2
        cardID = prefix + "_" + color + "_" + idLevel + "_2";

        vp = 10;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 5));
        cardCost.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        productionCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.COIN, 2));
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 3
        cardID = prefix + "_" + color + "_" + idLevel + "_3";

        vp = 11;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 7));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 3));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        //region CARD 4
        cardID = prefix + "_" + color + "_" + idLevel + "_4";

        vp = 12;

        cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 4));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 4));

        productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.STONE, 3));
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        marketGrid[row][col].add(new DevCard(cardID, vp, type, level, cardCost, productionCost, product));
        //endregion

        // endregion

        //endregion

        */
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
