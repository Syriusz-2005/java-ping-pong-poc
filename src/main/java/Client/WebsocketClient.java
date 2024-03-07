package Client;

import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.MessageType;
import Physics.Rectangle;
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
                Logger.printOk("Successfully joined the game");
                Logger.printOk("Game code: " + message.data.gameCode);
            }
            case GAME_KICK -> {
                Logger.printErr("You've been kicked, reason: " + message.data.gameKick.reason);
            }
            case GAME_STATE_UPDATE -> {
                manager.setGameState(message.data.gameStateUpdate.newState);
            }
            case SCENE_UPDATE -> {
                System.out.println(message.data.sceneDataUpdate);
                if (message.data.sceneDataUpdate.objects != null) {
                    var jsonArr = message.data.sceneDataUpdate.objects;
                    Rectangle[] arr = new Rectangle[jsonArr.length];
                    for (int i = 0; i < jsonArr.length; i++) {
                        var object = MessageEncoder.gson.fromJson(jsonArr[i], Rectangle.class);
                        arr[i] = object;
                    }
                    manager.sceneManager.scene.applyCorrection(arr);
                }
            }
        }
    }

    public void setManager(ClientManager manager) {
        this.manager = manager;
    }
}
