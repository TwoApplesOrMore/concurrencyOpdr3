import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @Author Harry van Gastel
 */

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        ActorSystem system = ActorSystem.create("Ziggo Dome");

        ActorRef VakAgentRouter = system.actorOf(VakagentRouter.prop(10,30));

    }
}
