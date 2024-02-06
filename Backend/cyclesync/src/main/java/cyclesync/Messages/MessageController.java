package cyclesync.Messages;
//adding this comment to commit something
import cyclesync.Users.UserRepository;
import cyclesync.Websockets.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cyclesync.Websockets.MessageEventBus;

@RestController
public class MessageController {
    @Autowired
    private MessageEventBus messageEventBus;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //-------------------------- CRUD Operations ------------------//
    /*
    // get all messages
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // get all messages between two users
    @GetMapping("/messages/{senderId}/{receiverId}")
    public List<Message> getAllMessagesBetweenUsers(
            @PathVariable int senderId,
            @PathVariable int receiverId) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    // post a new message
    @PostMapping("/messages")
    public ResponseEntity<String> postMessage(@RequestBody Message newMessage) {
        if (userRepository.existsById(newMessage.getSenderId())
                && userRepository.existsById(newMessage.getReceiverId())) {
            messageRepository.save(newMessage);
            return ResponseEntity.ok(success);
        } else {
            return ResponseEntity.status(400).body(failure);
        }
    }*/

    // delete a message
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        if (messageRepository.existsById((long) id)) {
            messageRepository.deleteById((long) id);
            return ResponseEntity.ok(success);
        } else {
            return ResponseEntity.status(400).body(failure);
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message savedMessage = messageRepository.save(message);
        // You can also broadcast the message to relevant WebSocket sessions here
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/messages/{userId}/{receiverId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable int userId, @PathVariable int receiverId) {
        // Retrieve messages sent by userId to receiverId
        List<Message> messagesFromUser = messageRepository.findBySenderIdAndReceiverId(userId, receiverId);

        // Retrieve messages sent by receiverId to userId
        List<Message> messagesFromReceiver = messageRepository.findBySenderIdAndReceiverId(receiverId, userId);

        // Combine both message lists into one stream, then collect as a list
        List<Message> combinedMessages = Stream.concat(messagesFromUser.stream(), messagesFromReceiver.stream())
                .sorted(Comparator.comparing(Message::getTimestamp)) // assuming Message has a getTimestamp method
                .collect(Collectors.toList());

        // Return the combined and sorted list of messages
        return ResponseEntity.ok(combinedMessages);
    }

    @EventListener
    public void handleNewMessageEvent(MessageEvent messageEvent) {
        // Handle the received message event and save it to the database
        Message message = new Message(messageEvent.getSenderId(), messageEvent.getReceiverId(), messageEvent.getContent());
        messageRepository.save(message);
    }
}
