package connection.responses;

import java.util.List;

public class KeyListResponse extends Response {
    private final List<Integer> keys;

    public KeyListResponse(List<Integer> keys) {
        this.keys = keys;
    }

    public List<Integer> getKeyList() {
        return keys;
    }
}
