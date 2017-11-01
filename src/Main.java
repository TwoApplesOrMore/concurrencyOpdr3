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
        ActorSystem system = ActorSystem.create("ziggo-dome");

        ActorRef master = system.actorOf(Props.create(TicketMaster.class), "master");

        for (int i = 0; i < 7; i++) {

            ActorRef vakAgent = system.actorOf(VakagentActor.prop((i+1),10,10), "vak"+(i+1));
        }

        for (int i = 0; i < 1000; i++) {
            ActorRef fan = system.actorOf(FanActor.prop("Fan:"+(i+1), master), "fan"+(i+1));
            fan.tell("Start", ActorRef.noSender());
        }


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        system.terminate();
    }
}
