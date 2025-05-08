package main.java.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class TCPServer extends AbstractTCPServer {
    private static final Logger logger = LogManager.getLogger();
    private final InetSocketAddress addr;

    public TCPServer(InetAddress addr, int port) throws IOException {
        super(addr, port);
        this.addr = new InetSocketAddress(addr, port);
    }
}
