package Server;

public class User {
    private UserConfig config;
    public User(UserConfig config) throws InvalidDataException {
        if (config.username().length() > 30) {
            throw new InvalidDataException("The username length is too big!");
        }
        this.config = config;
    }
}
