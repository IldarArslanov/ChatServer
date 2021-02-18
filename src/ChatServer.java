import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class ChatServer {
    ArrayList<Client> clients = new ArrayList<>();


    ServerSocket serverSocket;

    public ChatServer() throws IOException {
        serverSocket= new ServerSocket(1234);
    }

    void senALL(String message) {
        for(Client client : clients) {
            // Смотрим сколько клиентов получат сообщение
            System.out.println("number of clients " + clients.size() );
            client.receive("number of clients " + clients.size() );
            client.receive(message);
        }
    }

    public void run() {
        while(true) {
            System.out.println("Waiting....");
//Добавляем клиента
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                clients.add( new Client(socket, this));
            } catch(IOException e){
                e.printStackTrace();
            }
//Пытаюсь перебрать кдиентов с списке и убрать несуществующих
        try {
            Iterator<Client> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Client clientChecking = iterator.next();
                if (!clientChecking.socket.isConnected()) {
                    System.out.println("contact no exist");
                    try {
                        clientChecking.socket.close();
                        iterator.remove();
                        senALL("minus one client");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("error closing socket");
                    }
                    System.out.println("client removed");
                } else {
                    System.out.println("all cliets connected");
                }
            }
        } catch(ConcurrentModificationException e) {
            System.out.println("no element");
            }
        }
    }

    public static void main(String[] args) throws IOException{
        new ChatServer().run();

    }
}
