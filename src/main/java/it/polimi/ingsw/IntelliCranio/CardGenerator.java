package it.polimi.ingsw.IntelliCranio;

import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CardGenerator {

    public static void generateDevCardFile(ArrayList<DevCard>[][] grid) throws IOException {
        FileWriter cardFile = new FileWriter("src/main/resources/devcards.txt");
        DevCard temp;

        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 4; ++col) {
                for(int i = 0; i < 4; ++i) {
                    temp = grid[row][col].get(i);

                    cardFile.write("ID: " + temp.getID() + "\n");
                    cardFile.write("VP: " + Integer.toString(temp.getVictoryPoints()) + "\n");
                    cardFile.write("TYPE: " + temp.getType().toString() + "\n");
                    cardFile.write("LEVEL: " + Integer.toString(temp.getLevel()) + "\n");
                    cardFile.write("CARD COST:\n");

                    temp.getCardCost().forEach(resource -> {
                        try {
                            cardFile.write(resource.getType().toString() + " " + Integer.toString(resource.getAmount()) + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    cardFile.write(".\n");

                    cardFile.write("PRODUCTION COST:\n");

                    temp.getProductionCost().forEach(resource -> {
                        try {
                            cardFile.write(resource.getType().toString() + " " + Integer.toString(resource.getAmount()) + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    cardFile.write(".\n");

                    cardFile.write("PRODUCT:\n");

                    temp.getProduct().forEach(resource -> {
                        try {
                            cardFile.write(resource.getType().toString() + " " + Integer.toString(resource.getAmount()) + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    cardFile.write(".\n\n");

                }
            }
        }

        cardFile.close();

    }

}
