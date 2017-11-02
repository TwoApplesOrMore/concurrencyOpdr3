import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Identify;
import akka.actor.Props;

public class VakagentActor extends AbstractActor {
    private int section;
    private int maxRows;
    private int maxSeats;
    private boolean seats[][];

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
        this.seats = new boolean[maxRows][maxSeats];
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
                .match(Message.class, order -> {
                    getSender().tell(processOrder(order), getSender());
                })
                .match(ResponseMessage.class, payment -> {
                    getSender().tell(paymentResponse(payment), getSender());
                })
                .build();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    /**
     * Een methode voor het verwerken van een reservering
     * @param message het meegegeven bericht
     * @return een ResponseMessage om terug te sturen
     */
    public ResponseMessage processOrder(Message message) {
        for (int i = 0; i < maxRows; i++) {
            //aantal zitplaatsen die naast elkaar beschikbaar zijn
            int seatsRdy = 0;

            for (int j = 0; j < maxSeats; j++) {
                if (!seats[i][j]) {
                    seatsRdy++;
                    //als het aantal gewenste kaartjes gelijk is aan beschikbare stoelen wordt een lijst met de kaartjes
                    //terug gegeven in de message
                    if (seatsRdy == message.getKaarten()) {
                        int[] reservedSeats = new int[seatsRdy];
                        for (int k = 0; k < seatsRdy; k++) {
                            seats[i][j - k] = true;
                            reservedSeats[k] = j - k;
                        }
                        return new ResponseMessage("Order accepted", section, i, reservedSeats);

                    }
                } else {
                    seatsRdy = 0;
                }
            }
        }
        // als er niet genoeg plaatsen meer over zijn in het vak
        return new ResponseMessage("Order denied", section, -1, null);


    }

    /**
     * Een methode voor het verwerken van een betaling
     * @param responseMessage het meegegeven bericht
     * @return een ResponseMessage om terug te sturen
     */
    public ResponseMessage paymentResponse(ResponseMessage responseMessage) {
        //als er betaald wordt wordt er een Payment accepted response terug gestuurd en anders worden de gereserveerde
        // zitplaatsen weer vrijgegeven en een bericht terug gestuurd
        if (responseMessage.getType().equals("Pay")) {
            return new ResponseMessage("Payment accepted", responseMessage.getVak()
                    , responseMessage.getRij(), responseMessage.getKaarten());
        } else {
            for (int i = 0; i < responseMessage.getKaarten().length; i++) {
                seats[responseMessage.getRij()][responseMessage.getKaarten()[i]] = false;
            }
            return new ResponseMessage("Payment denied", responseMessage.getVak()
                    , responseMessage.getRij(), responseMessage.getKaarten());
        }

    }
}
