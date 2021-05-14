package it.polimi.ingsw.IntelliCranio.views.gui;


import javafx.event.Event;
import javafx.event.EventType;

public class GameChangedEvent extends Event {

    public static final EventType<GameChangedEvent> GAME_CHANGED_EVENT_TYPE = new EventType(ANY);

    public GameChangedEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
