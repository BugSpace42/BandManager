package connection;

import java.io.IOException;
import java.net.InetAddress;

public class TCPClient extends AbstractTCPClient{
    public TCPClient(InetAddress addr, int port) throws IOException {
        super(addr, port);
    }
}
