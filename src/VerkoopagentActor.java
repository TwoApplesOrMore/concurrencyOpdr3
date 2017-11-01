import akka.actor.AbstractActor;
import akka.actor.Props;

public class VerkoopagentActor extends AbstractActor{

    private String name;

        public VerkoopagentActor(String name) {
            this.name = name;
        }

        public static Props prop(String name){
            return Props.create(VerkoopagentActor.class, name);
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
