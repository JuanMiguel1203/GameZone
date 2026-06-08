package org.zonagamer.repositories;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.zonagamer.entities.DigitalVideogame;
import org.zonagamer.entities.PhysicalVideoGame;
import org.zonagamer.entities.Sale;
import org.zonagamer.entities.Videogame;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoGameRepository {

    private static final String FILE_PATH = "data/videogames.json";
    private static final String SALES_FILE_PATH = "data/sales.json";
    private Gson gson;

    public VideoGameRepository() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try { file.createNewFile();
                try (Writer w = new FileWriter(file)) { w.write("[]"); }
            } catch (IOException e) { e.printStackTrace(); }
        }
        File salesFile = new File(SALES_FILE_PATH);
        if (!salesFile.exists()) {
            try { salesFile.createNewFile();
                try (Writer w = new FileWriter(salesFile)) { w.write("[]"); }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public List<Videogame> findAll() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> jsonObjects = gson.fromJson(reader, listType);
            List<Videogame> games = new ArrayList<>();
            if (jsonObjects == null) return games;
            for (JsonObject obj : jsonObjects) {
                String type = obj.get("type").getAsString();
                if (type.equals("Digital")) games.add(gson.fromJson(obj, DigitalVideogame.class));
                else games.add(gson.fromJson(obj, PhysicalVideoGame.class));
            }
            return games;
        } catch (IOException e) { return new ArrayList<>(); }
    }

    public void save(Videogame game, String type) {
        List<JsonObject> list = readRawList();
        JsonObject obj = gson.toJsonTree(game).getAsJsonObject();
        obj.addProperty("type", type);
        list.add(obj);
        writeList(list);
    }

    public void update(String title, Videogame newGame, String type) {
        List<JsonObject> list = readRawList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("title").getAsString().equalsIgnoreCase(title)) {
                JsonObject obj = gson.toJsonTree(newGame).getAsJsonObject();
                obj.addProperty("type", type);
                list.set(i, obj);
                break;
            }
        }
        writeList(list);
    }

    public void delete(String title) {
        List<JsonObject> list = readRawList();
        list.removeIf(obj -> obj.get("title").getAsString().equalsIgnoreCase(title));
        writeList(list);
    }

    public Videogame findByTitle(String title) {
        return findAll().stream()
                .filter(g -> g.getTitle().equalsIgnoreCase(title))
                .findFirst().orElse(null);
    }

    public void saveSale(Sale sale) {
        List<JsonObject> list = readRawSaleList();
        JsonObject obj = new JsonObject();
        obj.addProperty("id", sale.getId());
        obj.addProperty("gameTitle", sale.getVideoGame().getTitle());
        obj.addProperty("quantity", sale.getQuantity());
        obj.addProperty("unitPrice", sale.getUnitPrice());
        obj.addProperty("total", sale.getTotal());
        obj.addProperty("saleDate", sale.getSaleDate().toString());
        list.add(obj);
        writeSaleList(list);
    }

    public List<Sale> findAllSales() {
        try (Reader reader = new FileReader(SALES_FILE_PATH)) {
            Type listType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> jsonObjects = gson.fromJson(reader, listType);
            List<Sale> sales = new ArrayList<>();
            if (jsonObjects == null) return sales;
            for (JsonObject obj : jsonObjects) {
                String gameTitle = obj.get("gameTitle").getAsString();
                Videogame game = findByTitle(gameTitle);
                if (game != null) {
                    Sale sale = new Sale(
                            obj.get("id").getAsString(), game,
                            obj.get("quantity").getAsInt(),
                            obj.get("unitPrice").getAsDouble()
                    );
                    sales.add(sale);
                }
            }
            return sales;
        } catch (IOException e) { return new ArrayList<>(); }
    }

    private List<JsonObject> readRawList() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) { return new ArrayList<>(); }
    }

    private void writeList(List<JsonObject> list) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(list, writer);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private List<JsonObject> readRawSaleList() {
        try (Reader reader = new FileReader(SALES_FILE_PATH)) {
            Type listType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) { return new ArrayList<>(); }
    }

    private void writeSaleList(List<JsonObject> list) {
        try (Writer writer = new FileWriter(SALES_FILE_PATH)) {
            gson.toJson(list, writer);
        } catch (IOException e) { e.printStackTrace(); }
    }
}