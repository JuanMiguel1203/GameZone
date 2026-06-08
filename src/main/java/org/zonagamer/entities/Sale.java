package org.zonagamer.entities;

import java.time.LocalDateTime;

public class Sale {

    private String id;
    private Videogame videoGame;
    private int quantity;
    private double unitPrice;
    private double total;
    private LocalDateTime saleDate;

    public Sale(String id, Videogame videoGame, int quantity, double unitPrice) {
        this.id = id;
        this.videoGame = videoGame;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = unitPrice * quantity;
        this.saleDate = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Videogame getVideoGame() { return videoGame; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal() { return total; }
    public LocalDateTime getSaleDate() { return saleDate; }

    @Override
    public String toString() {
        return "🆔 ID: " + id +
                " | 🎮 Juego: " + videoGame.getTitle() +
                " | 📦 Cantidad: " + quantity +
                " | 💲 Precio Unit.: $" + unitPrice +
                " | 💰 Total: $" + total +
                " | 📅 Fecha: " + saleDate;
    }
}