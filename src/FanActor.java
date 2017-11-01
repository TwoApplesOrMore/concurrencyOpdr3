import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;

import java.util.Arrays;
import java.util.Random;

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
                    switch (message.getType()){
                        case "Order accepted":
                            ResponseMessage responseMessage = new ResponseMessage("Pay", message.getVak()
                                    , message.getRij(), message.getKaarten());
                            master.tell(responseMessage, getSelf());
                            break;
                        case "Order denied":
                            System.out.println(name+" Vak:"+message.getVak()+" vol");
                            getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
                            break;
                        case "Payment accepted":
                            System.out.println(name+" Kaartjes ontvangen vak:"+message.getVak()+" rij:"+ message.getRij()+" plaatsen:"+ Arrays.toString(message.getKaarten()));
                            getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
                            break;
                        case "Payment denied":
                            System.out.println(name+" Er is niet betaald");
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
