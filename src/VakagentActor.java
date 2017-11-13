import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import messages.*;

import java.util.Arrays;

public class VakagentActor extends AbstractActor {
    private int section;
    private int maxRows;
    private int maxSeats;
    private ActorRef seats[][];

    /**
     * @param section  het vak van de vakAgent
     * @param maxRows  maximaal aantal rijen
     * @param maxSeats maximaal aantal zitplaatsen
     * @value seats[][] standaard waarde van de booleans is false, dus als een boolean true is betekent dat dat die
     *                  zitplaats gereserveerd is.
     */
    public VakagentActor(int section, int maxRows, int maxSeats) {
        this.section = section;
        this.maxRows = maxRows;
        this.maxSeats = maxSeats;
        this.seats = new ActorRef[maxRows][maxSeats];
    }


    public static Props prop(int section, int maxRows, int maxSeats) {
        return Props.create(VakagentActor.class, section, maxRows, maxSeats);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    /**
     * roept corresponderende functies aan bij het ontvangen van een bericht
     * @return receiveBuilder()
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderMessage.class, order -> {
                    getSender().tell(processOrder(order), order.getFan());
                })
                .match(PaymentMessage.class, payment -> {
                    getSender().tell(paymentResponse(payment), payment.getActorRef());
                })
                .build();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    /**
     * Een methode voor het verwerken van een reservering
     * @param orderMessage het meegegeven bericht
     * @return een messages.OrderResponse om terug te sturen
     */
    private Object processOrder(OrderMessage orderMessage) {
        for (int i = 0; i < maxRows; i++) {
            //aantal zitplaatsen die naast elkaar beschikbaar zijn
            int seatsRdy = 0;

            for (int j = 0; j < maxSeats; j++) {
                if (seats[i][j] == null) {
                    seatsRdy++;
                    //als het aantal gewenste kaartjes gelijk is aan beschikbare stoelen wordt een lijst met de kaartjes
                    //terug gegeven in de orderMessage
                    if (seatsRdy == orderMessage.getKaarten()) {
                        int[] reservedSeats = new int[seatsRdy];
                        for (int k = 0; k < seatsRdy; k++) {
                            seats[i][j - k] = orderMessage.getFan();
                            reservedSeats[k] = j - k;
                        }
                        return new OrderResponse("Order complete. vak:"+ section+" Seats:"+ Arrays.toString(reservedSeats));

                    }
                } else {
                    seatsRdy = 0;
                }
            }
        }
        // als er niet genoeg plaatsen meer over zijn in het vak
        return new NoticeMessage("Niet genoeg plaatsen in vak "+orderMessage.getVak());
    }

    /**
     * Een methode voor het verwerken van een betaling
     * @param paymentMessage het meegegeven bericht
     * @return een messages.OrderResponse om terug te sturen
     */
    private Object paymentResponse(PaymentMessage paymentMessage) {
        //als er betaald wordt wordt er een Payment accepted response terug gestuurd en anders worden de gereserveerde
        // zitplaatsen weer vrijgegeven en een bericht terug gestuurd
        if (paymentMessage.isPayed()) {
            return new PaymentResponse("Payment accepted");
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 30; j++) {
                    if(seats[i][j] == paymentMessage.getActorRef()){
                        seats[i][j] = null;
                    }
                }
            }
            return new NoticeMessage("Payment not completed");
        }

    }
}
