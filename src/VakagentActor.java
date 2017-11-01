import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Identify;
import akka.actor.Props;

public class VakagentActor extends AbstractActor {
    private int section;
    private int maxRows;
    private int maxSeats;
    private boolean seats[][];


    public VakagentActor(int section, int maxRows, int maxSeats) {
        this.section = section;
        this.maxRows = maxRows;
        this.maxSeats = maxSeats;
        // default value of the booleans are false, so this means that when a seat is 'false', that there is nobody occupying it yet
        this.seats = new boolean[maxRows][maxSeats];
    }


    public static Props prop(int section, int maxRows, int maxSeats) {
        return Props.create(VakagentActor.class, section, maxRows, maxSeats);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, order -> {
                    if (order.getType().equals("Order")) {
                        getSender().tell(processOrder(order), getSender());
                    }
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

    public ResponseMessage processOrder(Message message) {
        for (int i = 0; i < maxRows; i++) {
            int seatsRdy = 0;

            for (int j = 0; j < maxSeats; j++) {
                if (!seats[i][j]) {
                    seatsRdy++;
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

        return new ResponseMessage("Order denied", section, -1, null);


    }

    public ResponseMessage paymentResponse(ResponseMessage responseMessage) {
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
