import akka.actor.*;

/**
 * @Author Tim Scholten en Harry van Gastel
 * Een verkoopagent krijgt berichten
 */
public class VerkoopagentActor extends AbstractActor{

        public static Props prop(){
            return Props.create(VerkoopagentActor.class);
        }

        @Override
        public void preStart() throws Exception {
            super.preStart();
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(Message.class, msg -> {
                        //hier wordt gezocht naar de geschikte vakAgentActor om vervolgens het bericht door te sturen
                        getContext().actorSelection("//ziggo-dome/user/vak"+msg.getVak())
                                .tell(msg, getSender());

                    })
                    .match(ResponseMessage.class, msg -> {
                        //hier wordt gezocht naar de geschikte vakAgentActor om vervolgens het bericht door te sturen
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
