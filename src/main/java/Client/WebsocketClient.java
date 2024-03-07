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
    private ClientManager manager;

    @OnMessage
    public void onMessage(MessageType message) {
        Logger.print("Client received message:", LogLevel.VERY_SPECIFIC);
        Logger.print(message.toString(), LogLevel.VERY_SPECIFIC);
        switch (message.getCommand()) {
            case TEST_CONNECTION -> {
                Logger.printOk("Connection tested, everything works correctly");
            }
            case GAME_JOIN -> {
                Logger.printOk("Succesfully joined the game");
                Logger.printOk("Game code: " + message.data.gameCode);
            }
            case GAME_KICK -> {
                Logger.printErr("You've been kicked, reason: " + message.data.gameKick.reason);
            }
            case GAME_STATE_UPDATE -> {
                manager.sceneManager.scene.applyCorrection(message.data.sceneDataUpdate.objects);
            }
        }
    }

    public void setManager(ClientManager manager) {
        this.manager = manager;
    }
}
