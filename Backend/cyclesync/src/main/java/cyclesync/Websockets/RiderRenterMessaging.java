package cyclesync.Websockets;

import java.io.IOException;
import java.util.Hashtable;
import java.util.*;


import cyclesync.Messages.Message;
import cyclesync.Messages.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cyclesync.Users.UserRepository;
import cyclesync.Users.User;



/**
 * Sets us websocket endpoint for handling messages between a renter and a rider.
 * They are both connected to the server via id and join a session to
 * have realtime communication between each other.
 *
 *
 * Caleb: wrote the initial skeleton for messaging
 * Neil: implemented direct messaging between users
 *
 *
 *
 * Future ToDo: log the messages to save between sessions
 */

@ServerEndpoint("/messaging/{fromId}/{toId}")
@Component
public class RiderRenterMessaging {

    private static MessageEventBus messageEventBus;

    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();

    private final Logger logger = LoggerFactory.getLogger(RiderRenterMessaging.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("fromId") String sender, @PathParam("toId") String receiver) throws IOException {
        // server side log
        logger.info("[onOpen] from " + sender +"to"+receiver);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(sender)) {
            //            session.getBasicRemote().sendText("id already exists");
//            session.close();
            usernameSessionMap.remove(sender);
            sessionUsernameMap.remove(session);
            sessionUsernameMap.put(session, sender);
            usernameSessionMap.put(sender, session);
        } else {
            // map current session with username
            sessionUsernameMap.put(session, sender);
            // map current username with session
            usernameSessionMap.put(sender, session);
            // send to the user joining in
//            dmUser(sender, "Welcome to the chat server, "+ sender);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("toId") String receiver) throws IOException {
        String sender = sessionUsernameMap.get(session);

        logger.info("[onMessage] " + sender + " to " + receiver + ": " + message);

        dmUser(receiver, message);

        // Publish a MessageEvent
       // messageEventBus.publishMessageEvent(new MessageEvent(Integer.parseInt(sender), Integer.parseInt(receiver), message));

        // Manually obtain the MessageEventBus from the application context
        if (messageEventBus == null) {
            messageEventBus = ApplicationContextProvider.getApplicationContext().getBean(MessageEventBus.class);
        }

        // Publish a MessageEvent
        if (messageEventBus != null) {
            messageEventBus.publishMessageEvent(new MessageEvent(Integer.parseInt(sender), Integer.parseInt(receiver), message));
        }

        /*
        if (userRepository.findById(Math.toIntExact(Long.parseLong(sender))).isPresent()
                && userRepository.findById(Math.toIntExact(Long.parseLong(receiver))).isPresent()){
            Message newMessage = new Message(Math.toIntExact(Long.parseLong(sender)), Math.toIntExact(Long.parseLong(receiver)), message);
            messageRepository.save(newMessage);
            logger.info("[onMessage] saved new message!");
        }*/
    }

    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String id = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + id);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(id);

        // send the message to chat
//        broadcast(id + " disconnected");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // get the username from session-username mapping
        String id = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + id + ": " + throwable.getMessage());
    }

    private void dmUser(String id, String message) {
        try {
            usernameSessionMap.get(id).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, id) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}