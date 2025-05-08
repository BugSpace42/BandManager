package connection.responses;

import java.io.Serializable;

public class Response implements Serializable {
    private final int code;
    private final String error;

    public Response(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }
}
