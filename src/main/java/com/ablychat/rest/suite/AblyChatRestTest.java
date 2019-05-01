package com.ablychat.rest.suite;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AblyChatRestTest {
    private static final Logger logger = LogManager.getLogger(AblyChatRestTest.class);

    public static void main(String[]  args) {
        String participantName = "P1";
        String channelName = "INT-1";

        if (args.length < 2) {
            logger.error("Expected two arguments: <Interaction Id> <Participant Id>");
            System.exit(1);
        }

        channelName = args[0];
        participantName = args[1];

        logger.info("Starting ably rest api app with Interaction: " + channelName + "; participant: " + participantName);
        AblyChatRest chat = new AblyChatRest(channelName, participantName);
        chat.test();
    }
}
