import common.LevelLog;
import common.Log;
import connection.TcpConnection;

import java.io.IOException;

public class ClientListener extends Thread {
    private final TcpConnection<String> connection;
    private final Log log;

    public ClientListener(TcpConnection<String> connection, Log log) {
        this.connection = connection;
        this.log = log;
        start();
    }

    @Override
    public void run() {
        while (this.connection.isConnected()) {
            try {
                String msg = this.connection.receive();
                if (checkMessage(msg.trim())) {
                    System.out.println(msg);
                    this.log.log(LevelLog.INFO, "Client receive message > " + msg);
                } else {
                    this.log.log(LevelLog.INFO, "Client receive empty message!");
                }
            } catch (IOException e) {
                this.log.log(LevelLog.ERROR, "listenForMessage error > " + e.getMessage());
                break;
            }
        }
        this.connection.close();
    }

    private boolean checkMessage(String message) {
        return message != null && !message.equals("") && !message.equalsIgnoreCase("null");
    }
}
