package it.polimi.ingsw.IntelliCranio;

import it.polimi.ingsw.IntelliCranio.server.market.CardMarket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CardMarket market = new CardMarket();
        market.setup();
    }
}
