package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.InputStream;
import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CANCEL;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DEFAULT;
import static it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent.GAME_CHANGED_EVENT_TYPE;
import static java.lang.Integer.parseInt;

public class GuiCardMarketScene extends GuiScene {

    public Node goto_default;
    public ArrayList<Node> dev_cards;

    public GuiCardMarketScene(Parent parent) {
        super(parent);

        dev_cards = GuiUtil.getNodesStartingWithId(parent, "dev");
        goto_default = GuiUtil.getNodeById(parent, "goto_default");

        dev_cards.forEach(image -> {
            image.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                int row = parseInt(image.getId().split("_")[1]);
                int col = parseInt(image.getId().split("_")[2]);

                DevCard card = MainClient.getGame().getCardMarket().getCard(row, col);

                if (card == null)
                    ((ImageView) image).setImage(null);
                else {
                    InputStream tmp = getClass().getResourceAsStream("/assets/development_cards/" + card.getID() + ".JPG");
                    ((ImageView) image).setImage(new Image(tmp));
                }
            });
        });

        GuiUtil.getNodesStartingWithId(parent, "count").forEach(label -> {
            label.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                int row = parseInt(label.getId().split("_")[1]);
                int col = parseInt(label.getId().split("_")[2]);

                ((Label) label).setText(String.valueOf(MainClient.getGame().getCardMarket().getCardsAmount(row,col)));
            });
        });
    }

    public void setIdle(Gui gui) {
        goto_default.setOnMouseClicked(event -> {
            gui.setIdleScene(DEFAULT);
        });
    }

    public void removeIdle(Gui gui) {
        goto_default.setOnMouseClicked(null);
    }
}
