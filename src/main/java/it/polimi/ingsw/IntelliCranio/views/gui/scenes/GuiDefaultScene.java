package it.polimi.ingsw.IntelliCranio.views.gui.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.cards.PopeCard;
import it.polimi.ingsw.IntelliCranio.models.player.Strongbox;
import it.polimi.ingsw.IntelliCranio.models.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.util.GuiUtil;
import it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent;
import it.polimi.ingsw.IntelliCranio.views.gui.Gui;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.views.gui.GameChangedEvent.GAME_CHANGED_EVENT_TYPE;
import static java.lang.Integer.parseInt;

public class GuiDefaultScene extends GuiScene implements SceneWithLeaders, SceneWithResources {

    private Parent root;

    public Button manage_warehouse;
    public Button activate_production;
    public Button confirm;
    public Button cancel;
    public Node goto_market_card;
    public Node goto_market_resource;
    public Node anchor_extra;
    public Node anchor_resources;

    public ArrayList<Node> extra_count;
    public ArrayList<Node> extra_type;
    public ArrayList<Node> warehouse;
    public ArrayList<Node> dev_slots;
    public ArrayList<Node> strongbox;

    private ImageView faith;
    private int faith_pos = 0;
    private double faith_translate_x = 0;
    private double faith_translate_y = 0;
    private int dir = 0; // 0 to 3: right, down, left, up

    private ImageView black_faith;
    private int black_faith_pos = 0;
    private double black_faith_translate_x = 0;
    private double black_faith_translate_y = 0;
    private int black_dir = 0; // 0 to 3: right, down, left, up

