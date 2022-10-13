import com.google.gson.Gson;
import ru.netology.client.Request;
import ru.netology.data.Statistic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            System.out.println("Сервер стартовал.");

            Gson gson = new Gson();
            File savedData = new File("data.bin");
            Statistic statistic = new Statistic();

            if (!savedData.createNewFile()) {
                statistic = Statistic.loadFromBinFile();
            }

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
                    statistic.saveBin();
                } catch (Exception e) {
                    throw new RuntimeException("Не могу сохранить статистику :(", e);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер :(");
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException("Не могу загрузить статистику :(", e);
        }
    }
}
