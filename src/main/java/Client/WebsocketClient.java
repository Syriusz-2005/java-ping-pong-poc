package Client;

import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.MessageType;
import Utils.LogLevel;
import Utils.Logger;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;

@ClientEndpoint(
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class
)
public class WebsocketClient {
    @OnMessage
    public void onMessage(MessageType message) {
        Logger.print("Client received message:", LogLevel.VERY_SPECIFIC);
        Logger.print(message.toString(), LogLevel.VERY_SPECIFIC);
        switch (message.getCommand()) {
            case TEST_CONNECTION -> {
                Logger.printOk("Connection tested, everything works correctly");
            }
        }
    }
}
