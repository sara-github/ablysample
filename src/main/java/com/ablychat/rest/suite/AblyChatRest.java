package com.ablychat.rest.suite;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AblyChatRest {
    private String channelName;
    private String participantName;
    private static final Logger logger = LogManager.getLogger(AblyChatRest.class);


    AblyChatRest(String channelName, String participantName) {
        this.channelName = channelName;
        this.participantName = participantName;
    }

    public void test() {
        AblyChatRestParticipant participant1 = new AblyChatRestParticipant(channelName,
                this.participantName + "-" + channelName);

        try {
            int counter = 0;

            participant1.printHistory();
            while (true) {
                String message = "MSG-" + this.channelName + "-" +
                        this.participantName + "-" + counter;

                participant1.publish(message);
                logger.info("Last Message: " + participant1.getLastMessage());
                TimeUnit.SECONDS.sleep(10);
                counter++;

//                participant1.presenceEnter();
//                participant2.presenceEnter();
//
//                TimeUnit.SECONDS.sleep(5);
//
//                participant1.presenceLeave();
//                participant2.presenceLeave();

            }

        } catch (InterruptedException e ) {

        }
    }
}
