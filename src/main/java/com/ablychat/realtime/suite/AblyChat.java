package com.ablychat.realtime.suite;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class AblyChat {
    private String channelName;
    private String participantName;

    private static final Logger logger = LogManager.getLogger(AblyChat.class);


    AblyChat(String channelName, String participantName) {
        this.channelName = channelName;
        this.participantName = participantName;
    }

    public void test() {
        AblyChatParticipant participant1 = new AblyChatParticipant(channelName,
                this.participantName + "-" + channelName);

        try {
            int counter = 0;

            participant1.printHistory();
            participant1.presenceEnter();
            while (true) {
                String message = "MSG-" + this.channelName + "-" +
                        this.participantName + "-" + counter;

                participant1.publish(message);
                TimeUnit.SECONDS.sleep(10);
                counter++;

                //participant1.presenceEnter();

                TimeUnit.SECONDS.sleep(5);

                //participant1.presenceLeave();

            }

        } catch (InterruptedException e ) {

        }
    }
}
