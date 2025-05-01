package main.java.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TCPServer extends AbstractTCPServer {
    private static final Logger logger = LogManager.getLogger();
    private final InetSocketAddress addr;

    private boolean running = false;

    public TCPServer(InetAddress addr, int port) throws IOException {
        super(addr, port);
        this.addr = new InetSocketAddress(addr, port);
    }

    public static void main(String[] args) {
        Pair<Integer, String> pair = new Pair<>(123, "424224");
        System.out.println("pair: " + pair);
        logger.info("Log4j2ExampleApp started.");
        logger.warn("Something to warn");
        logger.error("Something failed.");
        try {
            Files.readAllBytes(Paths.get("/file/does/not/exist"));
        } catch (IOException e) {
            logger.error("Error message", e);
        }
    }
}
