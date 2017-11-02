import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;

import java.util.Arrays;
import java.util.Random;

/**
 * @Author Tim Scholten & Harry van Gastel
 *
 * De fanactor is de actor die kaartjes wilt bestellen en kopen door berichten naar de router actor te sturen
 */
public class FanActor extends AbstractActor {


    private Random RNG = new Random();
    private Message message;
    private String name;
    private ActorRef master;

    public FanActor(String name, ActorRef master) {
        this.name = name;
        this.master = master;
    }

    public static Props prop(String name, ActorRef master) {
        return Props.create(FanActor.class, name, master);
    }

    /**
     * Hier worden het vak en het aantal tickets dat besteld wordt random geset
     * Ook wordt het basis bericht gegenereerd.
     *
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        int amountOfTickets = RNG.nextInt(4) + 1;
        int section = RNG.nextInt(7) + 1;
        this.message = new Message("Order", section, amountOfTickets);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    if (msg.equals("Start")) {
                        master.tell(message, getSelf());
                    }
                })
                .match(ResponseMessage.class, message -> {
                    switch (message.getType()) {
                        //Als de order wordt geaccepteerd wordt er een response gestuurd naar de router actor dat hij betaald
                        case "Order accepted":
                            ResponseMessage responseMessage = new ResponseMessage("Pay", message.getVak()
                                    , message.getRij(), message.getKaarten());
                            master.tell(responseMessage, getSelf());
                            break;
                        //in alle andere gevallen moet er allen wat geprint worden en vervolgens de actor gekillt worden
                        case "Order denied":
                            System.out.println(name + " Vak:" + message.getVak() + " vol");
                            getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
                            break;
                        case "Payment accepted":
                            System.out.println(name + " Kaartjes ontvangen vak:" + message.getVak() + " rij:" + message.getRij() + " plaatsen:" + Arrays.toString(message.getKaarten()));
                            getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
                            break;
                        case "Payment denied":
                            System.out.println(name + " Er is niet betaald");
                            getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
                    }
                })
                .build();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
}
