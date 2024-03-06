package Client;

import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.MessageType;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;

@ClientEndpoint(
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class
)
public class WebsocketClient {
    @OnMessage
    public void onMessage(MessageType message) {
        System.out.println("Client received message:");
        System.out.println(message);
    }
}
