import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.routing.*;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author Tim Scholten & Harry van Gastel
 *
 * De TicketMaster is een actor die een router beheert.
 */
public class TicketMaster extends AbstractActor {

    /**
     * Er wordt hier een router aangemaakt die berichten die hij ontvangt, doorstuurt naar verkoopagenten
     * (via smallestMailBoxRoutingLogic, zodat de minst drukke verkoopagenten de meeste berichten krijgt)
     */
    Router router;
    {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ActorRef r = getContext().actorOf(VerkoopagentActor.prop(),"SA"+(i+1));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new SmallestMailboxRoutingLogic(), routees);
    }

    /**
     * stuurt berichten door naar de router.
     * @return receiveBuilder()
     */
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
