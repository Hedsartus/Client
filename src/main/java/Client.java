import common.LevelLog;
import common.Log;
import connection.TcpConnection;
import connection.TransportStringConnection;
import service.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private TcpConnection<String> connection;
    private final Scanner scanner = new Scanner(System.in);
    private final Log log = Log.getInstance();
    private ClientListener listener;

    public Client(Socket socket) {
        try {
            this.connection = new TransportStringConnection(socket);
            this.listener = new ClientListener(this.connection, this.log);
            this.log.log(LevelLog.INFO, "Client start session");
            sendMessage();
        } catch (IOException e) {
            this.connection.close();
            this.log.log(LevelLog.ERROR, "TcpConnection error : " + e.getMessage());
        }
    }

    public void sendMessage() {
        while (this.connection.isConnected()) {
            String msg = scanner.nextLine();
            this.connection.send(msg);
            this.log.log(LevelLog.INFO, "Client send message > " + msg);

            if (msg.equalsIgnoreCase("exit")) {
                this.listener.interrupt();
                this.log.log(LevelLog.INFO, "Client exit from chat");
                break;
            }
        }
        this.connection.close();
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        Service service = new Service();
        Socket socket = new Socket(service.getIp(), service.getPort());
        new Client(socket);
    }
}
