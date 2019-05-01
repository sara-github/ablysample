package com.ablychat.rest.suite;

import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.rest.Channel;
import io.ably.lib.types.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AblyChatRestParticipant {

    private static final Logger logger = LogManager.getLogger(AblyChatRestParticipant.class);

    private String channelName;
    private AblyRest ablyRest;
    private String participantName;
    private Channel channel;

    AblyChatRestParticipant(String channelName, String participantName) {
        this.channelName = channelName;
        this.participantName = participantName;
        try {
            initializeConnection();
        } catch (AblyException e) {
            logger.info("");
        }
    }

    private void initializeConnection() throws AblyException {
        ClientOptions options = new ClientOptions("KTbmDQ.mrODoQ:QWZuMP2sLuQEa8FF");
        options.clientId =  participantName;

        AblyRest ablyRest = new AblyRest(options);

        Channel channel = ablyRest.channels.get(channelName);
        this.channel = channel;

    }

    public void publish(String message) {
        try {
            Message[] messages = new Message[]{new Message(this.participantName,  message)};

            this.channel.publishAsync(messages, new CompletionListener() {
                @Override
                public void onSuccess() {
                    logger.info("Message successfully sent: " + message);
                }

                @Override
                public void onError(ErrorInfo reason) {
                    logger.error("Unable to publish message; err = " + reason.message +
                                " in channel: " + channelName + "; participant: " + participantName);
                }
            });
        } catch (Exception e) {
            logger.info("Error while publishing in channel: " + channelName + "; participant: " + participantName);
        }
    }

    public String getLastMessage() {
        Param param = new Param("limit", 4);
        String lastMessage = null;
        try {

            PaginatedResult<Message> paginatedResult = this.channel.history(new Param[]{param});
            while (paginatedResult.hasNext()) {
                Message[] messages = paginatedResult.next().items();
                for (Message message: messages) {
                    if (!this.participantName.equals(message.name)) {
                        lastMessage = "Received " + message.data + " message from: " + message.name +
                                " in channel: " + channelName + "; received by: " + participantName;
                        break;
                    }
                }
                if (lastMessage != null)
                    break;
            }

        } catch (AblyException e) {
            logger.error("Unable to get history");
        }
        return lastMessage;
    }

    public void printHistory() {
        Param param = new Param("limit", 10);
        try {

            PaginatedResult<Message> paginatedResult = this.channel.history(new Param[]{param});
            while (paginatedResult.hasNext()) {
                Message[] messages = paginatedResult.next().items();
                for (Message message: messages) {
                    logger.info("HIST: Received " + message.data + " from: " + message.name +
                            " in channel: " + channelName + "; by: " + participantName);
                }
            }

        } catch (AblyException e) {
            logger.error("Unable to get history");
        }
    }

}
