import akka.actor.*;

public class VerkoopagentActor extends AbstractActor{

    private String name;
    private Object message;
    private ActorRef actorRef;

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
                    .match(Message.class, msg -> {
                        System.out.println(msg.getType() + " kaarten:" + msg.getKaarten()+" vak:" + msg.getVak());
                        getContext().actorSelection("//ziggo-dome/user/vak"+msg.getVak())
                                .tell(msg, getSender());

                    })
                    .match(ResponseMessage.class, msg -> {
                        getContext().actorSelection("//ziggo-dome/user/vak"+msg.getVak())
                                .tell(msg, getSender());
                    })
                    .build();
        }

        @Override
        public void postStop() throws Exception {
            super.postStop();
        }
}
