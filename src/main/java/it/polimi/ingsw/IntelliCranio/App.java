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
        CardMarket market = new CardMarket(3, 4);
        market.setup("src/main/resources/devcards_config.json", true);

    }
}
