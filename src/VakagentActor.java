import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class VakagentActor extends AbstractActor {
    private int section;
    private int maxRows;
    private int maxSeats;
    private boolean seats[][];
    private int salesAgents = 1;

    public VakagentActor(int section, int maxRows, int maxSeats) {
        this.section = section;
        this.maxRows = maxRows;
        this.maxSeats = maxSeats;
        // default value of the booleans are false, so this means that when a seat is 'false', that there is nobody occupying it yet
        this.seats = new boolean[maxRows][maxSeats];
    }


    public static Props prop(int section, int maxRows, int maxSeats){
        return Props.create(VakagentActor.class, section, maxRows, maxSeats);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, order -> {
                    if (order.equals("Start")) {
                        ActorRef Salesagent = getContext().actorOf(Props.create(VerkoopagentActor.class, "Salesagent " + salesAgents));
                        salesAgents++;
                    }
                }).build();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
}
