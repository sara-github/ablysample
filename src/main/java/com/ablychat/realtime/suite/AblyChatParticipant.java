package com.ablychat.realtime.suite;

import io.ably.lib.realtime.*;
import io.ably.lib.realtime.Channel.*;
import io.ably.lib.types.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AblyChatParticipant {

    private static final Logger logger = LogManager.getLogger(AblyChatParticipant.class);

    private String channelName;
    private AblyRealtime ablyConnection;
    private String participantName;
    private Channel channel;

    AblyChatParticipant(String channelName, String participantName) {
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
        options.echoMessages = false; //dont receive self messages. default is to recieve self messages

        AblyRealtime ablyconnection = new AblyRealtime(options);
        this.ablyConnection = ablyconnection;

        ablyconnection.connection.on(new ConnectionStateListener() {
            @Override
            public void onConnectionStateChanged(ConnectionStateChange state) {
                logger.info("New state is " + state.current.name() +
                        " in channel: " + channelName + "; participant: " + participantName);
                switch (state.current) {
                    case connected: {
                        // Successful connection
                        break;
                    }
                    case failed: {
                        // Failed connection
                        break;
                    }
                }
            }
        });

        Channel channel = ablyconnection.channels.get(channelName);
        this.channel = channel;
        channel.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message) {

                logger.info("Received " + message.data + " message from: " + message.name +
                        " in channel: " + channelName + "; received by: " + participantName);
            }
        });

        ChannelStateListener listener = new ChannelStateListener() {

            @Override
            public void onChannelStateChanged(ChannelStateChange change) {
                logger.info("Channel state changed to " + change.current.name() +
                        " in channel: " + channelName + "; participant: " + participantName);
            }
        };

        channel.on(listener);

        channel.presence.subscribe(new Presence.PresenceListener() {
            @Override
            public void onPresenceMessage(PresenceMessage messages) {
                logger.info(channelName + "; participant= " + participantName +
                        "; client = " + messages.clientId + "; action=" + messages.action);
            }
        });

    }

    public void publish(String message) {
        try {
            this.channel.publish(this.participantName, message, new CompletionListener() {
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

    public void presenceEnter() {
        try {
            this.channel.presence.enter(participantName, new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });
        } catch (Exception e) {
            logger.error("Error in presence Enter" +
                    " in channel: " + channelName + "; participant: " + participantName);
        }
    }

    public void presenceLeave() {
        try {
            this.channel.presence.leave(participantName, new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });
        } catch (Exception e) {
            logger.error("Error in presence leave" +
                    " in channel: " + channelName + "; participant: " + participantName);
        }
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
