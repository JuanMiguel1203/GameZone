package org.zonagamer.services;

import org.zonagamer.entities.DigitalVideogame;
import org.zonagamer.entities.PhysicalVideoGame;
import org.zonagamer.entities.Sale;
import org.zonagamer.entities.Videogame;
import org.zonagamer.repositories.VideoGameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VideoGameService {

    private VideoGameRepository repository;

    public VideoGameService() {
        this.repository = new VideoGameRepository();
    }

    public void addVideoGame(Videogame game, String type) {
        if (game.getTitle() == null || game.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("El título no puede ser nulo o vacío.");
        if (game.getPrice() <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        if (game.getStock() < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        if (repository.findByTitle(game.getTitle()) != null)
            throw new IllegalArgumentException("El videojuego ya existe en el catálogo.");
        repository.save(game, type);
    }

    public List<Videogame> getAllVideoGames() { return repository.findAll(); }

    public Videogame findByTitle(String title) { return repository.findByTitle(title); }

    public List<Videogame> findByPlatform(String platform) {
        List<Videogame> result = new ArrayList<>();
        for (Videogame g : repository.findAll())
            if (g.getPlatform().equalsIgnoreCase(platform)) result.add(g);
        return result;
    }

    public void updateVideoGame(String title, Videogame newGame, String type) {
        repository.update(title, newGame, type);
    }

    public void deleteVideoGame(String title) { repository.delete(title); }

    public Sale sellVideoGame(String title, int quantity) {
        Videogame game = repository.findByTitle(title);
        if (game == null) throw new IllegalArgumentException("El videojuego no existe en el catálogo.");
        if (game.getStock() < quantity) throw new IllegalArgumentException("Stock insuficiente.");
        game.setStock(game.getStock() - quantity);
        String type = (game instanceof DigitalVideogame) ? "Digital" : "Physical";
        repository.update(title, game, type);
        double unitPrice = game.calculateFinalPrice();
        Sale sale = new Sale(UUID.randomUUID().toString(), game, quantity, unitPrice);
        repository.saveSale(sale);
        return sale;
    }

    public List<Sale> getAllSales() { return repository.findAllSales(); }
}