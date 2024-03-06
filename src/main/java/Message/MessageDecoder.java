package Message;

import com.google.gson.Gson;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

public class MessageDecoder implements Decoder.Text<MessageType> {
    private static Gson gson = new Gson();

    @Override
    public MessageType decode(String s) throws DecodeException {
        return gson.fromJson(s, MessageType.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }
}
