package Message;

public class MessageData {
    public String gameCode;

    public GameKickMessageData gameKick = new GameKickMessageData();
    public GameStateUpdateMessageData gameStateUpdate = new GameStateUpdateMessageData();
    public GameMetadataUpdateMessageData gameMetadataUpdate = new GameMetadataUpdateMessageData();
    public SceneUpdateMessageData sceneDataUpdate = new SceneUpdateMessageData();
}
