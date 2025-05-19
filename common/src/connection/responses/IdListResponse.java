package connection.responses;

import java.util.List;

public class IdListResponse extends Response {
    private final List<Long> idList;

    public IdListResponse(List<Long> idList) {
        this.idList = idList;
    }

    public List<Long> getIdList() {
        return idList;
    }
}
