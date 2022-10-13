package ru.netology.data;

import com.google.gson.Gson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.client.Request;


import static ru.netology.client.Client.getCurrentDate;

class StatisticTest {
    public static Statistic statistic;
    public Gson gson = new Gson();
    public String currentDate = getCurrentDate();


    @BeforeEach
    public void createStatistic(){
        statistic = new Statistic();
    }

    @Test
    void getMaxCategoryBySum() {

        Request clientRequest = gson.fromJson("{\"title\": \"булка\", \"date\": \"" + currentDate + "\", \"sum\": 35}", Request.class);
        statistic.addItem(clientRequest);

        clientRequest = gson.fromJson("{\"title\": \"колбаса\", \"date\": \"" + currentDate + "\", \"sum\": 568}", Request.class);
        statistic.addItem(clientRequest);

        clientRequest = gson.fromJson("{\"title\": \"акции\", \"date\": \"" + currentDate + "\", \"sum\": 10000}", Request.class);
        statistic.addItem(clientRequest);

        Assertions.assertEquals(10000, statistic.getMaxCategory().getSum());

    }

    @Test
    void getMaxCategoryName() {

        Request clientRequest = gson.fromJson("{\"title\": \"булка\", \"date\": \"" + currentDate + "\", \"sum\": 35}", Request.class);
        statistic.addItem(clientRequest);

        clientRequest = gson.fromJson("{\"title\": \"автомобиль\", \"date\": \"" + currentDate + "\", \"sum\": 2500000}", Request.class);
        statistic.addItem(clientRequest);

        clientRequest = gson.fromJson("{\"title\": \"акции\", \"date\": \"" + currentDate + "\", \"sum\": 10000}", Request.class);
        statistic.addItem(clientRequest);

        Assertions.assertEquals("другое", statistic.getMaxCategory().getCategoryName());
    }


    @Test
    void getServerResponse() {
        Request clientRequest = gson.fromJson("{\"title\": \"тапки\", \"date\": \"" + currentDate + "\", \"sum\": 267}", Request.class);
        statistic.addItem(clientRequest);

        clientRequest = gson.fromJson("{\"title\": \"шапка\", \"date\": \"" + currentDate + "\", \"sum\": 5796}", Request.class);
        statistic.addItem(clientRequest);

        clientRequest = gson.fromJson("{\"title\": \"мыло\", \"date\": \"" + currentDate + "\", \"sum\": 21}", Request.class);
        statistic.addItem(clientRequest);

        Assertions.assertEquals("\"maxCategory\": {" +
                "    \"category\": \"" + "одежда" + "\"," +
                "    \"sum\": \"" + "6063" + "\"" +
                "  }", statistic.getServerResponse());
    }

}