package messages;

import akka.actor.ActorRef;

public class OrderMessage {
    private int vak;
    private int kaarten;
    private ActorRef fan;

    /**
     * Een bericht die meegegeven wordt als er kaartjes besteld gaan worden
     * @param vak het gewenste vak
     * @param kaarten het aantal kaartjes die besteld worden
     */
    public OrderMessage(int vak, int kaarten, ActorRef fan) {
        this.vak = vak;
        if(kaarten > 0 && kaarten < 5) {
            this.kaarten = kaarten;
        } else {
            this.kaarten = 1;
        }
        this.fan = fan;
    }

    public int getVak() {
        return vak;
    }

    public int getKaarten() {
        return kaarten;
    }

    public ActorRef getFan() {
        return fan;
    }
}
