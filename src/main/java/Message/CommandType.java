package Message;

public enum CommandType {
    TEST_CONNECTION,
    GAME_JOIN_REQUEST,
    CREATE_GAME,


    GAME_JOIN,
    GAME_KICK,
    GAME_PLAYER_DISCONNECTED,
    GAME_STATE_UPDATE,
    GAME_METADATA_UPDATE,
    SCENE_UPDATE,
}
