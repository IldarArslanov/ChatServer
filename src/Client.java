import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client implements Runnable {
    Socket socket;
    ChatServer server;
    Scanner in;
    PrintStream out;

    public Client(Socket socket, ChatServer server) {

        this.socket = socket;
        this.server =server;
        //Сразу запускаем
        new Thread(this).start();
    }

    void receive(String message) {
        out.println(message);
    }

    public void run() {
        try {

            InputStream is = socket.getInputStream();
            OutputStream os=socket.getOutputStream();

             in = new Scanner(is);
             out = new PrintStream(os);

            out.println("welcom chat");
            String input = in.nextLine();

            while(!input.equals("buy")) {
                server.senALL(input);
        // Пытаюсь обезопасить прием сообщения
                try {
                    input = in.nextLine();
                } catch( NoSuchElementException e) {
                    e.printStackTrace();
                    System.out.println("error input");

                }
            }
            socket.close();


        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
