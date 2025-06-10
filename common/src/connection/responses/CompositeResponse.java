package connection.responses;

import java.io.Serializable;

public class CompositeResponse extends Response implements Serializable {
    private CommandResponse commandResponse;
    private KeyListResponse keyList;
    private IdListResponse idList;
    private static final long serialVersionUID = 42L;

    public CompositeResponse(CommandResponse commandResponse, KeyListResponse keyList, IdListResponse idList) {
        this.commandResponse = commandResponse;
        this.keyList = keyList;
        this.idList = idList;
    }

    public CommandResponse getCommandResponse() {
        return commandResponse;
    }

    public IdListResponse getIdList() {
        return idList;
    }

    public KeyListResponse getKeyList() {
        return keyList;
    }
}
