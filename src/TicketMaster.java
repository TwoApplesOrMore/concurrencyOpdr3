import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class TicketMaster extends AbstractActor {

    ActorRef router;

    {
        router = getContext().actorOf(new RoundRobinPool(10).props(Props.create(VerkoopagentActor.class, "test")), "router");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> {
                            router.tell(message, getSender());
                        }
                ).build();

    }
}
