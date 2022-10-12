import com.google.gson.Gson;
import ru.netology.client.Request;
import ru.netology.data.Statistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            System.out.println("Сервер стартовал.");
            Gson gson = new Gson();
            Statistic statistic = new Statistic();
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
                ) {
                    String input = in.readLine();
                    Request clientRequest = gson.fromJson(input, Request.class);
                    statistic.addItem(clientRequest);

                    String serverResponse = statistic.getServerResponse();

                    System.out.println("Максимальные траты клиента: ");
                    System.out.println(serverResponse);
                    out.println(serverResponse);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер :(");
            e.printStackTrace();
        }
    }
}
