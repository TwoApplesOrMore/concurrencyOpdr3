import akka.actor.AbstractActor;

import java.util.Random;

public class FanActor extends AbstractActor {


    private Random RNG = new Random();
    private Message message;
    @Override
    public void preStart() throws Exception {
        Message message;
        int amountOfTickets = RNG.nextInt(4) +1;
        int section = RNG.nextInt(7)+1;
        this.message = new Message("post", section, amountOfTickets);
    }

    @Override
    public Receive createReceive() {
        return null;
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
}
