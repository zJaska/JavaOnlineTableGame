package it.polimi.ingsw.IntelliCranio.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SinglePlayerData implements Serializable {

    public enum Token {
        GREEN,
        BLUE,
        YELLOW,
        PURPLE,
        BLACK_CROSS,
        SHUFFLE_CROSS
    }

    private int lorenzoFaithPos;

    ArrayList<Token> tokens;

    public SinglePlayerData() {
        lorenzoFaithPos = 0;
        tokens = new ArrayList<>();

        shuffleTokens();
    }

    private void shuffleTokens() {

        ArrayList<Token> temp = new ArrayList<>(Arrays.asList(Token.values()));

        while (temp.size() != 0) {
            Random rand = new Random();
            int randIndex = rand.ints(0, temp.size()).findFirst().getAsInt();

            tokens.add(temp.get(randIndex));
            temp.remove(randIndex);
        }
    }

    public int getLorenzoFaith() { return lorenzoFaithPos; }

    public void addLorenzoFaith() { lorenzoFaithPos++; }


    /**
     * Get a token from available tokens, if the token is a shuffle one,
     * shuffle the available tokens before returning the extracted one,
     * else remove it from available tokens
     *
     * @return The token extracted
     */
    public Token getToken() {

        Token temp = tokens.remove(0);

        if(temp.equals(Token.SHUFFLE_CROSS))
            shuffleTokens();

        return temp;
    }

}
