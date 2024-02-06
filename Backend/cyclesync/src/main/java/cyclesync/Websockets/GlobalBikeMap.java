package cyclesync.Websockets;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

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

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 *
 * Caleb: Had initial skeleton
 * Neil: Authored all code
 *
 */

@ServerEndpoint("/rental-updates/{userId}")
@Component
public class GlobalBikeMap {
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    // server side logger
    private final Logger logger = LoggerFactory.getLogger(GlobalBikeMap.class);
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String user) throws IOException {

        // server side log
        logger.info("[onOpen] from " + user);

        // Handle the case of a duplicate username
        if (sessionUsernameMap.containsKey(user)) {
//            session.getBasicRemote().sendText("id already exists");
//            session.close();
            sessionUsernameMap.remove(session);
            sessionUsernameMap.put(session, user);
        } else {
            // map current session with username
            sessionUsernameMap.put(session, user);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // get the username from session-username mapping
        String id = sessionUsernameMap.get(session);
        // server side log
        logger.info("[onClose] " + id);
        // remove user from memory mappings
        sessionUsernameMap.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }


    public void broadcast(String rentalJson) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(rentalJson);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}