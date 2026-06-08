package org.zonagamer.entities;

public abstract class Videogame {

    protected String title;
    protected double price;
    protected String platform;
    protected int stock;
    protected String genre;

    public Videogame(String title, double price, String platform, int stock, String genre) {
        this.title = title;
        this.price = price;
        this.platform = platform;
        this.stock = stock;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getPlatform() { return platform; }
    public int getStock() { return stock; }
    public String getGenre() { return genre; }
    public void setStock(int stock) { this.stock = stock; }

    public abstract double calculateFinalPrice();

    @Override
    public String toString() {
        return "Título: " + title + " | Precio: $" + price + " | Plataforma: " + platform +
                " | Stock: " + stock + " | Género: " + genre;
    }
}