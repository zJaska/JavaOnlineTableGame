package it.polimi.ingsw.IntelliCranio.models.cards;

public class PopeCard extends Card{

    public enum Status {INACTIVE, ACTIVE, REMOVED}

    private Status status;

    public PopeCard() {
        this.status=Status.INACTIVE;
    }

    public Status getStatus() {
        return status;
    }

    //Se ACTIVE o REMOVED non può essere cambiato
    public void setStatus(Status status) {
        if(this.status==Status.INACTIVE)
            this.status = status;
    }
}
