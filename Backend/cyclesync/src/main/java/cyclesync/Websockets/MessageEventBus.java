package cyclesync.Websockets;

import cyclesync.Websockets.MessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MessageEventBus {
    private final ApplicationEventPublisher eventPublisher;

    public MessageEventBus(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishMessageEvent(MessageEvent messageEvent) {
        eventPublisher.publishEvent(messageEvent);
    }
}
