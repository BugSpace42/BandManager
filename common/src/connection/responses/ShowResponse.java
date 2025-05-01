package connection.responses;

public class ShowResponse extends Response{
    public ShowResponse(int code, String error) {
        super(code, error);
    }

    public ShowResponse(int code) {
        super(code, null);
    }
}
