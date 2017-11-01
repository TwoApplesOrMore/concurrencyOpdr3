import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.routing.*;

import java.util.ArrayList;
import java.util.List;

public class TicketMaster extends AbstractActor {

    Router router;


    {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ActorRef r = getContext().actorOf(VerkoopagentActor.prop("SA:"+(i+1)), "SA"+(i+1));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new SmallestMailboxRoutingLogic(), routees);


    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, msg -> {
                    router.route(msg, getSender());
                })
                .match(ResponseMessage.class, msg ->{
                    router.route(msg, getSender());
                })
                .build();

    }
}
