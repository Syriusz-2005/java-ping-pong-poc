package Server;

import Message.CommandType;
import Message.MessageType;

public class User {
    private final UserConfig config;
    private Player player;
    private ConnectionEndpoint endpoint;

    public User(UserConfig config) throws InvalidDataException {
        if (config.username().length() > 30) {
            throw new InvalidDataException("The username length is too big!");
        }
        this.config = config;
        this.endpoint = config.endpoint();
    }

    public void setPlayer(Player player) {
        if (player == null) {
            var msg = new MessageType().setCommand(CommandType.GAME_KICK);
            endpoint.postMessage(msg);
        } else {
            var msg = new MessageType().setCommand(CommandType.GAME_JOIN);
            msg.data.gameCode = player.getGame().gameCode;
            endpoint.postMessage(msg);
        }
        this.player = player;
    }

    public UserConfig getConfig() {
        return config;
    }
}
