package Server;

public class User {
    private final UserConfig config;
    private Player player;

    public User(UserConfig config) throws InvalidDataException {
        if (config.username().length() > 30) {
            throw new InvalidDataException("The username length is too big!");
        }
        this.config = config;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UserConfig getConfig() {
        return config;
    }
}