    public GuiDefaultScene(Parent parent) {
        super(parent);

        root = parent;
        faith = (ImageView) GuiUtil.getNodeById(root,"faith");
        black_faith = (ImageView) GuiUtil.getNodeById(root,"black_faith");
        manage_warehouse = (Button) GuiUtil.getNodeById(root, "action_manage_warehouse");
        activate_production = (Button) GuiUtil.getNodeById(root, "action_activate_production");
        confirm = (Button) GuiUtil.getNodeById(root, "confirm");
        cancel = (Button) GuiUtil.getNodeById(root, "cancel");
        goto_market_card = GuiUtil.getNodeById(root, "goto_market_card");
        goto_market_resource = GuiUtil.getNodeById(root, "goto_market_resource");
        anchor_extra = GuiUtil.getNodeById(root, "anchor_extra");
        anchor_resources = GuiUtil.getNodeById(root, "anchor_resources");

        extra_count = GuiUtil.getNodesStartingWithId(root, "extra_count");
        extra_type = GuiUtil.getNodesStartingWithId(root, "extra_type");
        warehouse = GuiUtil.getNodesStartingWithId(root, "warehouse");
        dev_slots = GuiUtil.getNodesStartingWithId(root, "dev_slot");
        strongbox = GuiUtil.getNodesStartingWithId(root, "strong");

        manage_warehouse.setVisible(false);
        activate_production.setVisible(false);
        confirm.setVisible(false);
        cancel.setVisible(false);
        anchor_extra.setVisible(false);
        anchor_resources.setVisible(false);

        GuiUtil.setLeadersUpdater(parent);

        Label nickname = ((Label) GuiUtil.getNodeById(root, "nickname"));
        nickname.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
            nickname.setText(MainClient.nickname);
        });

        faith.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
            int newFaithPos = MainClient.game.getPlayer(MainClient.nickname).getFaithPosition();
            while (newFaithPos > faith_pos) {
                if (Arrays.asList(2,16).contains(faith_pos))
                    dir = 3;
                if (Arrays.asList(4,11,18).contains(faith_pos))
                    dir = 0;
                if (faith_pos == 9)
                    dir = 1;

                if (dir == 0)
                    faith_translate_x += faith.getFitWidth() - 2;
                if (dir == 1)
                    faith_translate_y += faith.getFitHeight() -2;
                if (dir == 3)
                    faith_translate_y -= (faith.getFitHeight() -2);

                faith_pos++;
            }

            faith.setTranslateX(faith_translate_x);
            faith.setTranslateY(faith_translate_y);
        });

        if (!MainClient.game.isSinglePlayer())
            black_faith.setImage(null);
        else
            black_faith.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                int newFaithPos = MainClient.game.getSinglePlayerData().getLorenzoFaith();
                while (newFaithPos > black_faith_pos) {
                    if (Arrays.asList(2,16).contains(black_faith_pos))
                        black_dir = 3;
                    if (Arrays.asList(4,11,18).contains(black_faith_pos))
                        black_dir = 0;
                    if (black_faith_pos == 9)
                        black_dir = 1;

                    if (black_dir == 0)
                        black_faith_translate_x += black_faith.getFitWidth() - 2;
                    if (black_dir == 1)
                        black_faith_translate_y += black_faith.getFitHeight() -2;
                    if (black_dir == 3)
                        black_faith_translate_y -= (black_faith.getFitHeight() -2);

                    black_faith_pos++;
                }

                black_faith.setTranslateX(black_faith_translate_x);
                black_faith.setTranslateY(black_faith_translate_y);
            });

        GuiUtil.getNodesStartingWithId(root, "dev_image").forEach(image -> {
            image.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                ArrayList<DevCard>[] cards = MainClient.game.getPlayer(MainClient.nickname).getDevCardsCopy();

                int slot = parseInt(image.getId().split("_")[2]);
                int col = parseInt(image.getId().split("_")[3]);

                if (cards[slot] == null || cards[slot].size() <= col)
                    ((ImageView)image).setImage(null);
                else {
                    InputStream tmp = getClass().getResourceAsStream("/assets/development_cards/" + cards[slot].get(col).getID() + ".jpg");
                    ((ImageView) image).setImage(new Image(tmp));
                }
            });
        });

        GuiUtil.getNodesStartingWithId(root, "img_res_leader").forEach(image -> {
            image.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                ArrayList<LeadCard> leaders = MainClient.game.getPlayer(MainClient.nickname).getLeaders();
                int index = parseInt(image.getId().split("_")[3]);

                try {
                    Resource res = ((DepotAbility) leaders.get(index).getSpecialAbility()).getDepot();

                    if (parseInt(image.getId().split("_")[4]) < res.getAmount()) {
                        InputStream tmp = getClass().getResourceAsStream("/assets/Materials/resource_" + res.getType().toString().toLowerCase() + ".png");
                        ((ImageView) image).setImage(new Image(tmp));
                    } else {
                        ((ImageView)image).setImage(null);
                    }
                } catch (Exception e) {
                    ((ImageView)image).setImage(null);
                }
            });
        });

        GuiUtil.getNodesStartingWithId(root, "strong_count").forEach(label -> {
            label.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                Strongbox box = MainClient.game.getPlayer(MainClient.nickname).getStrongbox();
                int amount = box.getAmount(ResourceType.valueOf(label.getId().split("_")[2].toUpperCase()));
                ((Label) label).setText(String.valueOf(amount));
            });
        });

        GuiUtil.getNodesStartingWithId(root, "extra_count").forEach(label -> {
            label.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                ArrayList<Resource> resources = MainClient.game.getPlayer(MainClient.nickname).getExtraRes();

                int amount = 0;
                for (Resource res : resources)
                    if (res.getType().toString().equalsIgnoreCase(label.getId().split("_")[2]))
                        amount = res.getAmount();

                ((Label) label).setText(String.valueOf(amount));
            });
        });

        warehouse.forEach(image -> {
            image.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                Warehouse warehouse = MainClient.game.getPlayer(MainClient.nickname).getWarehouse();
                int row = parseInt(image.getId().split("_")[1]);
                int col = parseInt(image.getId().split("_")[2]);
                if (warehouse.getDepot()[row] == null || warehouse.getDepot()[row].getAmount() <= col)
                    ((ImageView) image).setImage(null);
                else {
                    InputStream tmp = getClass().getResourceAsStream("/assets/Materials/resource_" + warehouse.getDepot()[row].getType().toString().toLowerCase() + ".png");
                    ((ImageView) image).setImage(new Image(tmp));
                }
            });
        });

        GuiUtil.getNodesStartingWithId(root, "pope").forEach(rect -> {
            rect.addEventHandler(GAME_CHANGED_EVENT_TYPE, event -> {
                ArrayList<PopeCard> cards = MainClient.game.getPlayer(MainClient.nickname).getPopeCards();

                switch (cards.get(parseInt(rect.getId().split("_")[1])).getStatus()) {
                    case ACTIVE:
                        ((Rectangle) rect).setFill(Color.GREEN);
                        break;
                    case INACTIVE:
                        ((Rectangle) rect).setFill(Color.YELLOW);
                        break;
                    case REMOVED:
                        ((Rectangle) rect).setFill(Color.RED);
                        break;
                }
            });
        });
    }

    public void setIdle(Gui gui) {
        goto_market_resource.setOnMouseClicked(event -> {
            gui.setIdleScene(RES_MARKET);
        });
        goto_market_card.setOnMouseClicked(event -> {
            gui.setIdleScene(CARD_MARKET);
        });
    }

    public void removeIdle(Gui gui) {
        goto_market_resource.setOnMouseClicked(null);
        goto_market_card.setOnMouseClicked(null);
    }

    public ArrayList<Node> getLeadersButtons() {
        return GuiUtil.getNodesStartingWithId(root, "leader_btn");
    }

    public ArrayList<Node> getResources() {
        return GuiUtil.getNodesStartingWithId(root, "resource");
    }
}
