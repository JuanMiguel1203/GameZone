package org.zonagamer.entities;

import org.zonagamer.interfaces.Displayable;
import org.zonagamer.interfaces.Sellable;

public class DigitalVideogame extends Videogame implements Sellable, Displayable {

    private double sizeGB;
    private String downloadPlatform;

    public DigitalVideogame(String title, double price, String platform, int stock, String genre, double sizeGB, String downloadPlatform) {
        super(title, price, platform, stock, genre);
        this.sizeGB = sizeGB;
        this.downloadPlatform = downloadPlatform;
    }

    public double getSizeGB() { return sizeGB; }
    public String getDownloadPlatform() { return downloadPlatform; }
    public void setSizeGB(double sizeGB) { this.sizeGB = sizeGB; }
    public void setDownloadPlatform(String downloadPlatform) { this.downloadPlatform = downloadPlatform; }

    @Override
    public double calculateFinalPrice() {
        if (sizeGB > 50) return price + 5000;
        return price;
    }

    @Override
    public double sell(int qty) { return calculateFinalPrice() * qty; }

    @Override
    public String getDisplayInfo() { return toString(); }

    @Override
    public Object[] toTableRow() {
        return new Object[]{title, price, platform, stock, genre, sizeGB, downloadPlatform, "Digital"};
    }

    @Override
    public String toString() {
        return "Título: " + title + " | Precio: $" + price + " | Plataforma: " + platform +
                " | Stock: " + stock + " | Género: " + genre +
                " | SizeGB: " + sizeGB + " | Download Platform: " + downloadPlatform;
    }
}