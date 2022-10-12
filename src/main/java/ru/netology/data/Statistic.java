package ru.netology.data;

import ru.netology.client.Request;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Statistic {

    private final Map<String, Category> itemsByCategory = new TreeMap<>();

    private final Map<String, String> itemsFromRequest = new TreeMap();

    public Statistic() {
        loadCategoriesFromTSV();
    }

    private void loadCategoriesFromTSV() {
        try {
            File tsvFile = new File("categories.tsv");

            System.out.println("Список товаров по категориям загружен из файла.");

            Scanner reader = new Scanner(tsvFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] lineSplit = line.split("\t");

                Category currentCategory;
                String itemName = lineSplit[0];
                String categoryName = lineSplit[1];
                if (itemsByCategory.containsKey(categoryName)) {
                    currentCategory = itemsByCategory.get(categoryName);
                } else {
                    currentCategory = new Category(categoryName);
                }
                currentCategory.addItem(itemName);
                itemsFromRequest.put(itemName, categoryName);
                itemsByCategory.put(categoryName, currentCategory);
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Category getMaxCategory() {
        int maxSum = Integer.MIN_VALUE;
        Category maxCategory = null;

        for (Map.Entry<String, Category> entry : itemsByCategory.entrySet()) {
            if (entry.getValue().getSum() > maxSum) {
                maxSum = entry.getValue().getSum();
                maxCategory = entry.getValue();
            }
        }
        return maxCategory;
    }

    public void addItem(Request request) {
        if (itemsFromRequest.containsKey(request.title)) {
            Category existingCategory = itemsByCategory.get(itemsFromRequest.get(request.title));
            existingCategory.totalSum(request);
        } else {
            Category newCategory;
            if (itemsByCategory.containsKey("другое")) {
                newCategory = itemsByCategory.get("другое");
            } else {
                newCategory = new Category("другое");
            }
            newCategory.addItem(request.title);
            newCategory.totalSum(request);
            itemsFromRequest.put(request.title, "другое");
            itemsByCategory.put("другое", newCategory);
        }

        System.out.println("Товар \"" + request.title + "\", приобретённый на сумму " + request.sum +
                " р., занесён в категорию \"" + itemsFromRequest.get(request.title) + "\".");
    }

    public String getServerResponse() {
        Category response = getMaxCategory();
        return "  \"maxCategory\": {" +
                "    \"category\": \"" + response.getCategoryName() + "\"," +
                "    \"sum\": \"" + response.getSum() + "\"" +
                "  }";
    }
}
