package connection.responses;

import java.util.List;

public class KeysListResponse extends Response {
    private final List<Integer> keys;

    public KeysListResponse(List<Integer> keys) {
        this.keys = keys;
    }

    public List<Integer> getKeys() {
        return keys;
    }
}
