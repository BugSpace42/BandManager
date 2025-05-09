package main.java.connection;

import java.io.IOException;
import java.net.InetAddress;

public class TCPServer extends AbstractTCPServer {
    public TCPServer(InetAddress addr, int port) throws IOException {
        super(addr, port);
    }
}
