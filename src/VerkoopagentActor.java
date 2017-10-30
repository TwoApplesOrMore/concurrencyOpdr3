import akka.actor.AbstractActor;
import akka.actor.Props;

public class VerkoopagentActor extends AbstractActor{

        private int section;

        public VerkoopagentActor(int section, int maxRows, int maxSeats) {
            this.section = section;
        }

        public static Props prop(){
            return Props.create(VakagentActor.class);
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
                        }
                    }).build();
        }

        @Override
        public void postStop() throws Exception {
            super.postStop();
        }
}
