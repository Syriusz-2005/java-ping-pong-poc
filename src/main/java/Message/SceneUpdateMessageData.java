package Message;

import Physics.Rectangle;

import java.util.Arrays;

public class SceneUpdateMessageData {
    public String[] objects;
    public long timestamp;

    @Override
    public String toString() {
        return "SceneUpdateMessageData{" +
                "objects=" + Arrays.toString(objects) +
                ", timestamp=" + timestamp +
                '}';
    }
}
