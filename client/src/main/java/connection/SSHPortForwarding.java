package main.java.connection;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SSHPortForwarding {
    private static final Logger logger = LogManager.getLogger(SSHPortForwarding.class);

    private static final int sshPort = 2222; // порт ssh
    private static final String sshUser = "s409478";
    private static final String sshPassword = "uEtl*8862";

    private static final int localPort = 54321; // локальный порт для приложения
    private static final String remoteHost = "localhost"; // хост на сервере, куда перенаправляем
    private static final int remotePort = 12345; // порт сервера, с которым работает ваше приложение

    public static void connect() {
        String sshHost = "helios.se.ifmo.ru"; // адрес сервера
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            session.setPortForwardingL(localPort, remoteHost, remotePort);

            logger.info("Туннель установлен на localhost:" + localPort);
        } catch (Exception e) {
            logger.error("Произошла ошибка при установлении туннеля", e);
        }
    }

    public static int getSshPort() {
        return sshPort;
    }

    public static int getLocalPort() {
        return localPort;
    }

    public static int getRemotePort() {
        return remotePort;
    }
}
