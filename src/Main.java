import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;

/**
 * @Author Harry van Gastel
 */

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        ActorSystem system = ActorSystem.create("Ziggo_Dome");

        ActorRef master = system.actorOf(Props.create(TicketMaster.class));


        for (int i = 0; i < 25; i++) {
            ActorRef SalesAgent = system.actorOf(VerkoopagentActor.prop("SA:" + (i+1)));
        }

        for (int i = 0; i < 7; i++) {

            ActorRef VakAgent = system.actorOf(VakagentActor.prop((i+1),10,30));
        }


    }
}
