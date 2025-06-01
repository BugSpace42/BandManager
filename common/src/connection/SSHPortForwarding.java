package connection;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SSHPortForwarding {
    private static final Logger logger = LogManager.getLogger(SSHPortForwarding.class);

    private static final String sshHost = "helios.se.ifmo.ru"; // адрес сервера
    private static final int sshPort = 2222; // порт ssh
    private static final String sshUser = "s409478";
    private static final String sshPassword = "uEtl*8862";

    private static final int localPort = 54321; // локальный порт для приложения
    private static final String remoteHost = "127.0.0.1"; // хост на сервере, куда перенаправляем
    private static final int remotePort = 5432; // порт сервера, с которым работает ваше приложение

    private static Session session;

    public static void connect() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, sshPort);
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

    public static void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            logger.info("Туннель закрыт");
        }
    }

    public static String getSshHost() {
        return sshHost;
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
