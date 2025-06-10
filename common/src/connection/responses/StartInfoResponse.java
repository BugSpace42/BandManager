package connection.responses;

import java.io.Serializable;

public class StartInfoResponse extends Response implements Serializable {
    private CommandMapResponse commandMapResponse;
    private KeyListResponse keyList;
    private IdListResponse idList;
    private static final long serialVersionUID = 42L;

    public StartInfoResponse(CommandMapResponse commandMapResponse, KeyListResponse keyList, IdListResponse idList) {
        this.commandMapResponse = commandMapResponse;
        this.keyList = keyList;
        this.idList = idList;
    }

    public CommandMapResponse getCommandMapResponse() {
        return commandMapResponse;
    }

    public IdListResponse getIdList() {
        return idList;
    }

    public KeyListResponse getKeyList() {
        return keyList;
    }
}
