import akka.actor.*;
import messages.*;

import java.util.ArrayList;

/**
 * @Author Tim Scholten en Harry van Gastel
 * Een verkoopagent krijgt berichten
 */
public class VerkoopagentActor extends AbstractActor{

    private ArrayList<ActorRef> vakagenten = new ArrayList<>();

    public VerkoopagentActor(ArrayList<ActorRef> vakagenten){
        this.vakagenten = vakagenten;
    }

    public static Props prop(ArrayList<ActorRef> vakagenten){
            return Props.create(VerkoopagentActor.class, vakagenten);
        }

        @Override
        public void preStart() throws Exception {
            super.preStart();
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(OrderMessage.class, msg -> {
                        vakagenten.get(msg.getVak()-1)
                                .tell(msg, getSelf());

                    })
                    .match(PaymentMessage.class, msg -> {
                        vakagenten.get(msg.getVak()-1)
                                .tell(msg, getSelf());
                    })
                    .match(PaymentResponse.class, msg ->{
                        getSender().tell(msg, getSelf());
                    })
                    .match(OrderResponse.class, msg ->{
                        getSender().tell(msg, getSelf());
                    })
                    .match(NoticeMessage.class, msg ->{
                        getSender().tell(msg, getSelf());
                    })
                    .build();
        }

        @Override
        public void postStop() throws Exception {
            super.postStop();
        }
}
