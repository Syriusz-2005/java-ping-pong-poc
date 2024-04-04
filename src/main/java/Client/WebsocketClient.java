package Client;

import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.MessageType;
import Physics.Rectangle;
import Server.GameState;
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
                var gameCode = message.data.gameCode;
                Logger.printOk("Successfully joined the game");
                Logger.printOk("Game code: " + gameCode);
                manager.setGameCode(gameCode);
                manager.setGameState(GameState.WAITING_IN_LOBBY);
            }
            case GAME_KICK -> {
                Logger.printErr("You've been kicked, reason: " + message.data.gameKick.reason);
            }
            case GAME_METADATA_UPDATE -> {
                manager.sceneManager.setSimulationStepsPerSecond(message.data.gameMetadataUpdate.simulationStepsPerSecond);
            }
            case GAME_STATE_UPDATE -> {
                manager.setGameState(message.data.gameStateUpdate.newState);
            }
            case SCENE_UPDATE -> {
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
            case GAME_END_SUMMARY -> {
                var data = message.data.gameEndSummary;
                System.out.println(data.isWin ? "You won!" : "You lost!");
            }
        }
    }

    public void setManager(ClientManager manager) {
        this.manager = manager;
    }
}
