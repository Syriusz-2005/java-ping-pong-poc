package Message;

public class MessageType {
    private CommandType command;

    public MessageType setCommand(CommandType command) {
        this.command = command;
        return this;
    }

    public CommandType getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "command=" + command +
                '}';
    }
}
