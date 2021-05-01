package it.polimi.ingsw.IntelliCranio;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        Game game;
        Game game1;

        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");

        game = new Game(nicknames);

        Save.saveGame(game);

        game = Save.loadSave(game.getUuid());

        System.out.println();

    }
}
