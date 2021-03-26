package it.polimi.ingsw.IntelliCranio.server.cards;

public class PopeCard extends Card{

    public enum Status {INACTIVE, ACTIVE, REMOVED}

    private Status status;


    public Status getStatus() {
        return status;
    }

    //Se ACTIVE o REMOVED non può essere cambiato
    public void setStatus(Status status) {
        this.status = status;
    }
}
