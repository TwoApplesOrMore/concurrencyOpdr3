package messages;

import akka.actor.ActorRef;

public class PaymentMessage {
    private boolean payed;
    private ActorRef actorRef;
    private int vak;

    public PaymentMessage(boolean payed, ActorRef actorRef, int vak) {
        this.payed = payed;
        this.actorRef = actorRef;
        this.vak = vak;
    }

    public boolean isPayed() {
        return payed;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }

    public int getVak() {
        return vak;
    }
}
