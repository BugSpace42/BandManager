package main.java.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.Selector;

public class TCPClient extends AbstractTCPClient{
    public TCPClient(InetAddress addr, int port, Selector selector) throws IOException {
        super(addr, port, selector);
    }
}
