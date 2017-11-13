import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;

/**
 * @Author Tim Scholten & Harry van Gastel
 *
 * Hier wordt het systeem, de routerActor en alle actoren aangemaakt, door bij het maken van de FanActors een bericht
 * mee te geven met start, beginnen ze met een bericht te sturen naar de TicketMaster.
 */

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        ActorSystem system = ActorSystem.create("ziggo-dome");

        ActorRef master = system.actorOf(Props.create(TicketMaster.class), "master");

        for (int i = 0; i < 700; i++) {
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
