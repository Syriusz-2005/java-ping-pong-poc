package Message;

import com.google.gson.Gson;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class MessageEncoder implements Encoder.Text<MessageType> {
    private static Gson gson = new Gson();

    @Override
    public String encode(MessageType message) throws EncodeException {
        return gson.toJson(message);
    }
}
