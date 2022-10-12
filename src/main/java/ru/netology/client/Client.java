package ru.netology.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    private static final int PORT = 8989;
    private static final String IP = "127.0.0.1";

    public static void main(String[] args) {
        while (true) {
            try (Socket clientSocket = new Socket(IP, PORT);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("Введите наименование товара и сумму покупки через пробел или end.");
                String input = reader.readLine();
                String[] inputSplit = input.split(" ");

                if (input.equals("end")) {
                    System.out.println("Программа завершена");
                    break;
                } else if (inputSplit.length > 1) {
                    String itemName = inputSplit[0];
                    String currentDate = getCurrentDate();
                    int sum = Integer.parseInt(inputSplit[1]);

                    out.println("{\"title\": \"" + itemName + "\", \"date\": \"" + currentDate + "\", " +
                            "\"sum\": " + sum + "}");
                    System.out.println("Данные о покупке переданы на сервер.");

                    System.out.println("Ваши траты максимальны в следующей категории покупок:");
                    System.out.println(in.readLine());
                } else {
                    System.out.println("Неверный ввод.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
