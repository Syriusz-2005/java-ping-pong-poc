package Message;

import Server.CommandType;

public class MessageType {
    private CommandType command;

    public void setCommand(CommandType command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "command=" + command +
                '}';
    }
}
