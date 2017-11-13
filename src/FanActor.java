import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import messages.*;

import java.util.Random;

/**
 * @Author Tim Scholten & Harry van Gastel
 *
 * De fanactor is de actor die kaartjes wilt bestellen en kopen door berichten naar de router actor te sturen
 */
public class FanActor extends AbstractActor {


    private Random RNG = new Random();
    private OrderMessage orderMessage;
    private String name;
    private ActorRef master;
    private int section;

    public FanActor(String name, ActorRef master) {
        this.name = name;
        this.master = master;
    }

    public static Props prop(String name, ActorRef master) {
        return Props.create(FanActor.class, name, master);
    }

    /**
     * Hier worden het vak en het aantal tickets dat besteld wordt random gemaakt
     * Ook wordt het basis bericht gegenereerd.
     *
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        int amountOfTickets = RNG.nextInt(4) + 1;
        int section = RNG.nextInt(7) + 1;
        this.section = section;
        this.orderMessage = new OrderMessage(section, amountOfTickets, getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    if (msg.equals("Start")) {
                        master.tell(orderMessage, getSelf());
                    }
                })
                .match(OrderResponse.class, msg -> {
                    System.out.println(name + " " + msg.getMessage());
                    master.tell(new PaymentMessage(true ,getSelf(), section), getSelf());
                })
                .match(PaymentResponse.class, msg ->{
                    System.out.println(name + " Tickets recieved: "+msg.getSeats());
                    getSelf().tell(PoisonPill.class, ActorRef.noSender());
                })
                .match(NoticeMessage.class, msg ->{
                    System.out.println(name + " " + msg.getMessage());
                    getSelf().tell(PoisonPill.class, ActorRef.noSender());
                })
                .build();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
}
